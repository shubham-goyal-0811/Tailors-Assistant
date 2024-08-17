package com.example.tailor;

import java.sql.*;

import java.sql.DriverManager;

public class MySqlConnectionKlass {
    public static Connection doConnect(){
        Connection con = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con =  DriverManager.getConnection("jdbc:mysql://localhost/tailor","your-username","your-password");
            //works on singleton design pattern
        }
        catch (SQLException exp){
            exp.printStackTrace();
        }
        catch(ClassNotFoundException exp){
            exp.printStackTrace();
        }

        return con;
    }

    public static void main(String srg[]){
        if(doConnect()== null){
            System.out.println("Connection failed");
        }
        else{
            System.out.println("Connection Established");
        }
    }
}
