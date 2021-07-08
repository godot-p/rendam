package com.gmail.fdhdcd.renda;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.gmail.fdhdcd.renda.addresult.AddResultController;
import com.gmail.fdhdcd.renda.util.ErrorAlertGenerator;
import com.gmail.fdhdcd.renda.util.Serializer;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Controller {

    @FXML
    private Button startButton;
    @FXML
    private Button resetButton;
    @FXML
    private CheckBox left;
    @FXML
    private CheckBox right;
    @FXML
    private Label clicksResult;
    @FXML
    private Label cpsResult;
    @FXML
    private Label clicksLabel;
    @FXML
    private Label cpsLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private ListView<Integer> timeListView;
    @FXML
    private MenuBar menuBar;
    @FXML
    private Menu menuFile;
    @FXML
    private MenuItem menuClose;
    @FXML
    private MenuItem menuTimeChange;
    @FXML
    private Menu menuRanking;
    @FXML
    private MenuItem rankingShow;
    @FXML
    private MenuItem rankingInput;
    @FXML
    private MenuItem rankingOutput;
    @FXML
    private CheckMenuItem synchronize;
    @FXML
    private MenuItem DBSettings;

    private Counter counter;
    private long time;
    private TimeUnit timeUnit;
    private boolean isRankingEnable;
    private boolean isSynced;
    private static final String SER_FILES = "直列化ファイル (*.ser)";
    private static final String ALL_FILES = "すべてのファイル (*.*)";

    @FXML
    public void initialize() {
        ObservableList<Integer> o = FXCollections.observableList(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        timeListView.setItems(o);
        timeListView.getSelectionModel().selectedItemProperty().addListener((oList, oldValue, newValue) -> {
            if (newValue != null) {
                time = newValue;
                timeUnit = TimeUnit.SECONDS;
            }
        });
        timeListView.getSelectionModel().select(0);
        left.setSelected(true);
        isSynced = synchronize.isSelected();
    }

    public void onStartClicked(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY && left.isSelected()) {
            initOrInc();
        } else if (event.getButton() == MouseButton.SECONDARY && right.isSelected()) {
            initOrInc();
        }
    }

    public void initOrInc() {
        if (counter == null) {
            startButton.setText("クリック");
            counter = new Counter(time, timeUnit);
            resetButton.setDisable(true);
            timeListView.setDisable(true);
            if (timeListView.getSelectionModel().getSelectedIndex() == -1) {
                isRankingEnable = false;
            } else {
                isRankingEnable = true;
            }
        }
        this.counter.inc();
    }

    public void onResetClicked() {
        clicksResult.setText("");
        cpsResult.setText("");
        startButton.setDisable(false);
    }

    public void onTimeChangeClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("timeChange/Layout.fxml"));
            AnchorPane root = (AnchorPane)loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initOwner(Main.stage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setTime(long time) {
        timeListView.getSelectionModel().clearSelection();
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    @SuppressWarnings("static-method")
    public void onCloseClicked() {
        Platform.exit();
    }

    public void onRankingClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("rankingviewer/Layout.fxml"));
            AnchorPane root = (AnchorPane)loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("static-method")
    public void onRankingInputClicked() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(SER_FILES, "*.ser"), new FileChooser.ExtensionFilter(ALL_FILES, "*.*"));
        List<File> files = fileChooser.showOpenMultipleDialog(Main.stage);
        if (files == null) {
            return;
        }
        for (File file : files) {
            try {
                Main.oList.addAll(Serializer.deserialize(file));
            } catch (FileNotFoundException e) {
                Alert a = ErrorAlertGenerator.generate(e);
                a.setHeaderText("ファイルが見つかりません: \"" + file.toString() +"\"");
                a.showAndWait();
            } catch (ClassNotFoundException | IllegalArgumentException e) {
                Alert a = ErrorAlertGenerator.generate(e);
                a.setHeaderText("指定されたファイルがランキングファイルではありません: \"" + file.toString() +"\"");
                a.showAndWait();
            } catch (java.io.StreamCorruptedException | java.io.OptionalDataException e) {
                Alert a = ErrorAlertGenerator.generate(e);
                a.setHeaderText("指定されたファイルがランキングファイルでないか、壊れています: \"" + file.toString() +"\"");
                a.showAndWait();
            } catch (IOException e) {
                Alert a = ErrorAlertGenerator.generate(e);
                a.setHeaderText("入出力エラーが発生しました: \"" + file.toString() +"\"");
                a.showAndWait();
            } catch (Exception e) {
                Alert a = ErrorAlertGenerator.generate(e);
                a.setHeaderText("不明なエラーが発生しました: \"" + file.toString() +"\"");
                a.showAndWait();
            }
        }
    }

    @SuppressWarnings("static-method")
    public void onRankingOutputClicked() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(SER_FILES, "*.ser"), new FileChooser.ExtensionFilter(ALL_FILES, "*.*"));
        File file = fileChooser.showSaveDialog(Main.stage);
        if (file == null) {
            return;
        }
        try {
            Serializer.serialize(file, Main.resultList);
        } catch (FileNotFoundException e) {
            Alert a = ErrorAlertGenerator.generate(e);
            a.setHeaderText("ファイルが見つかりません: \"" + file.toString() +"\"");
            a.showAndWait();
        } catch (IOException e) {
            Alert a = ErrorAlertGenerator.generate(e);
            a.setHeaderText("入出力エラーが発生しました: \"" + file.toString() +"\"");
            a.showAndWait();
        }
    }

    private void openAddResultWindow(int clicks, int seconds) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addresult/Layout.fxml"));
            AnchorPane root = (AnchorPane)loader.load();
            AddResultController controller = loader.getController();
            controller.setClicks(clicks);
            controller.setSeconds(seconds);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initOwner(Main.stage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onSynchronizeClicked() {
        isSynced = synchronize.isSelected();
    }

    public boolean isSynced() {
        return isSynced;
    }

    public void onDBSettingsClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dbsettings/Layout.fxml"));
            AnchorPane root = (AnchorPane)loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initOwner(Main.stage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setMinHeight(185 + 30);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class Counter {

        private int count;

        private void inc() {
            count++;
            clicksResult.setText(Integer.toString(count));
        }

        private Counter(long time, TimeUnit timeUnit) {
            Executors.newScheduledThreadPool(1, r -> {
                Thread t = new Thread(r);
                t.setDaemon(true);
                return t;
            }).schedule(() -> {
                Platform.runLater(() -> {
                    Alert a;
                    if (isRankingEnable) {
                        a = new Alert(AlertType.CONFIRMATION, "結果をランキングに追加しますか?");
                        ObservableList<ButtonType> buttonList = a.getButtonTypes();
                        buttonList.clear();
                        buttonList.add(ButtonType.YES);
                        buttonList.add(ButtonType.NO);
                    } else {
                        a = new Alert(AlertType.INFORMATION, null);
                    }
                    a.setHeaderText("計測終了");
                    a.setResizable(false);
                    a.initOwner(Main.stage);
                    a.initModality(Modality.WINDOW_MODAL);
                    double seconds = time;
                    switch (timeUnit) {
//                        case DAYS:
//                            seconds = time * 24 * 60 * 60;
//                            break;
//                        case HOURS:
//                            seconds = time * 60 * 60;
//                            break;
//                        case MINUTES:
//                            seconds = time * 60;
//                            break;
//                        case SECONDS:
//                            seconds = time;
//                            break;
//                        case MILLISECONDS:
//                            seconds = time * 1d / 1000;
//                            break;
//                        case MICROSECONDS:
//                            seconds = time * 1d / 1000 / 1000;
//                            break;
//                        case NANOSECONDS:
//                            seconds = time * 1d / 1000 / 1000 / 1000;
//                            break;
//                        default:
//                            throw new InternalError();
                        case NANOSECONDS:
                            seconds /= 1000;
                        case MICROSECONDS:
                            seconds /= 1000;
                        case MILLISECONDS:
                            seconds /= 1000;
                            break;
                        case DAYS:
                            seconds *= 24;
                        case HOURS:
                            seconds *= 60;
                        case MINUTES:
                            seconds *= 60;
                        case SECONDS:
                            break;
                    }
                    cpsResult.setText(Double.toString(count / seconds));
                    startButton.setDisable(true);
                    a.showAndWait().filter(b -> b == ButtonType.YES).ifPresent(b -> openAddResultWindow(count, timeListView.getSelectionModel().getSelectedItem()));
                    counter = null;
                    startButton.setText("スタート");
                    resetButton.setDisable(false);
                    timeListView.setDisable(false);
                });
            }, time, timeUnit);
        }
    }

}
