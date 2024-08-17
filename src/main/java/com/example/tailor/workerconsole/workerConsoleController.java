package com.example.tailor.workerconsole;
import com.example.tailor.MySqlConnectionKlass;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

public class workerConsoleController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button RecieveBtn;

    @FXML
    private ListView<Date> datelist;

    @FXML
    private ListView<String> dresslist;

    @FXML
    private ListView<Integer> orderlist;

    @FXML
    private ComboBox<String> workerName;

    @FXML
    void selectWorker(ActionEvent event){
        orderlist.getItems().clear();
        dresslist.getItems().clear();
        datelist.getItems().clear();
        String wor = workerName.getSelectionModel().getSelectedItem();
        String query = "select orderid,dress,doddate from measurement where worker = (?) and order_status = 1";
        PreparedStatement stmt;
        try{
            stmt = con.prepareStatement(query);
            stmt.setString(1,wor);
            ResultSet res = stmt.executeQuery();
            while(res.next()){
                orderlist.getItems().add(res.getInt("orderid"));
                dresslist.getItems().add(res.getString("dress"));
                datelist.getItems().add(res.getDate("doddate"));
            }
        }
        catch(SQLException exp){
            exp.printStackTrace();
        }
    }

    @FXML
    void doRecieve(ActionEvent event) {
        //update all
        int idx = 0;
        while(!orderlist.getItems().isEmpty()){
            PreparedStatement stmt;
            orderlist.getSelectionModel().select(idx);
            int id = orderlist.getSelectionModel().getSelectedItem();
            int index = orderlist.getSelectionModel().getSelectedIndex();
            String query = "update measurement set order_status = 2 where orderid=(?)";
            try{
                dresslist.getSelectionModel().select(index);
                datelist.getSelectionModel().select(index);
                stmt = con.prepareStatement(query);
                stmt.setInt(1,id);
                stmt.executeUpdate();
                System.out.println("Status Updated ");
                if(index < orderlist.getItems().size() && index < dresslist.getItems().size() && index < datelist.getItems().size()) {
                    orderlist.getItems().remove(index);
                    dresslist.getItems().remove(index);
                    datelist.getItems().remove(index);
                }
            }
            catch (SQLException exp){
                exp.printStackTrace();
            }
        }
        System.out.println("All orders recieved");
    }

    @FXML
    void updateStatus(MouseEvent event) {
        //Single single update
        if(event.getClickCount() == 2) {
            PreparedStatement stmt;
            int id = orderlist.getSelectionModel().getSelectedItem();
            int index = orderlist.getSelectionModel().getSelectedIndex();
            String query = "update measurement set order_status = 2 where orderid=(?)";
            try{
                dresslist.getSelectionModel().select(index);
                datelist.getSelectionModel().select(index);
                stmt = con.prepareStatement(query);
                stmt.setInt(1,id);
                stmt.executeUpdate();
                System.out.println("Status Updated");

                if(index < orderlist.getItems().size() && index < dresslist.getItems().size() && index < datelist.getItems().size()) {
                    orderlist.getItems().remove(index);
                    dresslist.getItems().remove(index);
                    datelist.getItems().remove(index);
                }
            }
            catch (SQLException exp){
                exp.printStackTrace();
            }
        }
    }
    Connection con;
    PreparedStatement stmt;
    @FXML
    void initialize() {
        con = MySqlConnectionKlass.doConnect();

        if(con == null){
            System.out.println("Connection Failed");
        }
        else{
            System.out.println("Connection Established");
        }

        String query = "select DISTINCT worker from measurement where order_status=1";
        try {
            stmt = con.prepareStatement(query);
            ResultSet res = stmt.executeQuery();
            while(res.next()){
                workerName.getItems().add(res.getString("worker"));
            }
        }
        catch(SQLException exp){
            exp.printStackTrace();
        }



    }

}
