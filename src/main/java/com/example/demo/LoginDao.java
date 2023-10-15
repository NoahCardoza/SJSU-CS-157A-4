package com.example.demo;
import com.example.demo.bean.LoginBean;

import java.sql.*;
public class LoginDao {

    public static int validate(LoginBean bean){
        Connection conn = Database.getConnection();

        if (conn == null) {
            return 0;
        }

        try {
            PreparedStatement ps = conn.prepareStatement("select id from user where email=? and password=?");

            ps.setString(1,bean.getEmail());
            ps.setString(2, bean.getPassword());

            ResultSet rs = ps.executeQuery();

            if (rs.next()){
                return rs.getInt(1);
            }

        } catch (SQLException ignored) {}

        return 0;

    }
}
