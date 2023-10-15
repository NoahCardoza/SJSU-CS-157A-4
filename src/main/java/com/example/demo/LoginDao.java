package com.example.demo;
import java.sql.*;
public class LoginDao {

    public static int validate(LoginBean bean){
        java.sql.Connection con;
        try{
            String user; // Use your last name as the database name
            user = "root";
            String password = "MySQLPass@710.";
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hidden_gems", user,
                        password);

                PreparedStatement ps=con.prepareStatement(
                        "select id from user where email=? and password=?");

                ps.setString(1,bean.getEmail());
                ps.setString(2, bean.getPassword());

                System.out.println(ps.toString());
                ResultSet rs=ps.executeQuery();
                if (rs.next()){
                    return rs.getInt(1);
                }

            } catch (SQLException e) {

            }



        }catch(Exception e){}

        return 0;

    }
}
