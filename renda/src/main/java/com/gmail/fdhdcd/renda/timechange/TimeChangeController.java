package com.gmail.fdhdcd.renda.timechange;

import java.util.concurrent.TimeUnit;

import com.gmail.fdhdcd.renda.Main;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Window;

public class TimeChangeController {

    @FXML
    private TextField timeField;
    @FXML
    private Label typeTime;
    @FXML
    private ChoiceBox<String> choiceTimeUnit;

    @FXML
    public void initialize() {
        timeField.setText(Long.toString(Main.firstController.getTime()));
        choiceTimeUnit.setItems(FXCollections.observableArrayList("秒", "ミリ秒", "マイクロ秒", "ナノ秒"));
        String timeUnitStr;
        switch (Main.firstController.getTimeUnit()) {
            case DAYS:
                timeUnitStr = "Days";
                break;
            case HOURS:
                timeUnitStr = "Hours";
                break;
            case MINUTES:
                timeUnitStr = "Minutes";
                break;
            case SECONDS:
                timeUnitStr = "秒";
                break;
            case MILLISECONDS:
                timeUnitStr = "ミリ秒";
                break;
            case MICROSECONDS:
                timeUnitStr = "マイクロ秒";
                break;
            case NANOSECONDS:
                timeUnitStr = "ナノ秒";
                break;
            default:
                throw new InternalError();
        }
        choiceTimeUnit.setValue(timeUnitStr);
    }

    public void onOKClicked(ActionEvent event) {
        String text = timeField.getText();
        if (!text.matches("[0-9]+")) {
            Alert a = new Alert(AlertType.WARNING, "整数値を入力してください");
            a.setTitle(null);
            a.setHeaderText(null);
            a.showAndWait();
            return;
        }
        switch (this.choiceTimeUnit.getValue()) {
            case "秒":
                Main.firstController.setTimeUnit(TimeUnit.SECONDS);
                break;
            case "ミリ秒":
                Main.firstController.setTimeUnit(TimeUnit.MILLISECONDS);
                break;
            case "マイクロ秒":
                Main.firstController.setTimeUnit(TimeUnit.MICROSECONDS);
                break;
            case "ナノ秒":
                Main.firstController.setTimeUnit(TimeUnit.NANOSECONDS);
                break;
        }
        Main.firstController.setTime(Long.parseLong(text));
        closeWindow(event);
    }

    public void onCancelClicked(ActionEvent event) {
        closeWindow(event);
    }

    @SuppressWarnings("static-method")
    private void closeWindow(ActionEvent event) {
        Scene scene = ((Node)event.getSource()).getScene();
        Window window = scene.getWindow();
        window.hide();
    }

}
