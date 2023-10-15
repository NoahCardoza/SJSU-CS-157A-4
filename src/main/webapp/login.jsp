<%--
  Created by IntelliJ IDEA.
  User: kalan
  Date: 10/14/2023
  Time: 11:51 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="index.jsp" %>
<%@ page import="java.sql.*" %>
<html>
<head>
    <title>Login Page</title>
</head>
<body>
<hr/>

<h3>Login</h3>
<%
    String profile_msg=(String)request.getAttribute("profile_msg");
    if(profile_msg!=null){
        out.print(profile_msg);
    }
    String login_msg=(String)request.getAttribute("login_msg");
    if(login_msg!=null){
        out.print(login_msg);
    }
%>
<br/>
<form action="loginprocess.jsp" method="post">
    Email:<input type="text" name="email"/><br/><br/>
    Password:<input type="password" name="password"/><br/><br/>
    <input type="submit" value="login"/>
</form>
<%
    String user; // Use your last name as the database name
    user = "root";
    String password = "MySQLPass@710.";
    try {
        java.sql.Connection con;
        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hidden_gems", user,
                password);

        out.println("Initial entries in table \"User\": <br/>");
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM user");
        while (rs.next()) {
            out.println(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getString(3) + "<br/>");
        }
        rs.close();
        stmt.close();
        con.close();
    } catch (SQLException e) {
        out.println("SQLException caught: " + e.getMessage());
    }
%>
</body>
</html>
