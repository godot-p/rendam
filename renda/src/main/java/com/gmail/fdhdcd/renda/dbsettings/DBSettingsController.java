package com.gmail.fdhdcd.renda.dbsettings;

import com.gmail.fdhdcd.renda.util.ResultDAO2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Window;

public class DBSettingsController {

    @FXML
    private TextField address;
    @FXML
    private TextField user;
    @FXML
    private TextField password;
    @FXML
    private TextField database;
    @FXML
    private Button okButton;
    @FXML
    private Button cancelButton;

    @FXML
    public void initialize() {
        ResultDAO2 dao = ResultDAO2.getInstance();
        address.setText(dao.getAddress());
        user.setText(dao.getUser());
        password.setText(dao.getPassword());
        database.setText(dao.getDatabase());
    }

    public void onOKClicked(ActionEvent event) {
        ResultDAO2 dao = ResultDAO2.getInstance();
        dao.setAddress(address.getText());
        dao.setUser(user.getText());
        dao.setPassword(password.getText());
        dao.setDatabase(database.getText());
        Scene scene = ((Node)event.getSource()).getScene();
        Window window = scene.getWindow();
        window.hide();
    }

    @SuppressWarnings("static-method")
    public void onCancelClicked(ActionEvent event) {
        Scene scene = ((Node)event.getSource()).getScene();
        Window window = scene.getWindow();
        window.hide();
    }

}
