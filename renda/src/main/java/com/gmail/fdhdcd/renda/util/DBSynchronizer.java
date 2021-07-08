package com.gmail.fdhdcd.renda.util;

import java.sql.SQLException;

import com.gmail.fdhdcd.renda.Main;

import javafx.scene.control.Alert;

public class DBSynchronizer {

    private ResultDAO2 dao = ResultDAO2.getInstance();

    private DBSynchronizer() {
    }

    public static DBSynchronizer getInstance() {
        return Holder.INSTANCE;
    }

    public void synchronize() {
        Main.resultList.clear();
        try {
            Main.resultList.addAll(dao.readFromDB());
        } catch (SQLException e) {
            Alert a = ErrorAlertGenerator.generate(e);
            a.setHeaderText("データベースにアクセスできません");
            a.showAndWait();
        }
    }

    private static class Holder {
        private static final DBSynchronizer INSTANCE = new DBSynchronizer();
    }

}
