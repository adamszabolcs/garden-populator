package hu.plantation.webscraper.controller;

import hu.plantation.webscraper.services.WebCrawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {

    @Autowired
    WebCrawler webCrawler;

    @GetMapping("/populate")
    public ResponseEntity<?> populatePlants() {
        webCrawler.getPageLinks();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
