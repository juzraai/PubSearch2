package hu.juranyi.zsolt.pubsearch2.core;

import hu.juranyi.zsolt.pubsearch2.interfaces.IPubDataCrawler;

/**
 *
 * @author Zsolt
 */
public class Crawler {

    // core
    private IPubDataCrawler instance;
    private boolean running = false;
    // loader
    private String jarFile;
    private String crawlerClass;
    // config
    private Boolean enabled;
    private Boolean useProxy;

    public Crawler(IPubDataCrawler instance, String jarFile, String crawlerClass, Boolean enabled, Boolean useProxy) {
        this.instance = instance;
        this.jarFile = jarFile;
        this.crawlerClass = crawlerClass;
        this.enabled = enabled;
        this.useProxy = useProxy;
    }

    public IPubDataCrawler getInstance() {
        return instance;
    }

    public void setInstance(IPubDataCrawler instance) {
        this.instance = instance;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public String getJarFile() {
        return jarFile;
    }

    public void setJarFile(String jarFile) {
        this.jarFile = jarFile;
    }

    public String getCrawlerClass() {
        return crawlerClass;
    }

    public void setCrawlerClass(String crawlerClass) {
        this.crawlerClass = crawlerClass;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getUseProxy() {
        return useProxy;
    }

    public void setUseProxy(Boolean useProxy) {
        this.useProxy = useProxy;
    }
}
