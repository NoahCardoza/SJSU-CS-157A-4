<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.example.demo.UtilsTesting223" %>
<%@ page import="com.example.demo.Database" %>
<%@ page import="com.example.demo.IPApiXMLParser" %>


<html>
    <head>
        <title>Three Tier Architecture Demo</title>
        <%@include file="includes/head.jsp" %>
    </head>
    <body>
        <h1>Locations</h1>
        <table class="table">
            <tr>
                <td>ID</td>
                <td>Name</td>
                <td>Address</td>
                <td>Description</td>
                <td>Long</td>
                <td>Lat</td>
            </tr>
            <%
                var db = new Database();

                if (db.conn != null) {
                    Statement stmt = db.conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT id, name, address, description, longitude, latitude FROM Location");
                    while (rs.next()) {
                        out.println("<tr><th scope=\"row\">" + rs.getInt(1) + "</th><td>" + rs.getString(2) + "</td><td>" + rs.getString(3) + "</td><td>" + rs.getString(4) + "</td><td>" + rs.getDouble(5) + "</td><td>" + rs.getDouble(6) + "</td></tr>");
                    }
                    rs.close();
                    stmt.close();
                    db.conn.close();
                }
            %>
        </table>
    </body>
</html>
