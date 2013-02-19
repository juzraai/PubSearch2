package hu.juranyi.zsolt.pubdatacrawlers.pubsearch1x;

import hu.juranyi.zsolt.pubsearch2.interfaces.IDownloader;
import hu.juranyi.zsolt.pubsearch2.interfaces.IPubData;
import hu.juranyi.zsolt.pubsearch2.interfaces.IPubDataCrawler;
import java.util.List;

/**
 * PubSearch 1.x Crawler definition for PubSearch 2.
 *
 * @author Zsolt
 */
public class PubSearch1xCrawler implements IPubDataCrawler {

    // meta
    private final String crawlerName = "PubSearch 1.x Crawler v1.1 (embedded)";
    private final boolean hasOwnDownloader = false;
    // init
    private String author;
    private String title;
    private int transitivityLevel;
    private int threadCount;
    private IDownloader downloader;
    // progress
    private String progressText;
    private double progressPercent;
    // result
    private String errorMessage;
    private List<IPubData> publications;

    @Override
    public String getCrawlerName() {
        return crawlerName;
    }

    @Override
    public boolean hasOwnDownloader() {
        return hasOwnDownloader;
    }

    @Override
    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void setTransitivityLevel(int transitivityLevel) {
        this.transitivityLevel = transitivityLevel;
    }

    @Override
    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    @Override
    public void setDownloader(IDownloader downloader) {
        this.downloader = downloader;
    }

    @Override
    public Double getProgressPercent() {
        return progressPercent;
    }

    @Override
    public String getProgressText() {
        return progressText;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public List<IPubData> getPublications() {
        return publications;
    }

    @Override
    public void run() {
        //throw new UnsupportedOperationException("Not supported yet.");
        System.out.println("PubSearch 1.x Crawler is under development.");
    }
}
