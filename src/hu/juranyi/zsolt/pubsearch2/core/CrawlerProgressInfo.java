package hu.juranyi.zsolt.pubsearch2.core;

/**
 *
 * @author Zsolt
 */
public class CrawlerProgressInfo {

    private String crawlerName;
    private String progressText;
    private String progressPercent;

    public CrawlerProgressInfo(Crawler crawler) {
        crawlerName = crawler.getInstance().getCrawlerName();

        if (crawler.isRunning()) {
            progressText = crawler.getInstance().getProgressText();
        } else {
            progressText = crawler.getInstance().getErrorMessage();
        }
        progressText = (null == progressText) ? "" : progressText;

        if (null == crawler.getInstance().getProgressPercent()) {
            progressPercent = "";
        } else {
            progressPercent = String.format("%.1f", (crawler.getInstance().getProgressPercent()) * 100.0) + " %";
        }
    }

    public String getCrawlerName() {
        return crawlerName;
    }

    public String getProgressText() {
        return progressText;
    }

    public String getProgressPercent() {
        return progressPercent;
    }
}
