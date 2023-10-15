<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.example.demo.Database" %>
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
        Connection conn = Database.getConnection();

        if (conn != null) {
            try {
                PreparedStatement ps=conn.prepareStatement("select email from user where id=?");

                ps.setString(1,userId);

                ResultSet rs=ps.executeQuery();

                if (rs.next()){
                     out.println("You are logged in as " + rs.getString(1));
                }

            } catch (SQLException ignore) {}
        }
    }
%>

</body>
</html>