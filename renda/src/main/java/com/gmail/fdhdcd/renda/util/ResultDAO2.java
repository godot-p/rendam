package com.gmail.fdhdcd.renda.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.gmail.fdhdcd.renda.addresult.Result;

public class ResultDAO2 {

    private Connection con;
    private String address = "";
    private String user = "";
    private String password = "";
    private String database = "";
    private String table = "test";

    private ResultDAO2() {
    }

    public static ResultDAO2 getInstance() {
        return Holder.INSTANCE;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public void sendToDB(Result result) throws SQLException {
        if (con == null) {
            openConnection();
        }
        try (PreparedStatement ps = con.prepareStatement("INSERT INTO " + table + " VALUES(?, ?, ?, ?)")) {
            ps.setString(1, result.getName());
            ps.setInt(2, result.getClicks());
            ps.setInt(3, result.getSeconds());
            ps.setTimestamp(4, Timestamp.valueOf(result.getTime()));
            ps.executeUpdate();
        }
    }

    public List<Result> readFromDB() throws SQLException {
        if (con == null) {
            openConnection();
        }
        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM " + table);
            List<Result> resultList = new ArrayList<>();
            while (rs.next()) {
                resultList.add(new Result(rs.getString(1), rs.getInt(2), rs.getInt(3), rs.getTimestamp(4).toLocalDateTime()));
            }
            return resultList;
        }
    }

    public void openConnection() throws SQLException {
        closeConnection();
        checkIfNull();
        con = DriverManager.getConnection("jdbc:mysql://" + address + "/" + database, user, password);
    }

    public void closeConnection() throws SQLException {
        if (con != null) {
            con.close();
        }
    }

    private void checkIfNull() {
        if (address == null) {
            throw new IllegalStateException("address is null: not initialized");
        }
        if (table == null) {
            throw new IllegalStateException("tableName is null: not initialized");
        }
    }

    private static class Holder {
        private static final ResultDAO2 INSTANCE = new ResultDAO2();
    }

}
