<%--
  Created by IntelliJ IDEA.
  User: kalan
  Date: 10/14/2023
  Time: 12:43 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="com.example.demo.LoginDao"%>

<html>

<jsp:useBean id="user" class="com.example.demo.LoginBean"/>

<jsp:setProperty property="*" name="user"/>

<%
  int userId=LoginDao.validate(user);
  if(userId != 0){
    out.println("You have been successfully logged in");
    session.setAttribute("user_id", Integer.toString(userId));
  }
  else
  {
    out.print("Sorry, email or password error");
%>



<jsp:include page="index.jsp"></jsp:include>
<%
  }
%>
<a href = "index.jsp">Home</a>
</html>
