package com.example.tailor.worker;
import com.example.tailor.MySqlConnectionKlass;
import java.net.URL;
import java.util.*;
import java.util.ResourceBundle;

import com.example.tailor.MySqlConnectionKlass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import java.sql.*;



public class workercontroller {

    @FXML
    private ResourceBundle resources;



    @FXML
    private URL location;

    @FXML
    private Button delBtn;

    @FXML
    private Button newBtn;

    @FXML
    private Button saveBtn;

    @FXML
    private TextField specdis;

    @FXML
    private TextField workaddr;

    @FXML
    private TextField workmob;

    @FXML
    private TextField workname;

    @FXML
    private ListView<String> workspeclist;


    private ArrayList<String> speciality = new ArrayList<>();
    @FXML
    void addSpec(MouseEvent event) {
        if(event.getClickCount() == 2){
            String text = specdis.getText()+ ","+workspeclist.getSelectionModel().getSelectedItem();
            specdis.setText(text);
        }
    }
    //wname,waddress,wmoblie,special
    @FXML
    void doDelete(ActionEvent event) {
        try {
            stmt = con.prepareStatement("delete from workers where wname=?");
            stmt.setString(1,workname.getText());
            int c = stmt.executeUpdate();
            if(c == 1){
                //kaam hogya
                System.out.println("Worker Deleted Successfully");
                workname.clear();workaddr.clear();workmob.clear();specdis.clear();
            }
        }
        catch(SQLException exp){
            exp.printStackTrace();
        }
        catch(Exception exp){
            exp.printStackTrace();
        }
    }
    PreparedStatement stmt;
    @FXML
    void doSave(ActionEvent event) {
        try {
            stmt = con.prepareStatement("insert into workers values(?,?,?,?)");
            stmt.setString(1,workname.getText());
            stmt.setString(2,workaddr.getText());
            stmt.setString(3,workmob.getText());
            stmt.setString(4,specdis.getText());
            stmt.executeUpdate();
            System.out.println("Record Saved Successfully :)");
        }
        catch(SQLException exp){
            exp.printStackTrace();
        }
        catch(Exception exp){
            exp.printStackTrace();
        }
    }

    @FXML
    void getNew(ActionEvent event) {
        workname.clear();workaddr.clear();workmob.clear();
        specdis.clear();
    }
    Connection con;
    @FXML
    void initialize() {


        con = MySqlConnectionKlass.doConnect();
        if(con==null){
            System.out.println("Connection Did Not Establish");
        }else{
            System.out.println("Connection Done");
        }


        String query = "select * from worspl";
        try{
            PreparedStatement st = con.prepareStatement(query);
            ResultSet res = st.executeQuery();
            while(res.next()){
                speciality.add(res.getString("spl"));
            }

            workspeclist.getItems().addAll(speciality);
        }
        catch(SQLException exp){
            exp.printStackTrace();
        }

    }

}
