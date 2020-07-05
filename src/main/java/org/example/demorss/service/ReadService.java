package org.example.demorss.service;

import org.example.demorss.dto.FluxDTO;
import org.example.demorss.dto.ListFluxDTO;
import org.example.demorss.dto.ListRssDTO;
import org.example.demorss.dto.RssDTO;
import org.example.demorss.model.Feed;
import org.example.demorss.util.Parser;
import org.example.demorss.util.ParserFeed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Service
public class ReadService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReadService.class);

    @Value("${config-file}")
    private String configFile;

    public ListFluxDTO read() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://www.lemonde.fr/rss/une.xml";
        LOGGER.info("call {}", url);
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            var res = response.getBody();
            Feed feed = null;
            try {
                if (false) {
                    Parser parser = new Parser(url);
                    feed = parser.getFeed(new ByteArrayInputStream(res.getBytes(StandardCharsets.UTF_8)));
                } else {
                    ParserFeed parserFeed = new ParserFeed();
                    feed = parserFeed.read(new ByteArrayInputStream(res.getBytes(StandardCharsets.UTF_8)));
                }
                var liste = new ListFluxDTO();
                if (feed != null && !CollectionUtils.isEmpty(feed.getMessages())) {
                    liste.setListe(new ArrayList<>());
                    for (var msg : feed.getMessages()) {
                        var fluxDTO = new FluxDTO();
                        fluxDTO.setTitle(msg.getTitle());
                        fluxDTO.setAuthor(msg.getAuthor());
                        fluxDTO.setDescription(msg.getDescription());
                        fluxDTO.setGuid(msg.getGuid());
                        fluxDTO.setLink(msg.getLink());
                        fluxDTO.setDatePublication(msg.getDatePublication());
                        liste.getListe().add(fluxDTO);
                    }
                }
                return liste;
            } catch (XMLStreamException e) {
                LOGGER.error("erreur", e);
            }
            LOGGER.info("feed: {}", feed);
        } else {
            LOGGER.error("Erreur pour lire le flux");
        }
        return null;
    }

    public ListRssDTO listeRss() throws IOException {
        Path p = Paths.get(configFile);
        Properties properties = new Properties();
        properties.load(Files.newBufferedReader(p));

        ListRssDTO listRssDTO = new ListRssDTO();
        List<RssDTO> liste = new ArrayList<>();
        listRssDTO.setList(liste);

        Set<Object> set = properties.keySet();
        if (set != null) {
            var set2 = set.stream()
                    .filter(x -> x instanceof String)
                    .map(x -> (String) x)
                    .filter(x -> x.startsWith("rss"))
                    .collect(toSet());

            var i = 1;
            do {
                final String name = "rss" + i;
                final String nameId = name + ".id";
                final String nameTitle = name + ".title";
                final String nameUrl = name + ".url";
                if (set2.contains(nameId)) {
                    String s = properties.getProperty(nameId);
                    int id = Integer.parseInt(s);
                    var title = properties.getProperty(nameTitle);
                    var url = properties.getProperty(nameUrl);
                    if (id > 0 && StringUtils.hasText(title) && StringUtils.hasText(url)) {
                        RssDTO rssDTO = new RssDTO();
                        rssDTO.setId(id);
                        rssDTO.setTitle(title);
                        rssDTO.setUrl(url);
                        liste.add(rssDTO);
                    }
                } else {
                    break;
                }
                i++;
            } while (true);
        }

        return listRssDTO;
    }
}
