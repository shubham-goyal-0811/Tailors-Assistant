package com.example.tailor.measurement;
import com.example.tailor.MySqlConnectionKlass;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.xml.transform.Result;

public class measurementcontroller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    private int currOrderId=-1;

    @FXML
    private TextField status;

    @FXML
    private TextField billDis;

    @FXML
    private TextField custMob;

    @FXML
    private TextArea measure;

    @FXML
    private DatePicker dateofDelivery;

    @FXML
    private ImageView desImg;

    @FXML
    private Button dofindMes;

    @FXML
    private ComboBox<String> dressList;


    @FXML
    private TextField price;

    @FXML
    private ComboBox<Integer> qtyList;

    @FXML
    private Button upldBtn;

    @FXML
    private ComboBox<String> workersList;

    @FXML
    private AnchorPane scenePane;

    Stage stage;

    String dress[] = {"Coat","Shirt","Pant","Salwar Suit","Basket"};


    @FXML
    void calcBill(ActionEvent event){
        int p = 0;
        int b = 0;
        int bill = 0;
        if(!(price.getText() == null) && !(qtyList.getEditor().getText() == null)) {
            p = Integer.parseInt(price.getText());
            b = Integer.parseInt(qtyList.getEditor().getText());

            bill = p * b;

        }
        if(bill != -1){
            billDis.setText(Integer.toString(bill));
        }
        else{
            billDis.setText("Invalid Input");
        }
    }

    @FXML
    void doClose(ActionEvent event) {
        stage = (Stage)scenePane.getScene().getWindow();
        System.out.println("window closed");
        stage.close();
    }

    @FXML
    void doNew(ActionEvent event) {
        custMob.clear();
        dressList.getEditor().clear();
        dateofDelivery.getEditor().clear();
        qtyList.getEditor().clear();
        price.clear();
        billDis.clear();
        workersList.getEditor().clear();
        measure.clear();
        pcpth="none";
    }
    long orderid;
    PreparedStatement stmt;
    @FXML
    void doSave(ActionEvent event) {
        try {
            if(currOrderId == -1 ) {
                stmt = con.prepareStatement("insert into measurement (cmobile, dress, despicpath, doddate, qty, pricep, bill, measurements, worker, order_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            }
            else{
                stmt = con.prepareStatement("UPDATE measurement SET cmobile = ?, dress = ?, despicpath = ?, doddate = ?, qty = ?, pricep = ?, bill = ?, measurements = ?, worker = ?, order_status = ? WHERE orderid = ?");
            }
            stmt.setString(1,custMob.getText());
            stmt.setString(2,dressList.getSelectionModel().getSelectedItem());

            stmt.setString(3,pcpth);

            LocalDate local = dateofDelivery.getValue();
            java.sql.Date date = java.sql.Date.valueOf(local);
            stmt.setDate(4,date);

            stmt.setInt(5,Integer.parseInt(qtyList.getEditor().getText()));
            stmt.setInt(6,Integer.parseInt(price.getText()));
            if(billDis.getText() != "Invalid Input"){
                stmt.setInt(7,Integer.parseInt(billDis.getText()));
            }
            stmt.setString(8,measure.getText());
            stmt.setString(9,workersList.getSelectionModel().getSelectedItem());
            stmt.setInt(10,Integer.parseInt(status.getText()));

            if (currOrderId != -1) {
                stmt.setInt(11, currOrderId);
            }

            stmt.executeUpdate();
            System.out.println("Order Placed :)");
            if (currOrderId == -1) {
                // Retrieve the generated order ID for a new record
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    long orderId = generatedKeys.getLong(1);

                    // Create and show the information alert with the generated orderId
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Order Saved");
                    alert.setHeaderText(null);
                    alert.setContentText("Generated Order ID: " + orderId);
                    alert.showAndWait();
                }
            } else {
                // Show a confirmation alert for an updated record
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Order Updated");
                alert.setHeaderText(null);
                alert.setContentText("Order ID: " + currOrderId + " has been updated successfully.");
                alert.showAndWait();
            }

            // Reset currentOrderId after saving or updating
            currOrderId = -1;
        }
        catch(SQLException exp){
            exp.printStackTrace();
        }
        catch(Exception exp){
            exp.printStackTrace();
        }

    }

    @FXML
    void doUpdate(ActionEvent event) {

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Fetch Order Details");
        dialog.setHeaderText("Enter Order ID");
        dialog.setContentText("Order ID:");


        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String orderIdStr = result.get();

            try {
                currOrderId = Integer.parseInt(orderIdStr);

                // Step 2: Fetch the order details using the provided orderid
                String query = "SELECT * FROM measurement WHERE orderid = ?";
                stmt = con.prepareStatement(query);
                stmt.setInt(1, currOrderId);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    // Step 3: Process the fetched order details
                    String cmobile = rs.getString("cmobile");
                    String dress = rs.getString("dress");
                    String despicpath = rs.getString("despicpath");
                    Date doddate = rs.getDate("doddate");
                    int qty = rs.getInt("qty");
                    int pricep = rs.getInt("pricep");
                    int bill = rs.getInt("bill");
                    String measurements = rs.getString("measurements");
                    String worker = rs.getString("worker");
                    int orderStatus = rs.getInt("order_status");

                    // Display the fetched details or update the UI components as needed
                    // For example, you can set these values to your input fields if you have them
                    custMob.setText(cmobile);
                    dressList.getSelectionModel().select(dress);
                    pcpth = despicpath;
                    try{
                    if(!pcpth.equals("none"))
                        desImg.setImage(new Image(new FileInputStream(pcpth)));
                    }
                    catch(FileNotFoundException exp){
                        exp.printStackTrace();
                    }
                    dateofDelivery.setValue(doddate.toLocalDate());
                    qtyList.getEditor().setText(String.valueOf(qty));
                    price.setText(String.valueOf(pricep));
                    billDis.setText(String.valueOf(bill));
                    measure.setText(measurements);
                    workersList.getSelectionModel().select(worker);
                    status.setText(String.valueOf(orderStatus));
                } else {
                    // Order ID not found
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Order Not Found");
                    alert.setHeaderText(null);
                    alert.setContentText("No order found with Order ID: " + currOrderId);
                    currOrderId = -1;
                    alert.showAndWait();
                }

            } catch (NumberFormatException e) {
                // Invalid Order ID
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Input");
                alert.setHeaderText(null);
                alert.setContentText("Please enter a valid Order ID.");
                alert.showAndWait();
                currOrderId = -1;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }


    String pcpth = "none";
    @FXML
    void uploadDesign(ActionEvent event) {
        FileChooser file = new FileChooser();
        file.setTitle("Upload Design Image");
        file.setInitialDirectory(new File("C:\\"));
        file.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPEG image","*.jpg"),new FileChooser.ExtensionFilter("PNG image","*.png"),new FileChooser.ExtensionFilter("All Image Files","*.jpg","*.png"));
        File selectedFile = file.showOpenDialog(null);
        pcpth = selectedFile.getAbsolutePath();
        System.out.println(pcpth);
        try{
            if(!pcpth.equals("none")) {
                desImg.setImage(new Image(new FileInputStream(selectedFile)));
                System.out.println("Image uploaded succesfully");
            }
        }
        catch(FileNotFoundException exp){
            exp.printStackTrace();
        }
    }

    @FXML
    void findMes(ActionEvent event){
        PreparedStatement s;
        try {
            s = con.prepareStatement("select DISTINCT measurements from measurement where cmobile = ?");
            if(custMob.getText() != "null") {
                s.setInt(1, Integer.parseInt(custMob.getText()));
                ResultSet res = s.executeQuery();
                while(res.next()){
                    String ms = measure.getText() + res.getString("measurements");
                    measure.setText(ms);


                }
            }
            else{
                System.out.println("Enter Valid Mobile Number");
            }
        }
        catch(SQLException exp){
            exp.printStackTrace();
        }
    }




    Connection con;
    @FXML
    void initialize() {
        dressList.getItems().addAll(Arrays.asList(dress));

        for(int i=0;i<=50;i++){
            qtyList.getItems().add(i);
        }
        dressList.setOnAction(event -> populateWorkers());
        con = MySqlConnectionKlass.doConnect();
        if(con == null){
            System.out.println("Pangaaaa");
        }
        else{
            System.out.println("connection done");
        }
    }

    void populateWorkers(){
        workersList.getItems().clear();//agar pichle pde hai toh hta de
        String selectedDress = dressList.getSelectionModel().getSelectedItem();
        if(selectedDress != null){
            PreparedStatement smt;
            try{
                smt = con.prepareStatement("SELECT wname from workers where special like ?");
                smt.setString(1,"%"+selectedDress+"%");
                try(ResultSet res = smt.executeQuery()){
                    while(res.next()){
                        workersList.getItems().add(res.getString("wname"));
                    }
                }
            }
            catch(SQLException exp){
                exp.printStackTrace();
            }
        }
    }

}
