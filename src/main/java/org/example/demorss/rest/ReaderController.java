package org.example.demorss.rest;

import org.example.demorss.dto.ListFluxDTO;
import org.example.demorss.dto.ListRssDTO;
import org.example.demorss.service.ReadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ReaderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReaderController.class);

    @Autowired
    private ReadService readService;

    @GetMapping("/api/read")
    public ListFluxDTO read(){
        LOGGER.info("read");
        return readService.read();
    }
    @GetMapping("/api/list-rss")
    public ListRssDTO listRss() throws IOException {
        LOGGER.info("listRss");
        return readService.listeRss();
    }

}
