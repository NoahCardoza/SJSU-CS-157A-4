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
<h1>Amenity Reviews</h1>
<c:forEach var="review" items="${reviews}">
    <div>
        <h3>${review.name}</h3>
        <p>${review.description}</p>
   <a class = "btn btn-secondary" href="<c:url value="/reviews?f=get&id=${review.id}"/>">Reviews</a>
    <a class = "btn btn-secondary" href="<c:url value="/reviews?f=hide&id=${review.id}"/>">Hide</a>
        <a class = "btn btn-secondary" href="<c:url value="/reviews?f=delete&id=${review.id}"/>">Delete</a>

    <div class="col">
        <p>${reviews}</p>
    </div>
    </div>
</c:forEach>

</body>
</html>
