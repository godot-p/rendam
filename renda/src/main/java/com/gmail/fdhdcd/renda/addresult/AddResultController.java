package com.gmail.fdhdcd.renda.addresult;

import java.sql.SQLException;

import com.gmail.fdhdcd.renda.Main;
import com.gmail.fdhdcd.renda.util.ErrorAlertGenerator;
import com.gmail.fdhdcd.renda.util.ResultDAO2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Window;

public class AddResultController {

    @FXML
    private Label label;
    @FXML
    private TextField textField;
    @FXML
    private Button button;

    private int clicks;
    private int seconds;
    private static final String DEFAULT_NAME = "名無し";

    @FXML
    public void initialize() {
        label.setText("名前を入力してください");
        button.setText("追加");
        textField.requestFocus();
    }

    @FXML
    public void onButtonClicked(ActionEvent event) {
        String editedText = textField.getText().strip();
        Result result = new Result(editedText.equals("") ? DEFAULT_NAME : editedText, clicks, seconds);
        if (Main.firstController.isSynced()) {
            try {
                ResultDAO2.getInstance().sendToDB(result);
            } catch (SQLException e) {
                Alert a = ErrorAlertGenerator.generate(e);
                a.setHeaderText("");
                a.showAndWait();
            }
        } else {
            Main.oList.add(result);
        }
        Scene scene = ((Node)event.getSource()).getScene();
        Window window = scene.getWindow();
        window.hide();
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

}
