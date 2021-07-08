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

public class ResultDAO {

    private String address;
    private String user;
    private String password;
    private String database;
    private String table;

    private ResultDAO() {
    }

    public static ResultDAO getInstance() {
        return Holder.INSTANCE;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getDatabase() {
        return database;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getTable() {
        return table;
    }

    /**
     * tableフィールドに指定された名前のテーブルを作成します。
     * @throws SQLException
     */
    public void createTable() throws SQLException {
        checkIfNull();
        try (Connection con = DriverManager.getConnection("jdbc:mysql://" + address + "/" + database, user, password);
                Statement st = con.createStatement()) {
            st.execute("CREATE TABLE " + table + "(NAME TEXT, CLICKS INT, SECONDS INT, CPS DOUBLE, TIME DATETIME(6))");
        }
    }

    /**
     *
     * @param result
     * @throws SQLException
     */
    public void sendToDB(Result result) throws SQLException {
        checkIfNull();
        try (Connection con = DriverManager.getConnection("jdbc:mysql://" + address + "/" + database, user, password); PreparedStatement ps = con.prepareStatement("INSERT INTO " + table + " VALUES(?, ?, ?, ?, ?)")) {
            ps.setString(1, result.getName());
            ps.setInt(2, result.getClicks());
            ps.setInt(3, result.getSeconds());
            ps.setDouble(4, result.getCps());
            ps.setTimestamp(5, Timestamp.valueOf(result.getTime()));
            ps.executeUpdate();
        }
    }

    public List<Result> readFromDB() throws SQLException {
        checkIfNull();
        try (Connection con = DriverManager.getConnection("jdbc:mysql://" + address + "/" + database, user, password); Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM " + table);
            List<Result> resultList = new ArrayList<>();
            while (rs.next()) {
                resultList.add(new Result(rs.getString(1), rs.getInt(2), rs.getInt(3), rs.getTimestamp(5).toLocalDateTime()));
            }
            return resultList;
        }
    }

    private void checkIfNull() {
        if (address == null) {
            throw new IllegalStateException("address is null: not initialized");
        }
        if (user == null) {
            throw new IllegalStateException("userName is null: not initialized");
        }
        if (password == null) {
            throw new IllegalStateException("password is null: not initialized");
        }
        if (table == null) {
            throw new IllegalStateException("tableName is null: not initialized");
        }
    }

    private static class Holder {
        private static final ResultDAO INSTANCE = new ResultDAO();
    }

}
