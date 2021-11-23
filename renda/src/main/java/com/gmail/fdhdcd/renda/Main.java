package com.gmail.fdhdcd.renda;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gmail.fdhdcd.renda.addresult.Result;
import com.gmail.fdhdcd.renda.util.ResultDAO2;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {

    public static Controller firstController;
    public static final List<Result> resultList = new ArrayList<>();
    public static final ObservableList<Result> oList = FXCollections.observableList(resultList);
    public static Stage stage;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Main.stage = primaryStage;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Layout.fxml"));
            AnchorPane root = (AnchorPane)loader.load();
            Main.firstController = loader.getController();
            primaryStage.setScene(new Scene(root));
            primaryStage.setResizable(false);
            primaryStage.show();
            primaryStage.setOnCloseRequest(req -> {
                try {
                    ResultDAO2.getInstance().closeConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
