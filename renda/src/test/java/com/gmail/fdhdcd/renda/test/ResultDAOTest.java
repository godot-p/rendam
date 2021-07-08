package com.gmail.fdhdcd.renda.test;

import static org.junit.jupiter.api.Assertions.fail;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.gmail.fdhdcd.renda.addresult.Result;
import com.gmail.fdhdcd.renda.util.ResultDAO;

@SuppressWarnings("static-method")
public class ResultDAOTest {

    //@Test
    public void testInitializeDB() throws SQLException {
        ResultDAO dao = ResultDAO.getInstance();
        dao.setAddress("localhost");
        dao.setUser("root");
        dao.setPassword("MySQL");
        dao.setDatabase("testdb");
        dao.setTable("junit2");
        //dao.createTable();
        Result send = new Result("namae2", 100, 3);
        System.out.println(send);
        dao.sendToDB(send);
        List<Result> results = dao.readFromDB();
        for (Result r : results) {
            if (!r.equals(send)) {
                fail();
            }
        }
    }

    //@Test
    public void singletonTest() {
        if (!(ResultDAO.getInstance() == ResultDAO.getInstance())) {
            fail();
        }
    }

    @Test
    public void tessst() {
        System.out.println(LocalDateTime.now().truncatedTo(ChronoUnit.MICROS));
        System.out.println(100d / 3);
    }

}
