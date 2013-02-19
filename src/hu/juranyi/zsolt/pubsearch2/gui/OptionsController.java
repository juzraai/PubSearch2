package hu.juranyi.zsolt.pubsearch2.gui;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class OptionsController
        implements Initializable {

    @FXML //  fx:id="addCrawlerClass"
    private Button addCrawlerClass; // Value injected by FXMLLoader

    @FXML //  fx:id="checkListContainer"
    private VBox checkListContainer; // Value injected by FXMLLoader

    @FXML //  fx:id="newCrawlerClassName"
    private TextField newCrawlerClassName; // Value injected by FXMLLoader

    @FXML //  fx:id="newCrawlerJarName"
    private TextField newCrawlerJarName; // Value injected by FXMLLoader


    // Handler for Button[fx:id="addCrawlerClass"] onAction
    public void addCrawlerClass(ActionEvent event) {
        // handle the event here
    }

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert addCrawlerClass != null : "fx:id=\"addCrawlerClass\" was not injected: check your FXML file 'Options.fxml'.";
        assert checkListContainer != null : "fx:id=\"checkListContainer\" was not injected: check your FXML file 'Options.fxml'.";
        assert newCrawlerClassName != null : "fx:id=\"newCrawlerClassName\" was not injected: check your FXML file 'Options.fxml'.";
        assert newCrawlerJarName != null : "fx:id=\"newCrawlerJarName\" was not injected: check your FXML file 'Options.fxml'.";

        // initialize your logic here: all @FXML variables will have been injected

    }
}