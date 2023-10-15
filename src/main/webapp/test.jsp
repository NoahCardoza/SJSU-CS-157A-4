<%--
  Created by IntelliJ IDEA.
  User: vulcan
  Date: 9/6/23
  Time: 6:52 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*" %>
<html>
<head>
    <title>Three Tier Architecture Demo</title>
</head>
<body>
<h1>JDBC Connection Example</h1>
<table border="5">
    <tr>
        <td> SJSU ID</td>
        <td> Name</td>
        <td> Major</td>
    </tr>
</table>
<%
    String db = "cardoza";
    String user; // Use your last name as the database name
    user = "root";
    String password = "Fx.u2K_A4wq2MXoABzAU";
    try {
        java.sql.Connection con;
        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + db, user,
                password);

        out.println(db + " database successfully opened.<br/><br/>");
        out.println("Initial entries in table \"Student\": <br/>");
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Student");
        while (rs.next()) {
            out.println(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getString(3) + "<br/>");
        }
        rs.close();
        stmt.close();
        con.close();
    } catch (SQLException e) {
        out.println("\nSQLException caught: " + e.getMessage());
    }
%>
</body>
</html>
