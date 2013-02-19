package hu.juranyi.zsolt.pubsearch2.core;

import hu.juranyi.zsolt.pubsearch2.interfaces.IPubData;
import hu.juranyi.zsolt.pubsearch2.network.Downloader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * @author Zsolt
 */
public class CrawlerRunner { // TODO ezeket akár át is rakhatnám a CrawlerHandler-be

    public static List<IPubData> runCrawlers(String author, String title, int transLev, int crawlerThreads, int insideThreads) {

        // build crawler tasks
        List<CrawlerTask> crawlerTasks = new ArrayList<CrawlerTask>();
        for (Crawler crawler : CrawlerHandler.getCrawlers()) {
            if (crawler.getEnabled()) {

                // initialize crawler
                crawler.getInstance().setAuthor(author);
                crawler.getInstance().setTitle(title);
                crawler.getInstance().setThreadCount(insideThreads);
                crawler.getInstance().setTransitivityLevel(transLev);
                if (!crawler.getInstance().hasOwnDownloader()) {
                    crawler.getInstance().setDownloader(new Downloader());
                }

                // build crawler task
                crawlerTasks.add(new CrawlerTask(crawler));
            }
        }

        // execute crawlers in thread pool
        ExecutorService threadPool = Executors.newFixedThreadPool(crawlerThreads);
        List<Future<List<IPubData>>> executionResults = null;
        try {
            executionResults = threadPool.invokeAll(crawlerTasks);
        } catch (InterruptedException ex) {
        }

        // gather crawled publications
        List<IPubData> results = new ArrayList<IPubData>();
        for (Future<List<IPubData>> executionResult : executionResults) {
            try {
                results.addAll(executionResult.get());
            } catch (InterruptedException | ExecutionException ex) {
            }
        }
        return results;
    }

    public static List<CrawlerProgressInfo> getCrawlerProgressInfos() {
        List<CrawlerProgressInfo> crawlerProgressInfos = new ArrayList<CrawlerProgressInfo>();
        for (Crawler crawler : CrawlerHandler.getCrawlers()) {
            if (crawler.getEnabled()) {
                crawlerProgressInfos.add(new CrawlerProgressInfo(crawler));
            }
        }
        return crawlerProgressInfos;
    }

    public static double getOverallProgressPercent() {
        double sum = 0.0;
        int count = 0;
        for (Crawler crawler : CrawlerHandler.getCrawlers()) {
            if (crawler.getEnabled()) {
                if (!crawler.isRunning()) {
                    sum += 1.0;
                } else {
                    Double current = crawler.getInstance().getProgressPercent();
                    sum += (null == current) ? 0.0 : current;
                }
                count++;
            }
        }
        return sum / count;
    }
}
