<%--
  Created by IntelliJ IDEA.
  User: kalan
  Date: 10/14/2023
  Time: 11:51 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.example.demo.Database" %>
<html>
<head>
    <title>Login Page</title>
    <%@include file="includes/head.jsp" %>
</head>
<body>
<%@include file="includes/nav.jsp" %>

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
    try {
        Connection conn = Database.getConnection();

        if (conn != null) {
            out.println("Initial entries in table \"User\": <br/>");

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM user");
            while (rs.next()) {
                out.println(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getString(3) + "<br/>");
            }
            rs.close();
            stmt.close();
            conn.close();
        }
    } catch (SQLException e) {
        out.println("SQLException caught: " + e.getMessage());
    }
%>
</body>
</html>
