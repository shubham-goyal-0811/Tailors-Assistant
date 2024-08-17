package com.example.tailor.workerTable;

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
import java.util.ResourceBundle;

import com.example.tailor.MySqlConnectionKlass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class workertablecontroller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private ComboBox<String> spclList;

    @FXML
    private URL location;

    @FXML
    void toExcel(ActionEvent event) throws Exception {
        Writer writer = null;
        try {
            File file = new File("data.csv");
            writer = new BufferedWriter(new FileWriter(file));
            String text = "Name,Address,Mobile,Speciality\n";
            writer.write(text);
            for (workerbean p : getRecords()) {
                text = p.getWname() + "," + p.getWaddress() + "," + p.getWmobile() + "," + p.getSpl()+"\n";
                writer.write(text);
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



    @FXML
    private TableView<workerbean> tblView;

    PreparedStatement stmt;
    ObservableList<workerbean> getRecords(){
        ObservableList<workerbean> ary = FXCollections.observableArrayList();

        try{
//            stmt = con.prepareStatement(query);
//            if(flag == 1){
//                stmt.setString(1,"%"+spclList.getEditor().getText()+"%");
//                flag = 0;
//            }
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){

                String nm = rs.getString("wname");
                String ad = rs.getString("waddress");
                String mob = rs.getString("wmoblie");
                String sp = rs.getString("special");

                System.out.println(nm+" "+ad+" "+mob+" "+sp);
                ary.add(new workerbean(nm,ad,mob,sp));
            }

        }
        catch(Exception exp){
            exp.printStackTrace();
        }
        return ary;
    }

    void doShow(){

        tblView.getColumns().clear();
        tblView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<workerbean, String> uidc = new TableColumn<workerbean,String>("Name");//kuch bi
        uidc.setCellValueFactory(new PropertyValueFactory<>("wname"));
        uidc.setMinWidth(100);

        TableColumn<workerbean, String> wadr = new TableColumn<workerbean,String>("Address");//kuch bi
        wadr.setCellValueFactory(new PropertyValueFactory<>("waddress"));
        wadr.setMinWidth(100);

        TableColumn<workerbean, String> wmob = new TableColumn<workerbean,String>("Mobile");//kuch bi
        wmob.setCellValueFactory(new PropertyValueFactory<>("wmobile"));
        wmob.setMinWidth(100);

        TableColumn<workerbean, String> wspl = new TableColumn<workerbean,String>("Speciality");//kuch bi
        wspl.setCellValueFactory(new PropertyValueFactory<>("spl"));
        wspl.setMinWidth(100);

        tblView.getColumns().addAll(uidc,wadr,wmob,wspl);
        System.out.println("Number of columns: " + tblView.getColumns().size()); // Debugging output
        tblView.setItems(getRecords());
    }
    String query="";
    @FXML
    void getAll(ActionEvent event) {
        query = "select * from workers";
        try {
            stmt = con.prepareStatement(query);

        }
        catch(SQLException exp){
            exp.printStackTrace();
        }
//        getRecords(query);
        doShow();
    }
     int flag = 0;
    @FXML
    void getSpl(ActionEvent event) {
        query = "select * from workers where special like (?)";
        try {
            stmt = con.prepareStatement(query);
            stmt.setString(1,"%"+spclList.getEditor().getText()+"%");
        }
        catch(SQLException exp){
            exp.printStackTrace();
        }
//        flag = 1;
//        getRecords(query);
        doShow();
    }
    String speciality[]={"Coat","Shirt","Pant","Salwar Suit","Basket"};
    Connection con;
    @FXML
    void initialize() {
        con = MySqlConnectionKlass.doConnect();
        if(con == null){
            System.out.println("Pangaaaa");
        }
        else{
            System.out.println("Connection Established");
        }
        spclList.getItems().addAll(Arrays.asList(speciality));
    }

}
