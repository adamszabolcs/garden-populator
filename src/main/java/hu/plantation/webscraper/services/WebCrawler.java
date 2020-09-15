package hu.plantation.webscraper.services;

import hu.plantation.webscraper.enums.SunRequirements;
import hu.plantation.webscraper.enums.WaterRequirements;
import hu.plantation.webscraper.exceptions.WrongEnumException;
import hu.plantation.webscraper.model.Plant;
import hu.plantation.webscraper.config.PropertyConfig;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Crawl through the www.garden.org website and
 * gets the necessary information from it.
 *
 * @author szabolcs.adam
 * @version 0.1
 * @since 2020-09-13
 */
@Component
public class WebCrawler {

    @Autowired
    PropertyConfig propertyConfig;

    public void getPageLinks() {
        try {
            Set<String> list = new HashSet<>();
            String webPageName = "https://garden.org";
            for (String url : propertyConfig.getUrls()) {
                Document document = Jsoup.connect(url).get();
                Element body = document.getElementsByClass("panel-body").first();
                Elements tdElements = body.getElementsByAttribute("data-th");
                for (Element td : tdElements) {
                    if (!StringUtils.isEmpty(td.text())) {
                        list.add(td.getElementsByTag("a").first().attr("href"));
                    }
                }
            }
            populatePlants(list, webPageName);
        } catch (IOException | WrongEnumException e) {
            e.printStackTrace();
        }

    }

    /**
     * Iterates through the urls of the given list.
     * Gets the sourcecode of the url, and gets the necessary information from it.
     * @param list - the list of the urls
     * @throws IOException - if the given url not exists.
     */
    private void populatePlants(Set<String> list, String webPageName) throws IOException, WrongEnumException {
        for (String url : list) {
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

            List<SunRequirements> sunRequirementsList = getElements(document, "sun", "Sun Requirements");

            List<WaterRequirements> waterRequirementsList = getElements(document, "water", "Water Preferences");

            String height = document.getElementsContainingOwnText("Plant Height").parents().eq(1).next().text();

            Plant plant = Plant.builder()
                    .simpleName(plantSimpleName)
                    .scientificName(plantScientificName)
                    .sunReq(sunRequirementsList)
                    .watering(waterRequirementsList)
                    .height(height)
                    .build();
            System.out.println(plant.toString());
        }
    }

    @SuppressWarnings("unchecked")
    private <E extends Enum<E>> List<E> getElements(Document document, String req, String ownText) throws WrongEnumException {

        List<E> resultList = new ArrayList<>();
        Element element = document.getElementsContainingOwnText(ownText).first();
        if (element != null) {
            String[] splittedElement = element.nextElementSibling().html().split("<br>");
            for (String elem : splittedElement) {
                if (elem.trim().startsWith("<span")) {
                    elem = elem.substring(elem.indexOf(">") + 1, elem.indexOf("</span>"));
                }
                if (req.equals("sun")) {
                    resultList.add((E) SunRequirements.get(elem.trim()));
                } else if (req.equals("water")) {
                    resultList.add((E) WaterRequirements.get(elem.trim()));
                } else {
                    throw new WrongEnumException("No enumtype found to the given String " + req + ".");
                }
            }
        }
        return resultList;
    }

}