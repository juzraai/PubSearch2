/**
 * Sample Skeleton for "Main.fxml" Controller Class You can copy and paste this
 * code into your favorite IDE
 *
 */
package hu.juranyi.zsolt.pubsearch2.gui;

import hu.juranyi.zsolt.pubsearch2.PubSearch2;
import hu.juranyi.zsolt.pubsearch2.core.CrawlerHandler;
import hu.juranyi.zsolt.pubsearch2.core.ProgressInfo;
import hu.juranyi.zsolt.pubsearch2.core.ProgressUpdater;
import hu.juranyi.zsolt.pubsearch2.interfaces.IPubData;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ProgressBarTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;

public class MainController
        implements Initializable {

    @FXML //  fx:id="about"
    private MenuItem about; // Value injected by FXMLLoader
    @FXML //  fx:id="authorField"
    private TextField authorField; // Value injected by FXMLLoader
    @FXML //  fx:id="browseTab"
    private Tab browseTab; // Value injected by FXMLLoader
    @FXML //  fx:id="browseTable"
    private TableView<?> browseTable; // Value injected by FXMLLoader
    @FXML //  fx:id="crawlerThreadsSlider"
    private Slider crawlerThreadsSlider; // Value injected by FXMLLoader
    @FXML //  fx:id="insideThreadsSlider"
    private Slider insideThreadsSlider; // Value injected by FXMLLoader
    @FXML //  fx:id="progressBar"
    private ProgressBar progressBar; // Value injected by FXMLLoader
    @FXML //  fx:id="progressCrawlerColumn"
    private TableColumn<ProgressInfo, String> progressCrawlerColumn; // Value injected by FXMLLoader
    @FXML //  fx:id="progressPercentColumn"
    private TableColumn progressPercentColumn; // Value injected by FXMLLoader
    @FXML //  fx:id="progressTab"
    private Tab progressTab; // Value injected by FXMLLoader
    @FXML //  fx:id="progressTable"
    private TableView<ProgressInfo> progressTable; // Value injected by FXMLLoader
    @FXML //  fx:id="progressTextColumn"
    private TableColumn<ProgressInfo, String> progressTextColumn; // Value injected by FXMLLoader
    @FXML //  fx:id="queryButton"
    private Button queryButton; // Value injected by FXMLLoader
    @FXML //  fx:id="resultsPieChart"
    private PieChart resultsPieChart; // Value injected by FXMLLoader
    @FXML //  fx:id="resultsTab"
    private Tab resultsTab; // Value injected by FXMLLoader
    @FXML //  fx:id="resultsTable"
    private TableView<IPubData> resultsTable; // Value injected by FXMLLoader
    @FXML //  fx:id="resultsText"
    private Label resultsText; // Value injected by FXMLLoader
    @FXML //  fx:id="searchTab"
    private Tab searchTab; // Value injected by FXMLLoader
    @FXML //  fx:id="startButton"
    private Button startButton; // Value injected by FXMLLoader
    private Tab statsTab; // Value injected by FXMLLoader
    @FXML //  fx:id="tabs"
    private TabPane tabs; // Value injected by FXMLLoader
    @FXML //  fx:id="titleField"
    private TextField titleField; // Value injected by FXMLLoader
    @FXML //  fx:id="transLevCombo"
    private ComboBox<?> transLevCombo; // Value injected by FXMLLoader
    @FXML
    private Font x1;
    @FXML
    private Tab resultsSubTab;
    @FXML
    private Tab statsSubTab;

    // Handler for MenuItem[fx:id="about"] onAction
    @FXML
    public void about(ActionEvent event) {
        // handle the event here
    }

    // Handler for Button[fx:id="queryButton"] onAction
    @FXML
    public void query(ActionEvent event) {
        // handle the event here
    }

    // Handler for TextField[fx:id="authorField"] onKeyPressed
    // Handler for TextField[fx:id="titleField"] onKeyPressed
    @FXML
    public void refreshFormControls(KeyEvent event) {
        titleField.setDisable(authorField.getText().length() == 0);
        startButton.setDisable(authorField.getText().length() == 0 || CrawlerHandler.getEnabledCount() == 0);
    }

    // Handler for MenuItem[javafx.scene.control.MenuItem@18debd3] onAction
    @FXML
    public void settings(ActionEvent event) {
        // handle the event here
    }

    // Handler for Button[fx:id="startButton"] onAction
    @FXML
    public void start(ActionEvent event) { // TODO kitalálni a
        //tabs.getTabs().add(progressTab);
        tabs.getSelectionModel().select(progressTab);
        /*searchTab.disableProperty().set(true);
         resultsTab.disableProperty().set(true);
         statsTab.disableProperty().set(true);*/
        tabs.getTabs().removeAll(searchTab, resultsTab, statsTab);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                updateProgress();
                ProgressUpdater progressUpdater = new ProgressUpdater(MainController.this);
                progressUpdater.start();

                List<IPubData> results = CrawlerHandler.runCrawlers(authorField.getText(), titleField.getText(), transLevCombo.selectionModelProperty().get().getSelectedIndex(), (int) crawlerThreadsSlider.getValue(), (int) insideThreadsSlider.getValue());

                progressUpdater.interrupt();
                updateProgress();

                resultsTable.setItems(FXCollections.observableArrayList(results));
                //TODO stats data pass
                /*resultsTab.disableProperty().set(false);
                 searchTab.disableProperty().set(false);
                 statsTab.disableProperty().set(false);*/
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        // TODO add and select result tab
                        //tabs.getTabs().addAll(searchTab, resultsTab, statsTab);
                        //tabs.getSelectionModel().select(resultsTab);
                    }
                });


            }
        });
        t.setName("Crawler runner");
        t.start();
    }

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert about != null : "fx:id=\"about\" was not injected: check your FXML file 'Main.fxml'.";
        assert authorField != null : "fx:id=\"authorField\" was not injected: check your FXML file 'Main.fxml'.";
        assert browseTab != null : "fx:id=\"browseTab\" was not injected: check your FXML file 'Main.fxml'.";
        assert browseTable != null : "fx:id=\"browseTable\" was not injected: check your FXML file 'Main.fxml'.";
        assert crawlerThreadsSlider != null : "fx:id=\"crawlerThreadsSlider\" was not injected: check your FXML file 'Main.fxml'.";
        assert insideThreadsSlider != null : "fx:id=\"insideThreadsSlider\" was not injected: check your FXML file 'Main.fxml'.";
        assert progressBar != null : "fx:id=\"progressBar\" was not injected: check your FXML file 'Main.fxml'.";
        assert progressCrawlerColumn != null : "fx:id=\"progressCrawlerColumn\" was not injected: check your FXML file 'Main.fxml'.";
        assert progressPercentColumn != null : "fx:id=\"progressPercentColumn\" was not injected: check your FXML file 'Main.fxml'.";
        assert progressTab != null : "fx:id=\"progressTab\" was not injected: check your FXML file 'Main.fxml'.";
        assert progressTable != null : "fx:id=\"progressTable\" was not injected: check your FXML file 'Main.fxml'.";
        assert progressTextColumn != null : "fx:id=\"progressTextColumn\" was not injected: check your FXML file 'Main.fxml'.";
        assert queryButton != null : "fx:id=\"queryButton\" was not injected: check your FXML file 'Main.fxml'.";
        assert resultsPieChart != null : "fx:id=\"resultsPieChart\" was not injected: check your FXML file 'Main.fxml'.";
        assert resultsTab != null : "fx:id=\"resultsTab\" was not injected: check your FXML file 'Main.fxml'.";
        assert resultsTable != null : "fx:id=\"resultsTable\" was not injected: check your FXML file 'Main.fxml'.";
        assert resultsText != null : "fx:id=\"resultsText\" was not injected: check your FXML file 'Main.fxml'.";
        assert searchTab != null : "fx:id=\"searchTab\" was not injected: check your FXML file 'Main.fxml'.";
        assert startButton != null : "fx:id=\"startButton\" was not injected: check your FXML file 'Main.fxml'.";
        assert statsTab != null : "fx:id=\"statsTab\" was not injected: check your FXML file 'Main.fxml'.";
        assert tabs != null : "fx:id=\"tabs\" was not injected: check your FXML file 'Main.fxml'.";
        assert titleField != null : "fx:id=\"titleField\" was not injected: check your FXML file 'Main.fxml'.";
        assert transLevCombo != null : "fx:id=\"transLevCombo\" was not injected: check your FXML file 'Main.fxml'.";

        // initialize your logic here: all @FXML variables will have been injected
        tabs.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
                PubSearch2.window.setTitle("PubSearch 2 - " + t1.getText());
            }
        });


        //tabs.getTabs().removeAll(...); // TODO remove not needed tabs


        progressCrawlerColumn.setCellValueFactory(new PropertyValueFactory<ProgressInfo, String>("crawlerName"));
        progressTextColumn.setCellValueFactory(new PropertyValueFactory<ProgressInfo, String>("progressText"));
        progressPercentColumn.setCellValueFactory(new PropertyValueFactory<ProgressInfo, Double>("progressPercent"));
        progressPercentColumn.setCellFactory(ProgressBarTableCell.forTableColumn());
    }

    public void updateProgress() {
        ObservableList<ProgressInfo> crawlerList = FXCollections.observableArrayList(CrawlerHandler.getProgressInfos());
        progressTable.setItems(crawlerList);
        progressBar.progressProperty().set(CrawlerHandler.getOverallProgressPercent());
    }
}
