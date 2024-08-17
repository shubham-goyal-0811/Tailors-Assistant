package com.example.tailor.ordercontroller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import com.example.tailor.MySqlConnectionKlass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class order {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField mobileText;

    @FXML
    private ComboBox<String> statusList;

    @FXML
    private TableView<orderbean> tblView;

    @FXML
    private ComboBox<String> workerList;

    PreparedStatement stmt;

    void addCols(){
        tblView.getColumns().clear();
        tblView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        //order id,mobile,dress,qty,dateof delivery, bill, worker,status
        TableColumn<orderbean, String> oidc = new TableColumn<orderbean,String>("Order Id");//kuch bi
        oidc.setCellValueFactory(new PropertyValueFactory<>("order_id"));//same as in bean file
        oidc.setMinWidth(100);

        TableColumn<orderbean, String> oidm = new TableColumn<orderbean,String>("Mobile");//kuch bi
        oidm.setCellValueFactory(new PropertyValueFactory<>("mob"));
        oidm.setMinWidth(100);

        TableColumn<orderbean, String> oidr = new TableColumn<orderbean,String>("Dress");//kuch bi
        oidr.setCellValueFactory(new PropertyValueFactory<>("dress"));
        oidr.setMinWidth(100);

        TableColumn<orderbean, String> oidq = new TableColumn<orderbean,String>("Quantity");//kuch bi
        oidq.setCellValueFactory(new PropertyValueFactory<>("qty"));
        oidq.setMinWidth(100);

        TableColumn<orderbean, String> oidod = new TableColumn<orderbean,String>("Delivery Date");//kuch bi
        oidod.setCellValueFactory(new PropertyValueFactory<>("dod"));
        oidod.setMinWidth(100);

        TableColumn<orderbean, String> oidb = new TableColumn<orderbean,String>("Bill");//kuch bi
        oidb.setCellValueFactory(new PropertyValueFactory<>("bill"));
        oidb.setMinWidth(100);

        TableColumn<orderbean, String> oidw = new TableColumn<orderbean,String>("Worker");//kuch bi
        oidw.setCellValueFactory(new PropertyValueFactory<>("worker"));
        oidw.setMinWidth(100);

        TableColumn<orderbean, String> oidst = new TableColumn<orderbean,String>("Status");//kuch bi
        oidst.setCellValueFactory(new PropertyValueFactory<>("st"));
        oidst.setMinWidth(100);

        tblView.getColumns().addAll(oidc,oidm,oidr,oidq,oidod,oidb,oidw,oidst);
        System.out.println("Number of columns: " + tblView.getColumns().size()); // Debugging output
        tblView.setItems(getRecords());
    }

    ObservableList<orderbean> getRecords(){
        ObservableList<orderbean> ary = FXCollections.observableArrayList();
        //order id,mobile,dress,qty,dateof delivery, bill, worker,status
        try{
            ResultSet res = stmt.executeQuery();
            while(res.next()){
                int oid = res.getInt("orderid");
                String cmob = res.getString("cmobile");
                String dr = res.getString("dress");
                int qt = res.getInt("qty");
                Date dd = res.getDate("doddate");
                int bil = res.getInt("bill");
                String work = res.getString("worker");
                int st = res.getInt("order_status");

                ary.add(new orderbean(oid,cmob,dr,qt,dd.toString(),bil,work,st));
            }
            System.out.println(ary.size());
        }
        catch(SQLException exp){
            exp.printStackTrace();
        }
        return ary;
    }
    @FXML
    void doShowAll(ActionEvent event) {
        String query = "Select * from measurement";
        try{
            stmt= con.prepareStatement(query);
            addCols();
        }
        catch(SQLException exp){
            exp.printStackTrace();
        }
    }

    @FXML
    void getMobile(ActionEvent event) {
        String mob = mobileText.getText();
        String query = "SELECT * FROM measurement WHERE cmobile = (?)";
        try{
            stmt= con.prepareStatement(query);
            stmt.setString(1,mob);
            addCols();
        }
        catch(SQLException exp){
            exp.printStackTrace();
        }

    }

    @FXML
    void getStatus(ActionEvent event) {
        int idx = statusList.getSelectionModel().getSelectedIndex() + 1;
        String query = "SELECT * FROM measurement WHERE order_status = (?)";
        try{
            stmt= con.prepareStatement(query);
            stmt.setInt(1,idx);
            addCols();
        }
        catch(SQLException exp){
            exp.printStackTrace();
        }
    }

    @FXML
    void showWorker(ActionEvent event) {
        String worker = workerList.getSelectionModel().getSelectedItem();
        String query = "SELECT * FROM measurement WHERE worker = (?)";
        try{
            stmt= con.prepareStatement(query);
            stmt.setString(1,worker);
            addCols();
        }
        catch(SQLException exp){
            exp.printStackTrace();
        }

    }

    @FXML
    void toExcel(ActionEvent event) throws Exception {
        Writer writer = null;
        try {

            TextInputDialog dlg = new TextInputDialog();
            dlg.setTitle("Save File");
            dlg.setHeaderText("Enter File name:");
            dlg.setContentText("Please do not add file extension like .csv, etc.");
            Optional<String> rs = dlg.showAndWait();
            if(rs.isPresent()){
                String fname = rs.get();
                File file = new File(fname+".csv");
                writer = new BufferedWriter(new FileWriter(file));
//                order id,mobile,dress,qty,dateof delivery, bill, worker,status
                String text = "Order Id,Mobile,Dress,Quantity,Date of Delivvery,Bill,Worker,Status\n";
                writer.write(text);
                for (orderbean p : getRecords()) {
                    text = p.getOrder_id()+","+p.getMob()+","+p.getDress()+","+p.getQty()+","+p.getDod()+","+p.getBill()+","+p.getWorker()+","+p.getSt()+"\n";
                    writer.write(text);
                }
            }

        }
        catch(Exception exp){
            exp.printStackTrace();
        }
        finally {
            writer.flush();
            writer.close();
        }
    }

    Connection con;
    @FXML
    void initialize() {
        con = MySqlConnectionKlass.doConnect();
        if(con == null){
            System.out.println("pangaaaa");
        }
        else{
            System.out.println("Connection established");
        }


        String sts[] = {"Order Placed", "Order Ready","Order Delivered"};
        statusList.getItems().addAll(Arrays.asList(sts));
        String query = "SELECT wname FROM workers";
        PreparedStatement st;
        try{
            st = con.prepareStatement(query);
            ResultSet res = st.executeQuery();
            while(res.next()){
                workerList.getItems().add(res.getString("wname"));
            }
        }
        catch(SQLException exp){
            exp.printStackTrace();
        }
        //order id,mobile,dress,qty,dateof delivery, bill, worker,status -> measurements table
    }

}
