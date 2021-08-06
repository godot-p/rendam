package com.gmail.fdhdcd.renda.rankingviewer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    private Set<Integer> secondsSet = new HashSet<>(11, 1);

    @FXML
    public void initialize() {
        List<IntegerItem> list = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            IntegerItem item = new IntegerItem(i);
            item.bProperty().addListener((observable, oldVal, newVal) -> {
                if (newVal) {
                    secondsSet.add(item.iProperty().intValue());
                } else {
                    secondsSet.remove(item.iProperty().intValue());
                }
                loadRanking();
            });
            list.add(item);
            secondsSet.add(i);
        }
        ObservableList<IntegerItem> o = FXCollections.observableList(list);
        listView.setItems(o);

        listView.setCellFactory(CheckBoxListCell.forListView(i -> i.bProperty()));

        name.setCellValueFactory(new PropertyValueFactory<Result, String>("name"));
        seconds.setCellValueFactory(new PropertyValueFactory<Result, String>("seconds"));
        clicks.setCellValueFactory(new PropertyValueFactory<Result, String>("clicks"));
        cps.setCellValueFactory(new PropertyValueFactory<Result, String>("cps"));
        time.setCellValueFactory(new PropertyValueFactory<Result, String>("time"));

        name.setMinWidth(20);
        seconds.setMinWidth(20);
        clicks.setMinWidth(20);
        cps.setMinWidth(20);
        time.setMinWidth(20);

        loadRanking();
    }

    private void loadRanking() {
        List<Result> resultList = List.of(); //initialize in case of exception
        if (Main.firstController.isSynced()) {
            try {
                resultList = ResultDAO2.getInstance().readFromDB();
            } catch (SQLException e) {
                ErrorAlertGenerator.generate(e);
            }
        } else {
            resultList = Main.oList;
        }
        ObservableList<Result> filteredList = FXCollections.observableList(resultList.stream().filter(r -> {
            for (int i : secondsSet) {
                if (i == r.getSeconds()) {
                    return true;
                }
            }
            return false;
        }).collect(Collectors.toList()));
        tableView.setItems(filteredList);
    }

    private class IntegerItem {

        private ReadOnlyIntegerWrapper i = new ReadOnlyIntegerWrapper();
        private SimpleBooleanProperty b = new SimpleBooleanProperty(true);

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
