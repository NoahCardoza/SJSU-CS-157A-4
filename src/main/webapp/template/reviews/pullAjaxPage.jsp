<%--
  Created by IntelliJ IDEA.
  User: kalan
  Date: 10/31/2023
  Time: 7:25 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" import ="java.sql.*"%>
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
        String username =request.getParameter("username");
        String comment =request.getParameter("description");
    %>
    <div class="panel-primary">
        <div class="panel-heading">
            <h4 class="panel-title">Reviews</h4>
        </div>
        <div class="panel-body">
            <%
                String message="";
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/MYDB","root","new_password");
                    PreparedStatement ps = con.prepareStatement("INSERT INTO reviews VALUES(?,?);");
                    ps.setString(1, username);
                    ps.setString(2, description);
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
