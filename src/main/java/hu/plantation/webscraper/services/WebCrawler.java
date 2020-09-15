package hu.plantation.webscraper.services;

import hu.plantation.webscraper.enums.SunRequirements;
import hu.plantation.webscraper.enums.WaterRequirements;
import hu.plantation.webscraper.exceptions.WrongEnumException;
import hu.plantation.webscraper.model.Plant;
import hu.plantation.webscraper.config.PropertyConfig;
import hu.plantation.webscraper.repository.PlantRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Crawl through the www.garden.org website and
 * gets the necessary information from it.
 *
 * @author szabolcs.adam
 * @version 0.1
 * @since 2020-09-13
 */
@Service
public class WebCrawler {

    private static final Logger LOG = LoggerFactory.getLogger(WebCrawler.class);

    @Autowired
    PropertyConfig propertyConfig;

    @Autowired
    PlantRepository plantRepository;

    public void getPageLinks() {
        try {
            LOG.info("--- getPageLinks method started! ---");
            Set<String> list = new HashSet<>();
            String webPageName = "https://garden.org";
            int i = 1;
            for (String url : propertyConfig.getUrls()) {
                try {
                    LOG.info("--- URL no. " + i + " ---");
                    Document document = Jsoup.connect(url).get();
                    Element body = document.getElementsByClass("panel-body").first();
                    Elements tdElements = body.getElementsByAttribute("data-th");
                    for (Element td : tdElements) {
                        if (!StringUtils.isEmpty(td.text())) {
                            list.add(td.getElementsByTag("a").first().attr("href"));
                        }
                    }
                    i++;
                } catch (Exception e) {
                    LOG.info("Error occurred, continuing with URL no. " + ++i);
                }
            }
            LOG.info("URL List populated, there are " + list.size() + " plants in the list!");
            populatePlants(list, webPageName);
        } catch (IOException | WrongEnumException e) {
            e.printStackTrace();
        }
        LOG.info("--- getPageLinks method finished! ---");
    }

    /**
     * Iterates through the urls of the given list.
     * Gets the sourcecode of the url, and gets the necessary information from it.
     * @param list - the list of the urls
     * @throws IOException - if the given url not exists.
     */
    private void populatePlants(Set<String> list, String webPageName) throws IOException, WrongEnumException {
        LOG.info("--- Population started! ---");
        int i = 0;
        for (String url : list) {
            try {
                Document document = Jsoup.connect(webPageName + url).get();
                String plantName = document.getElementsByClass("page-header").first().text();
                String plantSimpleName = "";
                String plantScientificName;
                // If there is not a bracket in the name, then its only the scientific name.
                if (plantName.contains("(")) {
                    plantSimpleName = plantName.substring(0, plantName.indexOf("(") - 1);
                    plantScientificName = plantName.substring(plantName.indexOf("(") + 1, plantName.indexOf(")"));
                } else
                    plantScientificName = plantName;

                Set<SunRequirements> sunRequirementsList = getElements(document, "sun", "Sun Requirements");

                Set<WaterRequirements> waterRequirementsList = getElements(document, "water", "Water Preferences");

                String height = document.getElementsContainingOwnText("Plant Height").parents().eq(1).next().text();

                Plant plant = Plant.builder()
                        .simpleName(plantSimpleName)
                        .scientificName(plantScientificName)
                        //.sunReq(sunRequirementsList)
                        //.watering(waterRequirementsList)
                        .height(height)
                        .build();
                plantRepository.save(plant);
                i++;
                LOG.info("--- Plant no. " + i + " added! ---");
            } catch (Exception e) {
                LOG.info("Error occurred, continuing!");
            }
        }
        LOG.info("--- Population ended, added " + i + " plants to database!");
    }

    @SuppressWarnings("unchecked")
    private <E extends Enum<E>> Set<E> getElements(Document document, String req, String ownText) throws WrongEnumException {

        Set<E> resultSet = new HashSet<>();
        Element element = document.getElementsContainingOwnText(ownText).first();
        if (element != null) {
            String[] splittedElement = element.nextElementSibling().html().split("<br>");
            for (String elem : splittedElement) {
                if (elem.trim().startsWith("<span")) {
                    elem = elem.substring(elem.indexOf(">") + 1, elem.indexOf("</span>"));
                }
                if (req.equals("sun")) {
                    resultSet.add((E) SunRequirements.get(elem.trim()));
                } else if (req.equals("water")) {
                    resultSet.add((E) WaterRequirements.get(elem.trim()));
                } else {
                    throw new WrongEnumException("No enumtype found to the given String " + req + ".");
                }
            }
        }
        return resultSet;
    }

}