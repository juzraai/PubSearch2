package hu.juranyi.zsolt.pubsearch2.core;

import hu.juranyi.zsolt.pubsearch2.interfaces.IPubData;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 *
 * @author Zsolt
 */
public class CrawlerTask implements Callable<List<IPubData>> {

    private Crawler crawler;

    public CrawlerTask(Crawler crawler) {
        this.crawler = crawler;
    }

    @Override
    public List<IPubData> call() throws Exception {
        crawler.setRunning(true);
        crawler.getInstance().run();
        crawler.setRunning(false);
        return (null == crawler.getInstance().getPublications()) ? new ArrayList<IPubData>() : crawler.getInstance().getPublications();
    }
}
