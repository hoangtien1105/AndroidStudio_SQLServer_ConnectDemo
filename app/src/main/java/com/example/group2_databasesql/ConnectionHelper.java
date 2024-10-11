package com.example.group2_databasesql;

import android.os.StrictMode;

import java.sql.Connection;

public class ConnectionHelper {
    Connection con;
    String uname, pass, ip, port, database;
    public Connection connectionclass(){
        ip = "127.0.0.1";
        database = "SaleManagement";
        uname="sa";
        pass="1245";
        port ="1433";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        // lay tat ca quyen
        StrictMode.setThreadPolicy(policy); // thiet lap chinh sac ket noi bao gom tat ca policy
        Connection connection =null;
        String ConnectionUrl = null;

        try {

        }catch (Exception e){

        }

        return connection;
    }
}
