<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="hg" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>

<%--@elvariable id="metrics" type="java.util.List<com.example.demo.beans.entities.AmenityTypeMetric>"--%>
<%--@elvariable id="reviews" type="java.util.List<com.example.demo.beans.entities.Review>"--%>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>LHG | Reviews</title>
    <%@include file="../../includes/head.jsp" %>
</head>
<body>
<%@include file="../../includes/nav.jsp" %>
    <div class="container mt-5 mx-auto" style="max-width: 600px">
        <h1 class="mb-3">Reviews</h1>
        <%@include file="/includes/alerts.jsp" %>
        <div class="overflow-hidden">
            <div class="row gy-3">
            <c:forEach var="review" items="${reviews}">
                    <div class="col col-12">
                        <hg:reviewCard review="${review}"/>
                    </div>
            </c:forEach>
            </div>
        </div>
    </div>
    <hg:footer/>
</body>
</html>
