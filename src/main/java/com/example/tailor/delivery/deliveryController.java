package com.example.tailor.delivery;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.example.tailor.MySqlConnectionKlass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class deliveryController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField billDis;

    @FXML
    private TextField custMob;

    @FXML
    private ListView<String> dressList;

    @FXML
    private ListView<Integer> idList;

    @FXML
    private ListView<Integer> priceList;

    @FXML
    private ListView<String> statusList;

    int finalBill = 0;
    int idx = -1;
    int prev = -2;

    void doWork(){
        idList.getItems().clear();
        statusList.getItems().clear();
        priceList.getItems().clear();
        dressList.getItems().clear();
        String mob = custMob.getText();
        String query = "SELECT * FROM measurement where cmobile = (?)";
        try {
            st = con.prepareStatement(query);
            st.setString(1,mob);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                idList.getItems().add(Integer.parseInt(rs.getString("orderid")));
                statusList.getItems().add(stat(rs.getInt("order_status")));
                priceList.getItems().add(rs.getInt("bill"));
                dressList.getItems().add(rs.getString("dress")+" - "+rs.getInt("qty"));
            }
        }
        catch(SQLException exp){
            exp.printStackTrace();
        }
    }

    @FXML
    void keyEnter(KeyEvent event) {
        doWork();
    }

    @FXML
    void doBill(MouseEvent event) {
        if(event.getClickCount() == 2){
            idx = idList.getSelectionModel().getSelectedIndex();
            if(idx == -1){
               idx = statusList.getSelectionModel().getSelectedIndex();
            }
            System.out.println(prev);
            if(idx == prev){
                Alert al = new Alert(Alert.AlertType.ERROR);
                al.setTitle("Invalid Index");
                al.setHeaderText("Item Selected Again");
                al.setContentText("Item is either Already delivered or Selected Again");
                al.showAndWait();
            }
            else {

                priceList.getSelectionModel().select(idx);
                idList.getSelectionModel().select(idx);
                statusList.getSelectionModel().select(idx);
                dressList.getSelectionModel().select(idx);
                int oid = idList.getSelectionModel().getSelectedItem();
                String query = "UPDATE measurement SET order_status = 3 where orderid = (?) and order_status = 2";
                String q2 = "UPDATE measurement SET doddate =  CURRENT_DATE where orderid = (?) and order_status = 2";
                try{
                    PreparedStatement stmt = con.prepareStatement(query);
                    PreparedStatement st = con.prepareStatement(q2);
                    stmt.setInt(1,oid);
                    st.setInt(1,oid);
                    st.executeUpdate();
                    stmt.executeUpdate();
                    System.out.println("Order Status and date Updated");
                }
                catch(SQLException exp){
                    System.out.println("Order not deluvered");
                    exp.printStackTrace();
                }
                finalBill += priceList.getSelectionModel().getSelectedItem();
                System.out.println(finalBill);
                billDis.setText(String.valueOf(finalBill));
                prev = idx;
                System.out.println(prev);
            }

        }
    }
    String stat (int s){
        if(s == 1){
            return "Order Placed";
        }
        else if (s==2){
            return "Order ready";
        }
        else{
            return "Order Delivered";
        }
    }


    PreparedStatement st;
    @FXML
    void getOrder(ActionEvent event) {
        doWork();
    }
    Connection con;
    @FXML
    void initialize() {
        con = MySqlConnectionKlass.doConnect();
        if(con == null){
            System.out.println("pangaaa");

        }
        else{
            System.out.println("Connection ++");
        }


    }

}
