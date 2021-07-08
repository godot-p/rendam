module com.gmail.fdhdcd.Renda {
    exports com.gmail.fdhdcd.renda.addresult;
    exports com.gmail.fdhdcd.renda.rankingviewer;
    exports com.gmail.fdhdcd.renda.timechange;
    exports com.gmail.fdhdcd.renda.dbsettings;
    exports com.gmail.fdhdcd.renda;

    opens com.gmail.fdhdcd.renda.addresult;
    opens com.gmail.fdhdcd.renda.rankingviewer;
    opens com.gmail.fdhdcd.renda.timechange;
    opens com.gmail.fdhdcd.renda.dbsettings;
    opens com.gmail.fdhdcd.renda;

    requires transitive javafx.base;
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires java.sql;
}
