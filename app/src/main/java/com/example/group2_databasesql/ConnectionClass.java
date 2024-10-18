package com.example.group2_databasesql;

import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionClass {
    String classes = "net.sourceforge.jtds.jdbc.Driver";
    protected static String ip="10.0.2.2";//192.168.56.1
    protected static  String port="1433";
    protected static String db="SaleManagement";
    protected static String un="sa";
    protected static String password="12345";

    public Connection CONN_SQLServer(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy); // thiet lap chinh sac ket noi bao gom tat ca policy
        Connection conn = null;
        try {
            //  Class.forName(classes);
            String conUrl= "jdbc:jtds:sqlserver://"+ ip + ":"+ port+";databasename="+ db;
            conn = DriverManager.getConnection(conUrl,un,password);


        }catch ( SQLException e){
            throw  new RuntimeException(e);
        }
        return conn;
    }



    public Connection CONN_MySQL(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy); // thiet lap chinh sac ket noi bao gom tat ca policy
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String connectionString = "jdbc:mysql://10.0.2.2:3306/SaleManagement";

            conn = DriverManager.getConnection(connectionString,"root","Qwer1234!");


        }catch (SQLException | ClassNotFoundException e){
            throw  new RuntimeException(e);
        }
        return conn;
    }
}
