<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%--@elvariable id="metrics" type="java.util.List<com.example.demo.beans.entities.AmenityTypeMetric>"--%>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Reviews</title>
    <%@include file="../../includes/head.jsp" %>
</head>
<body>
<%@include file="../../includes/nav.jsp" %>
<div class="container mt-5">
    <div class="panel-primary">
        <div class="panel-heading">
            <h1 class="panel-title">COMMENT BOX USING AJAX THROUGH JSP</h1>
        </div>
        <form method="post">
            <%@include file="../../includes/alerts.jsp" %>

            <div class="row">
                <div class="form-group col-md-6">
                    <div class="form-group">
                        <label for="username">Title</label>
                        <input class ="form-control" type="text" id="username" name="title">
                    </div>
                    <div class="clearfix"></div>
                    <div class="form-group">
                        <label for="comment">Review</label>
                        <textarea class="form-control" rows="8" id="comment" required="required" name="description"></textarea>
                    </div>
                    <p>Metrics</p>
                </div>
                <div class="form-group col-md-6">
                    <c:forEach var="metric" items="${metrics}">
                        <div class="form-group">
                            <label for="metric-${metric.id}">${metric.name}</label>
                            <input class="form-control" type="text" id="metric-${metric.id}" name="metric-${metric.id}" placeholder="Rate 0 to 5" />
                        </div>
                    </c:forEach>
                </div>
                <div class="clearfix"></div>
                <div class="form-group mt-3">
                    <button class="btn btn-primary" type="submit">Create</button>
                </div>
            </div>
        </form>
    </div>
</div>
</body>
</html>

