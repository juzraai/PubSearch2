package hu.juranyi.zsolt.pubsearch2.core;

import hu.juranyi.zsolt.pubsearch2.gui.MainController;

/**
 *
 * @author Zsolt
 */
public class ProgressUpdater extends Thread {

    private final MainController main;

    public ProgressUpdater(MainController main) {
        this.main = main;
        setName("Progress updater");
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(250);
                main.updateProgress();
            } catch (InterruptedException ex) {
                break;
            }
        }
    }
}
