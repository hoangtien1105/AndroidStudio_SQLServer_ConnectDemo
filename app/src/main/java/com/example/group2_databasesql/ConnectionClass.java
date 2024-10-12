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

    public Connection CONN(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        // lay tat ca quyen
        StrictMode.setThreadPolicy(policy); // thiet lap chinh sac ket noi bao gom tat ca policy
        Connection conn = null;
        try {
            Class.forName(classes);
           // String conUrl= "jdbc:jtds:sqlserver://"+ ip + ":"+ port+";"+ "databasename="+ db+";user="+un+";password="+password+";";
            String conUrl= "jdbc:jtds:sqlserver://"+ ip + ":"+ port+";"+ db;
            conn = DriverManager.getConnection(conUrl,un,password);


        }catch (ClassNotFoundException| SQLException e){
            throw  new RuntimeException(e);
        }
        return conn;
    }

}
