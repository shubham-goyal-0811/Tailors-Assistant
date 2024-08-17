package com.example.tailor.dashboard;

import com.example.tailor.HelloApplication;
import com.example.tailor.MySqlConnectionKlass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.chart.PieChart;
import java.util.HashMap;


public class dashboardController implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btn;

    @FXML
    private Label d;

    @FXML
    private Label ip;

    @FXML
    private Label noData;

    @FXML
    private PasswordField password;

    @FXML
    private PieChart pichart;

    @FXML
    private Label r;

    @FXML
    private AnchorPane slider;

    @FXML
    private Label to;

    @FXML
    private Label tor;

    @FXML
    private Label tr;

    @FXML
    private TextField username;

    private boolean isOpen = false;
    String u="admin";
    String p="admin";
    private Stage stage;
    private Scene scene;
    private Parent root;
    FXMLLoader loader;

    void showStatus(){
        String query1 = "Select bill FROM measurement WHERE order_status = 1";
        String query2 = "Select bill FROM measurement WHERE order_status = 2";
        String query3 = "Select bill FROM measurement WHERE order_status = 3";
        try{
            PreparedStatement stmt1 = con.prepareStatement(query1);
            PreparedStatement stmt2 = con.prepareStatement(query2);
            PreparedStatement stmt3 = con.prepareStatement(query3);

            ResultSet res1 = stmt1.executeQuery();
            int pro = 0;
            while(res1.next()){
                pro++;
            }

            ResultSet res2 = stmt2.executeQuery();
            int red = 0;
            while(res2.next()){
                red++;
            }

            ResultSet res3 = stmt3.executeQuery();
            int del = 0;
            while(res3.next()){
                del++;
            }

            ip.setText(Integer.toString(pro));
            r.setText(Integer.toString(red));
            d.setText(Integer.toString(del));
        }
        catch(SQLException exp){
            exp.printStackTrace();
        }
    }

    void totalRevenue(){
        String query = "Select bill FROM measurement WHERE order_status = 3";
        int revenue = 0;
        try{
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet res = stmt.executeQuery();
            while(res.next()) {
                int b = res.getInt("bill");
                revenue += b;
            }
            tor.setText(Integer.toString(revenue));
        }
        catch(SQLException exp){
            exp.printStackTrace();
        }
    }

    void showRevenue(){
        int revenue = 0;
        String query = "Select bill FROM measurement WHERE doddate = CURRENT_DATE";
        try{
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet res = stmt.executeQuery();
            while(res.next()) {
                int b = res.getInt("bill");

                revenue += b;
            }
            tr.setText(Integer.toString(revenue));
        }
        catch(SQLException exp){
            exp.printStackTrace();
        }
    }

    void showToday(){
        String query = "SELECT COUNT(*) AS order_count FROM measurement WHERE dateoforder = CURRENT_DATE";
        try{
            PreparedStatement st = con.prepareStatement(query);
            ResultSet res = st.executeQuery();
            if(res.next()) {
                int count = res.getInt("order_count");
                to.setText(Integer.toString(count));
            }
        }
        catch(SQLException exp){
            exp.printStackTrace();
        }
    }

    void showCharts(){
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        String query = "SELECT dress, COUNT(*) AS count FROM measurement GROUP BY dress";
        try{
            stmt = con.prepareStatement(query);
            ResultSet res = stmt.executeQuery();
            while(res.next()){
                String dress = res.getString("dress");
                int count = res.getInt("count");
                data.add(new PieChart.Data(dress,count));
            }
        }
        catch(SQLException exp){
            exp.printStackTrace();
        }
        pichart.setData(data);
//        pichart.setTitle("Clothing Details");
        pichart.setLabelLineLength(5);
        pichart.setLabelsVisible(true);
        pichart.setStartAngle(180);
    }

    @FXML
    void doSliding(javafx.event.ActionEvent event) {
        slide();
    }
    void slide()
    {
        if (u.equals(username.getText()) && p.equals(password.getText())) {

            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);

            if (isOpen) {
                noData.setVisible(true);
                ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
                pichart.setData(data);
                username.setText("");
                password.setText("");
                to.setText("0");
                tr.setText("0");
                tor.setText("0");
                ip.setText("0");
                r.setText("0");
                d.setText("0");
                slide.setToX(-190);
                isOpen = false;
            } else {
                noData.setVisible(false);
                showStatus();
                showToday();
                showCharts();
                totalRevenue();
                showRevenue();
                slide.setToX(0);
                isOpen = true;
            }
            slide.play();}
        else{
            showMyMsg("Invalid Email and Password");
        }
    }
    @FXML
    void keyEnter(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            slide();
        }
    }
    @FXML
    void openEnrollment(ActionEvent event) {
        loader= new FXMLLoader(HelloApplication.class.getResource("enrollmentt/EnrollmentView.fxml"));
        sets();
    }
    @FXML
    void openDeliv(ActionEvent event) {
        loader= new FXMLLoader(HelloApplication.class.getResource("deliveryDir/deliveryView.fxml"));
        sets();
    }
    @FXML
    void openMeasurement(ActionEvent event) {
        loader= new FXMLLoader(HelloApplication.class.getResource("measurementt/measurement-view.fxml"));
        sets();

    }

    @FXML
    void openReady(ActionEvent event) {
        loader= new FXMLLoader(HelloApplication.class.getResource("workerconolee/console-view.fxml"));
        sets();
    }

    @FXML
    void openWorkers(ActionEvent event) {
        loader= new FXMLLoader(HelloApplication.class.getResource("workerr/worker-view.fxml"));
        sets();
    }
    @FXML
    void OpenWorkerT(ActionEvent event) {
        loader= new FXMLLoader(HelloApplication.class.getResource("workerTableDir/table-view.fxml"));
        sets();
    }
    @FXML
    void openMeasureExp(ActionEvent event) {
        loader= new FXMLLoader(HelloApplication.class.getResource("ordersDir/order-view.fxml"));
        sets();
    }
    void sets() {
        try {
            Stage newStage = new Stage();
            Scene newScene = new Scene(loader.load());
            newStage.setScene(newScene);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void doLogout(MouseEvent event)
    {
        slide();
    }

    void showMyMsg(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Bad Credentials");
        alert.setContentText(msg);
        alert.showAndWait();
    }
    Connection con;
    PreparedStatement stmt ;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        con = MySqlConnectionKlass.doConnect();
        if(con == null){
            System.out.println("Connection failed");
        }
        else{
            System.out.println("Connection Established");
        }
        slider.setTranslateX(-190);

    }
}