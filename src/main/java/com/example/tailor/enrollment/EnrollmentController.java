package com.example.tailor.enrollment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.Properties;
import java.util.ResourceBundle;
import com.example.tailor.MySqlConnectionKlass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EnrollmentController {

    @FXML
    private TextField userMail;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField EndrolledCity;

    @FXML
    private DatePicker EndrolledDOB;

    @FXML
    private ImageView imgPrev;

    @FXML
    private ComboBox<String> EndrolledGender;

    @FXML
    private TextField EnrolledAddress;

    @FXML
    private TextField EnrolledName;

    @FXML
    private TextField EnrolledNumber;
    @FXML
    private Button uploadBtn;

    String filepath = "none";
    @FXML
    void doUploadImg(ActionEvent event) {
        FileChooser file = new FileChooser();
        file.setTitle("Upload Image");
        file.setInitialDirectory(new File("C:\\"));
        file.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPEG image","*.jpg"),new FileChooser.ExtensionFilter("PNG image","*.png"),new FileChooser.ExtensionFilter("All Image Files","*.jpg","*.png"));
        File selectedFile = file.showOpenDialog(null);
        filepath = selectedFile.getAbsolutePath();
        System.out.println(filepath);
        try{
            if(!filepath.equals("none")) {
                imgPrev.setImage(new Image(new FileInputStream(selectedFile)));
                System.out.println("Image uploaded succesfully");
            }
        }
        catch(FileNotFoundException exp){
            exp.printStackTrace();
        }
    }

    @FXML
    void DoClearAll(ActionEvent event) {
        try{
            stmt = con.prepareStatement("Delete From profile where Mobile=?");
            stmt.setString(1,EnrolledNumber.getText());
            int count = stmt.executeUpdate();
            if(count==1){
                ShowMyMsg("Record Cleared Successfully");
                EnrolledName.clear();
                EnrolledAddress.clear();
                EndrolledCity.clear();
                EndrolledGender.getSelectionModel().clearSelection();
                EndrolledDOB.getEditor().clear();
                filepath="none";
                EnrolledNumber.clear();
                userMail.clear();
                System.out.println("Record Deleted");
            }else{
                ShowMyMsg("Invalid Number");
            }
        }catch (Exception exp){
            exp.printStackTrace();
        }
    }

    void sendMail(){
        System.out.println("aaya");
        String message="This is a welcome Email .";
        String subject="Welcome to ABC tailors";
        String to = userMail.getText();
        String from="assistanttailor@gmail.com";


        String host = "smtp.gmail.com";

        //get the system properties
        Properties properties = System.getProperties();
        System.out.println("Prop : "+ properties);


        //host set
        properties.put("mail.smtp.host",host);//key value pair
        properties.put("mail.smtp.port","465");//many other options available
        properties.put("mail.smtp.ssl.enable","true");
        properties.put("mail.smtp.auth","true");

        //Step 1 : get the session object
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("assistanttailor@gmail.com","qglv amxs yhou gdgc");
            }
        });
//        session.setDebug(true);


        //Step 2: Compose the message
        MimeMessage m =  new MimeMessage(session);

        // from email
        try {
            m.setFrom(from);

            //adding reciptant
            m.addRecipient(Message.RecipientType.TO,new InternetAddress(to));

            //adding subject to message
            m.setSubject(subject);

            //adding message
            m.setText(message);


            //Step 3: Send the mail using transport class
            Transport.send(m);
            System.out.println("Email Sent  Successfully");

        }
        catch(MessagingException exp){
            exp.printStackTrace();

        }
    }

    @FXML
    void DoEditData(ActionEvent event) {
        //Mobile, cname, address, city, gender, dob date;
        try {
            stmt = con.prepareStatement("UPDATE profile SET cname = ?, address = ?, city = ?, gender = ?, dob = ? ,picpath =?,email=? WHERE Mobile = ?");

            stmt.setString(1, EnrolledName.getText());
            stmt.setString(2, EnrolledAddress.getText());
            stmt.setString(3, EndrolledCity.getText());
            stmt.setString(4, EndrolledGender.getSelectionModel().getSelectedItem());

            LocalDate local = EndrolledDOB.getValue();
            java.sql.Date date = java.sql.Date.valueOf(local);
            stmt.setDate(5, date);

            // Assuming Mobile number is the unique key for updating the record
            stmt.setString(6,filepath);
            stmt.setString(7,userMail.getText());

            stmt.setString(8, EnrolledNumber.getText());

            int count = stmt.executeUpdate();
            if (count == 1) {
                ShowMyMsg("Record Updated Successfully");
                System.out.println("Record Updated");
            } else {
                ShowMyMsg("Invalid Number");
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }


    PreparedStatement stmt;
    @FXML
    void DoEnroll(ActionEvent event) {
        try{
            //Mobile , cname , address , city , gender , dob date ,pic path
            stmt = con.prepareStatement("insert into profile values (?,?,?,?,?,?,?,?)");
            stmt.setString(1,EnrolledNumber.getText());
            stmt.setString(2,EnrolledName.getText());
            stmt.setString(3,EnrolledAddress.getText());
            stmt.setString(4,EndrolledCity.getText());
            stmt.setString(5,EndrolledGender.getSelectionModel().getSelectedItem());

            LocalDate local = EndrolledDOB.getValue();
            java.sql.Date date = java.sql.Date.valueOf(local);
            stmt.setDate(6,date);
            stmt.setString(7,filepath);
            stmt.setString(8,userMail.getText());
            sendMail();
            stmt.executeUpdate();
            System.out.println("Record Saved Successfully");

        }catch(SQLException exp){
            exp.printStackTrace();
        }
        catch(Exception exp){
            exp.printStackTrace();
        }
    }

    @FXML
    void DoFetchData(ActionEvent event) {
        try{
            stmt = con.prepareStatement("Select * from profile where Mobile=?");
            stmt.setString(1,EnrolledNumber.getText());
            ResultSet records = stmt.executeQuery();
            while(records.next()){
                String mobilee = records.getString("mobile");
                String cnamee = records.getString("cname");
                String addresss = records.getString("address");
                String cityy = records.getString("city");
                String genderr = records.getString("gender");
                Date dt = records.getDate("dob");
                String pth = records.getString("picpath");
                System.out.println(mobilee + " " + cnamee + " " + addresss + " " + cityy + " " + genderr + " " + dt+ " "+pth);
                EnrolledName.setText(cnamee);
                EnrolledNumber.setText(mobilee);
                EndrolledCity.setText(cityy);
                EndrolledGender.setValue(genderr);
                EnrolledAddress.setText(addresss);
                EndrolledDOB.setValue(dt.toLocalDate());
                filepath = pth;
                if(!filepath.equals("none"))
                    imgPrev.setImage(new Image(new FileInputStream(filepath)));

            }
        }catch(Exception exp){
            exp.printStackTrace();
        }
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
    }

    void ShowMyMsg(String Msg){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Its Header");
        alert.setContentText(Msg);

        alert.showAndWait();
    }
}

