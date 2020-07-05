package org.example.demorss.util;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import org.example.demorss.model.Feed;
import org.example.demorss.model.FeedMessage;
import org.xml.sax.InputSource;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class ParserFeed {

    public Feed read(InputStream inputStream) {

        SyndFeedInput input = new SyndFeedInput();
        try {
            SyndFeed feed = input.build(new InputSource(inputStream));

            String title = feed.getTitle();
            String description = feed.getDescription();
            String link = feed.getLink();
            String language = feed.getLanguage();
            String copyright = feed.getCopyright();
            Date date = feed.getPublishedDate();
            String dateStr = (date != null) ? date.toString() : "";

            Feed feed2 = new Feed(title, link, description, language, copyright, dateStr);

            if (feed.getEntries() != null) {
                for (var entry : feed.getEntries()) {
                    SyndEntry entry2 = (SyndEntry) entry;
                    FeedMessage feedMessage = new FeedMessage();
                    feedMessage.setTitle(entry2.getTitle());
                    feedMessage.setAuthor(entry2.getAuthor());
                    feedMessage.setDescription(entry2.getDescription().getValue());
                    feedMessage.setGuid(entry2.getUri());
                    feedMessage.setLink(entry2.getLink());
                    Date date2 = entry2.getPublishedDate();
                    LocalDateTime datePublication = null;
                    if (date2 != null) {
                        datePublication=convertToLocalDateTimeViaInstant(date2);
                    }
                    feedMessage.setDatePublication(datePublication);
                    feed2.getMessages().add(feedMessage);
                }
            }

            return feed2;
        } catch (FeedException e) {
            throw new RuntimeException(e);
        }
    }

    public LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
