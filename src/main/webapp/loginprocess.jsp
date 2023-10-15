<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="com.example.demo.LoginDao"%>

<html>
  <head>
    <title>Login Status</title>
    <%@include file="includes/head.jsp" %>
  </head>
  <body>
    <%@include file="includes/nav.jsp" %>
    <jsp:useBean id="user" class="com.example.demo.bean.LoginBean"/>

    <jsp:setProperty property="*" name="user"/>

    <%
      int userId = LoginDao.validate(user);
      if(userId != 0){
        out.println("You have been successfully logged in");
        session.setAttribute("user_id", Integer.toString(userId));
      }
      else
      {
        out.print("Sorry, email or password error");
      }
    %>
  </body>
</html>
