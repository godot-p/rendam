package com.gmail.fdhdcd.renda.rankingviewer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gmail.fdhdcd.renda.Main;
import com.gmail.fdhdcd.renda.addresult.Result;
import com.gmail.fdhdcd.renda.util.ErrorAlertGenerator;
import com.gmail.fdhdcd.renda.util.ResultDAO2;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.control.cell.PropertyValueFactory;

public class RankingViewerController {

    @FXML
    private ListView<IntegerItem> listView;
    @FXML
    private TableView<Result> tableView;
    @FXML
    private TableColumn<Result, String> name;
    @FXML
    private TableColumn<Result, String> seconds;
    @FXML
    private TableColumn<Result, String> clicks;
    @FXML
    private TableColumn<Result, String> cps;
    @FXML
    private TableColumn<Result, String> time;

    private ObservableList<Result> listFromDB = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        List<IntegerItem> l = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            IntegerItem item = new IntegerItem(i);
            item.bProperty().addListener((observable, oldVal, newVal) -> {
                System.out.println(String.valueOf(oldVal) + "->" + String.valueOf(newVal));
            });
            l.add(item);
        }
        ObservableList<IntegerItem> o = FXCollections.observableList(l);
        listView.setItems(o);
        listView.setCellFactory(CheckBoxListCell.forListView(i -> i.bProperty()));
        this.name.setCellValueFactory(new PropertyValueFactory<Result, String>("name"));
        this.seconds.setCellValueFactory(new PropertyValueFactory<Result, String>("seconds"));
        this.clicks.setCellValueFactory(new PropertyValueFactory<Result, String>("clicks"));
        this.cps.setCellValueFactory(new PropertyValueFactory<Result, String>("cps"));
        this.time.setCellValueFactory(new PropertyValueFactory<Result, String>("time"));
        if (Main.firstController.isSynced()) {
            loadRankingFromDB();
        } else {
            this.tableView.setItems(Main.oList);
        }
    }

    private void loadRankingFromDB() {
        try {
            listFromDB.clear();
            listFromDB.addAll(ResultDAO2.getInstance().readFromDB());
            tableView.setItems(listFromDB);
        } catch (SQLException e) {
            ErrorAlertGenerator.generate(e);
        }
    }

    private class IntegerItem {

        private ReadOnlyIntegerWrapper i = new ReadOnlyIntegerWrapper();
        private SimpleBooleanProperty b = new SimpleBooleanProperty(false);

        public IntegerItem(int num) {
            i.set(num);
        }

        public ReadOnlyIntegerProperty iProperty() {
            return i.getReadOnlyProperty();
        }

        public SimpleBooleanProperty bProperty() {
            return b;
        }

        @Override
        public String toString() {
            return String.valueOf(i.intValue());
        }

    }

}
