<%--
  Created by IntelliJ IDEA.
  User: kalan
  Date: 10/31/2023
  Time: 7:25 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" import ="java.sql.*"%>
<%@ page import="com.example.demo.Database" %>
<%@ page import="com.example.demo.beans.entities.User" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>AJAX REQUEST</title>
    <link rel="stylesheet" href="css/bootstrap.css">
</head>
<body>
<div class="container-fluid">
    <%
        String user_Id =request.getParameter("user_id");
        String description =request.getParameter("description");
    %>
    <div class="panel-primary">
        <div class="panel-heading">
            <h4 class="panel-title">Reviews</h4>
        </div>
        <div class="panel-body">
            <%
                String message="";
                try {
                    Connection conn = Database.getConnection();
                    PreparedStatement ps = conn.prepareStatement("SELECT id FROM reviews WHERE user_id=? AND descirption=?");
                    ps.setString(1, request.getAttribute());
                    User user = (User) request.getAttribute("user");
                    ps.setString(2, getDescription());
                    ps.executeUpdate();
            %>
            <strong><big>USER NAME IS : <%=username %><br>
                YOUR COMMENT :<%=description %>
            </strong>
            <%con.close();
            }catch (Exception e) {
                System.out.println(e);
            }
            %>
        </div>
    </div>
</div>
<script src="js/jquery-3.1.1.min.js"></script>
<script src="js/bootstrap.js"></script>
</body>
</html>
