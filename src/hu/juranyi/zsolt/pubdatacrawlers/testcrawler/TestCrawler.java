package hu.juranyi.zsolt.pubdatacrawlers.testcrawler;

import hu.juranyi.zsolt.pubsearch2.interfaces.IDownloader;
import hu.juranyi.zsolt.pubsearch2.interfaces.IPubData;
import hu.juranyi.zsolt.pubsearch2.interfaces.IPubDataCrawler;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Zsolt
 */
public class TestCrawler implements IPubDataCrawler {

    // meta
    private final String crawlerName = "Test Crawler";
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
        System.out.println("Test crawler: started");
        int i = 100;
        while (i > 0) {
            i--;
            progressText = Integer.toString(i);
            progressPercent = (100 - i) / 100.0;
            System.out.println("Test crawler: " + progressPercent + "%");
            try {
                Thread.sleep(150);
            } catch (InterruptedException ex) {
                errorMessage = "Interrupted.";
                break;
            }
        }
        progressPercent = 1;
        System.out.println("Test crawler: done");
    }
}
