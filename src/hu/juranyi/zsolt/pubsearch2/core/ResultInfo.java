/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.juranyi.zsolt.pubsearch2.core;

import hu.juranyi.zsolt.pubsearch2.interfaces.IPubData;
import java.util.List;

/**
 *
 * @author Zsolt
 */
public class ResultInfo {

    private final IPubData pubData;
    private final String crawlerName;
    private final String authors;
    private final String title;
    private final String year;
    private final int citedByCount;

    public ResultInfo(IPubData pubData, String crawlerName) {
        this.pubData = pubData;
        this.crawlerName = crawlerName;
        StringBuilder buf = new StringBuilder();
        for (String author : pubData.getAuthors()) {
            buf.append(author);
            if (!author.equals(pubData.getAuthors().get(pubData.getAuthors().size() - 1))) {
                buf.append(", ");
            }
        }
        authors = buf.toString();
        title = pubData.getTitle();
        year = (pubData.getYear() != null) ? pubData.getYear().toString() : "(N/A)";
        citedByCount = (pubData.getCitedByPublications() != null) ? pubData.getCitedByPublications().size() : 0;
    }

    public String getCrawlerName() {
        return crawlerName;
    }

    public String getAuthors() {
        return authors;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public int getCitedByCount() {
        return citedByCount;
    }
}
