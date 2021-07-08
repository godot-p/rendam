package com.gmail.fdhdcd.renda.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;
import javafx.stage.Window;

public class ErrorAlertGenerator {

    private ErrorAlertGenerator() {
    }

    /**
     * コンテンツテキストを渡されたThrowableのスタックトレースを含む{@link TitledPane}に置き換えたAlertを返します。
     * AlertTypeには{@link Alert.AlertType#ERROR ERROR}が指定され、またリサイズが可能です。
     * @param t スタックトレースを表示させるThrowable。
     * @return Throwableのスタックトレースを含むAlert。
     */
    public static Alert generate(Throwable t) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        String stackTrace = sw.toString();
        ScrollPane scrollPane = new ScrollPane(new Label(stackTrace));
        TitledPane titledPane = new TitledPane("Details", scrollPane);
        titledPane.setExpanded(false);
        a.getDialogPane().setContent(titledPane);
        a.setResizable(true);
        Window w = a.getDialogPane().getScene().getWindow();
        Stage s = (Stage)w;
        s.setOnShown(windowEvent -> {
            double minHeight = s.getHeight() + 50;
            s.setHeight(minHeight);
            s.setMinHeight(minHeight);
        });
        return a;
    }

}
