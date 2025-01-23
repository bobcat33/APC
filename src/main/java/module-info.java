module com.github.bobcat33.apcinterface {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javaosc.core;
    requires java.sql;


    opens com.github.bobcat33.apc to javafx.fxml;
    exports com.github.bobcat33.apc;
    exports com.github.bobcat33.apc.apcinterface;
    opens com.github.bobcat33.apc.apcinterface to javafx.fxml;
    exports com.github.bobcat33.apc.apcinterface.listener;
    opens com.github.bobcat33.apc.apcinterface.listener to javafx.fxml;
    exports com.github.bobcat33.apc.apcinterface.message;
    opens com.github.bobcat33.apc.apcinterface.message to javafx.fxml;
    exports com.github.bobcat33.apc.listeners;
    opens com.github.bobcat33.apc.listeners to javafx.fxml;
    exports com.github.bobcat33.apc.apcinterface.graphics;
    opens com.github.bobcat33.apc.apcinterface.graphics to javafx.fxml;
    exports com.github.bobcat33.apc.apcinterface.communication;
    opens com.github.bobcat33.apc.apcinterface.communication to javafx.fxml;
}