<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.sql.*" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
</head>
<body>
<h1><%= "Hello World!" %>
</h1>
<br/>
<a href="hello-servlet">Hello Servlet</a>|
<a href="login.jsp">login</a>|
<a href="logout.jsp">logout</a>|
<a href="profile.jsp">profile</a>

<%
    String userId = (String) session.getAttribute("user_id");
    if(userId != null){

    java.sql.Connection con;
        String user; // Use your last name as the database name
        user = "root";
        String password = "MySQLPass@710.";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hidden_gems", user,
                    password);

            PreparedStatement ps=con.prepareStatement(
                    "select email from user where id=?");

            ps.setString(1,userId);


            System.out.println(ps.toString());
            ResultSet rs=ps.executeQuery();
            if (rs.next()){
                 out.println("You are logged in as " + rs.getString(1));
            }

        } catch (SQLException e) {

        }}
%>

</body>
</html>