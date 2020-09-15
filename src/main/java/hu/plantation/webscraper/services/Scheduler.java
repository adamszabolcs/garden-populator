package hu.plantation.webscraper.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;


@Component
public class Scheduler {

    private static final Logger LOG = LoggerFactory.getLogger(Scheduler.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss");

    @Autowired
    WebCrawler webCrawler;

    @Scheduled(cron = "0 0 12 1 * ?") // Every month on the 1st, at noon
    public void startDatabasePopulate() {
        LOG.info("Scheduled task started at {}", dateFormat.format(new Date()));
        webCrawler.getPageLinks();
    }
}
