<%--
  Created by IntelliJ IDEA.
  User: kalan
  Date: 10/14/2023
  Time: 7:47 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Logout</title>
</head>
<body>

<% session.invalidate();
response.sendRedirect("index.jsp");%>

</body>
</html>
