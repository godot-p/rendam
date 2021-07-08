package com.gmail.fdhdcd.renda.test;

import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.gmail.fdhdcd.renda.addresult.Result;
import com.gmail.fdhdcd.renda.util.ResultDAO2;

@SuppressWarnings("static-method")
public class ResultDAO2Test {

    //@Test
    public void openSendTest() throws SQLException {
        ResultDAO2 dao = ResultDAO2.getInstance();
        dao.setAddress("localhost");
        dao.setDatabase("test_db");
        dao.setUser("root");
        dao.setPassword(null);
        dao.setTable("test");
        dao.openConnection();
        dao.sendToDB(new Result("test", 10, 2));
    }

    @Test
    public void readTest() throws SQLException {
        ResultDAO2 dao = ResultDAO2.getInstance();
        dao.setAddress("localhost");
        dao.setDatabase("test_db");
        dao.setUser("root");
        dao.setPassword(null);
        dao.setTable("test");
        List<Result> results = dao.readFromDB();
        for (Result r : results) {
            System.out.println(r.toString());
        }
    }

}
