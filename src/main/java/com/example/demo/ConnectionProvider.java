package com.example.demo;
import java.sql.*;
import static com.example.demo.Provider.*;
public class ConnectionProvider {

    private static Connection con=null;
    static{
        try{
            Class.forName(DRIVER);
            con=DriverManager.getConnection(CONNECTION_URL,USERNAME,PASSWORD);
        }catch(Exception e){}
    }

    public static Connection getCon(){
        return con;
    }
}
