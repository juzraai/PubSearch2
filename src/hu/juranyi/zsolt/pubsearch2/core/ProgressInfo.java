package hu.juranyi.zsolt.pubsearch2.core;

/**
 *
 * @author Zsolt
 */
public class ProgressInfo {

    private String crawlerName;
    private String progressText;
    private Double progressPercent;

    public ProgressInfo(Crawler crawler) {
        crawlerName = crawler.getInstance().getCrawlerName();

        if (crawler.isRunning()) {
            progressText = crawler.getInstance().getProgressText();
        } else {
            progressText = crawler.getInstance().getErrorMessage();
        }
        progressText = (null == progressText) ? "" : progressText;

        progressPercent = crawler.getInstance().getProgressPercent();
    }

    public String getCrawlerName() {
        return crawlerName;
    }

    public String getProgressText() {
        return progressText;
    }

    public Double getProgressPercent() {
        return progressPercent;
    }
}
