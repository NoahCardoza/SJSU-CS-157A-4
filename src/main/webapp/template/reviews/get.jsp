<%--
  Created by IntelliJ IDEA.
  User: kalan
  Date: 11/24/2023
  Time: 5:03 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%--@elvariable id="metrics" type="java.util.List<com.example.demo.beans.entities.AmenityTypeMetric>"--%>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>LHG | Review | post</title>
    <%@include file="../../includes/head.jsp" %>
</head>
<body>
<%@include file="../../includes/nav.jsp" %>
<c:forEach var="image" items="${images}">
    <div class="col-4 mb-4">
        <div class="card">
            <img class="card-img-top" src="${image}" style="height: 200px; width: 100%; object-fit: cover;">
        </div>
    </div>
</c:forEach>
${review}
${metrics}
<div class="col">
    <div>
        <p>${amenityTypeAttributes.textAttributes}</p>
    </div>
</div>
<div class="col">
    <p>${amenityTypeAttributes.booleanAttributes}</p>
</div>
<div class="col">
    <p>${amenityTypeAttributes.numberAttributes}</p>
</div>
<div class="col">
    <p>${amenityTypeMetrics}</p>
</div>
<div class="col">
    <p>${review}</p>
</div>
</body>
</html>