package com.gmail.fdhdcd.renda.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.gmail.fdhdcd.renda.addresult.Result;

public class ResultDAOUnused {

    private String address = null;

    private ResultDAOUnused() {
    }

    public static ResultDAOUnused getInstance() {
        return Holder.instance;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void sendToDB(Result result) throws SQLException {
        if (address == null) {
            throw new IllegalStateException("address is null: not initialized");
        }
        try (Connection con = DriverManager.getConnection("jdbc:sqlite:" + address); PreparedStatement ps = con.prepareStatement("INSERT INTO RESULTS VALUES(?, ?, ?, ?, ?)")) {
            ps.setString(1, result.getName());
            ps.setInt(2, result.getClicks());
            ps.setInt(3, result.getSeconds());
            ps.setDouble(4, result.getCps());
            String timeStr = result.getTime().toString();
            timeStr = timeStr.replace('T', ' ');
            timeStr = timeStr.substring(0, timeStr.indexOf("."));
            ps.setString(5, timeStr);
            ps.executeUpdate();
        }
    }

    public List<Result> readFromDB() throws SQLException {
        if (address == null) {
            throw new IllegalStateException("address is null: not initialized");
        }
        try (Connection con = DriverManager.getConnection("jdbc:sqlite:" + address); Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery("SElECT * FROM RESULTS");
            List<Result> resultList = new ArrayList<>();
            while (rs.next()) {
                resultList.add(new Result(rs.getString(1), rs.getInt(2), rs.getInt(3), LocalDateTime.parse(rs.getString(4).replace(' ', 'T'))));
            }
            return resultList;
        }
    }

    private static class Holder {
        private static final ResultDAOUnused instance = new ResultDAOUnused();
    }

}
