package hu.juranyi.zsolt.pubsearch2.core;

import hu.juranyi.zsolt.pubdatacrawlers.pubsearch1x.PubSearch1xCrawler;
import hu.juranyi.zsolt.pubdatacrawlers.testcrawler.TestCrawler;
import hu.juranyi.zsolt.pubsearch2.interfaces.IPubData;
import hu.juranyi.zsolt.pubsearch2.interfaces.IPubDataCrawler;
import hu.juranyi.zsolt.pubsearch2.network.Downloader;
import hu.juranyi.zsolt.util.TextFile;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Scans for crawler libraries in "crawlers" directory, loads and stores the
 * crawler classes, handles their configuration, and runs them providing
 * progress information.
 *
 * @author Zsolt Jurányi
 */
public class CrawlerHandler {

    private static final String CRAWLERS_DIRECTORY = "./crawlers/";
    private static final String CRAWLERS_CONFIG_FILE = CRAWLERS_DIRECTORY + "crawlers.cfg";
    private static final List<Crawler> crawlers = new ArrayList<Crawler>();

    private static class CrawlerTask implements Callable<List<IPubData>> {

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

    public static void lookupCrawlers() {
        System.out.print("Scanning for crawlers... ");
        crawlers.clear();

        // scan jars for IPubDataCrawler implementations
        File crawlersDir = new File(CRAWLERS_DIRECTORY);
        if (null != crawlersDir.list()) {
            for (String fileName : crawlersDir.list()) {
                if (fileName.endsWith(".jar")) {
                    try {
                        JarFile jar = new JarFile(CRAWLERS_DIRECTORY + fileName);
                        Enumeration e = jar.entries();
                        while (e.hasMoreElements()) {
                            JarEntry entry = (JarEntry) e.nextElement();
                            if (entry.getName().endsWith(".class")) {
                                String className = entry.getName().replaceFirst("\\.class", "").replaceAll("/", ".");
                                IPubDataCrawler crawler = loadCrawler(CRAWLERS_DIRECTORY + fileName, className);
                                if (null != crawler) {
                                    // verify crawler
                                    if (crawler.getCrawlerName() != null) {
                                        crawlers.add(new Crawler(crawler, CRAWLERS_DIRECTORY + fileName, className, false, false));
                                    }
                                }
                            } // class files
                        } // jar entries
                    } catch (IOException ex) {
                    } // jar read errors
                } // jar files
            } // files
        }

        // add embedded PubSearch 1.x Crawler
        crawlers.add(new Crawler(new PubSearch1xCrawler(), null, PubSearch1xCrawler.class.getName(), false, false));

        // TODO TestCrawler inject, kivenni
        crawlers.add(new Crawler(new TestCrawler(), null, TestCrawler.class.getName(), true, false));
        crawlers.add(new Crawler(new TestCrawler(200), null, TestCrawler.class.getName(), true, false));

        // output loaded crawlers
        Collections.sort(crawlers, new Comparator<Crawler>() {
            @Override
            public int compare(Crawler o1, Crawler o2) {
                String s1 = o1.getInstance().getCrawlerName();
                String s2 = o2.getInstance().getCrawlerName();
                return s1.compareTo(s2);
            }
        });

        if (crawlers.isEmpty()) {
            System.out.println("no crawlers found.");
        } else {
            System.out.println(crawlers.size() + " crawlers loaded:");
            for (Crawler c : crawlers) {
                System.out.println("\t" + c.getInstance().getCrawlerName());
            }
        }
    }

    private static IPubDataCrawler loadCrawler(String jarFileName, String className) {
        try {
            URL jarURL = new File(jarFileName).toURI().toURL();
            URLClassLoader classLoader = new URLClassLoader(new URL[]{jarURL}, IPubDataCrawler.class.getClassLoader());
            Class<? extends IPubDataCrawler> crawlerClass = (Class<? extends IPubDataCrawler>) Class.forName(className, true, classLoader);
            IPubDataCrawler crawler = crawlerClass.newInstance();
            return crawler;
        } catch (MalformedURLException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            return null;
        }
    }

    public static void loadCrawlerConfiguration() {
        System.out.println("Loading crawler configuration... ");
        TextFile f = new TextFile(CRAWLERS_CONFIG_FILE);
        for (String line : f.getLines()) {
            List<String> parts = Arrays.asList(line.split("\t"));
            if (!parts.isEmpty()) {
                String crawlerClass = parts.get(0);
                boolean enabled = parts.contains("enabled");
                boolean useProxy = parts.contains("useProxy");
                for (Crawler crawler : crawlers) {
                    if (crawler.getCrawlerClass().equals(crawlerClass)) {
                        crawler.setEnabled(enabled);
                        crawler.setUseProxy(useProxy);
                        break;
                    }
                } // crawlers
            } // valid lines
        } // lines
    }

    public static boolean saveCrawlerConfiguration() {
        System.out.println("Saving crawler configuration...");
        TextFile f = new TextFile(CRAWLERS_CONFIG_FILE);
        List<String> lines = f.getLines();
        f.getLines().clear();
        for (Crawler crawler : crawlers) {
            if (crawler.getEnabled() || crawler.getUseProxy()) {
                StringBuilder buf = new StringBuilder(crawler.getCrawlerClass());
                if (crawler.getEnabled()) {
                    buf.append("\tenabled");
                }
                if (crawler.getUseProxy()) {
                    buf.append("\tuseProxy");
                }
                lines.add(buf.toString());
                System.out.println(buf.toString());
            }
        }
        return f.save();
    }

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

    public static List<Crawler> getCrawlers() {
        return crawlers;
    }

    public static int getEnabledCount() {
        int c = 0;
        for (Crawler crawler : getCrawlers()) {
            if (crawler.getEnabled()) {
                c++;
            }
        }
        return c;
    }

    public static List<ProgressInfo> getProgressInfos() {
        List<ProgressInfo> crawlerProgressInfos = new ArrayList<ProgressInfo>();
        for (Crawler crawler : CrawlerHandler.getCrawlers()) {
            if (crawler.getEnabled()) {
                crawlerProgressInfos.add(new ProgressInfo(crawler));
            }
        }
        return crawlerProgressInfos;
    }

    public static double getOverallProgressPercent() {
        double sum = 0.0;
        int count = 0;
        for (Crawler crawler : CrawlerHandler.getCrawlers()) {
            if (crawler.getEnabled()) {
                Double current = crawler.getInstance().getProgressPercent();
                sum += (null == current) ? 0.0 : current;
                count++;
            }
        }
        return sum / count;
    }
}
