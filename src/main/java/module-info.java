module com.example.tailor {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;
    requires mysql.connector.java;
//    requires javax.mail.api;
    requires java.mail;


    opens com.example.tailor to javafx.fxml;
    exports com.example.tailor;

    opens com.example.tailor.enrollment to javafx.fxml;
    exports com.example.tailor.enrollment;

    opens com.example.tailor.worker to javafx.fxml;
    exports com.example.tailor.worker;

    opens com.example.tailor.measurement to javafx.fxml;
    exports com.example.tailor.measurement;

    opens com.example.tailor.workerconsole to javafx.fxml;
    exports com.example.tailor.workerconsole;

    opens com.example.tailor.workerTable to javafx.fxml;
    exports com.example.tailor.workerTable;


    opens com.example.tailor.ordercontroller to javafx.fxml;
    exports com.example.tailor.ordercontroller;

    opens com.example.tailor.delivery to javafx.fxml;
    exports com.example.tailor.delivery;

    opens com.example.tailor.dashboard to javafx.fxml;
    exports com.example.tailor.dashboard;
}