/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.juranyi.zsolt.pubsearch2;

import hu.juranyi.zsolt.pubsearch2.core.CrawlerHandler;
import hu.juranyi.zsolt.pubsearch2.gui.MainController;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Zsolt
 */
public class PubSearch2 extends Application {

    public static final String VERSION = "2.0.0";

    @Override
    public void start(Stage stage) throws Exception {
        initPubSearch2();
        if (getParameters().getRaw().isEmpty()) {
            Parent root = FXMLLoader.load(MainController.class.getResource("Main.fxml"), ResourceBundle.getBundle("hu.juranyi.zsolt.pubsearch2.gui.Main"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("PubSearch 2 by Zsolt Jurányi");
            stage.centerOnScreen();
            stage.onCloseRequestProperty().setValue(new EventHandler<WindowEvent>() {

                @Override
                public void handle(WindowEvent t) {
                    Platform.exit();
                    System.exit(0);
                }
            });
            stage.show();
        } else {
            PubSearch2CLI.main(getParameters().getRaw());
            Platform.exit();
        }
    }

    private void initPubSearch2() {
        CrawlerHandler.lookupCrawlers();
        CrawlerHandler.loadCrawlerConfiguration();
        
        // TODO load config
        // TODO Database.connect(<config>)
        
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
