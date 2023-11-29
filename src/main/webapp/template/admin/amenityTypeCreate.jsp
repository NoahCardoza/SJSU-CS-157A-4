<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%--@elvariable id="amenityType" type="com.example.demo.beans.entities.AmenityType"--%>

<!DOCTYPE html>
<html>
<head>
    <title>LHG | Admin | Amenity Type | Create</title>
    <%@include file="../../includes/head.jsp" %>
</head>
<body>
<%@include file="../../includes/nav.jsp" %>
<div class="container mt-5">
    <div class="col-12">
        <h1>Amenity Type Create</h1>
    </div>
    <div class="col-12">
        <%@include file="/includes/alerts.jsp" %>
    </div>
    <div class="col-12">
        <form method="POST" class="mt-5" id="new-location-form">
            <div class="mb-3">
                <label for="name">Name</label>
                <input id="name" name="name" type="text" class="form-control" placeholder="Name"
                       value="${amenityType.name}"/>
            </div>
            <div class="mb-3">
                <label for="description">Description</label>
                <textarea id="description" name="description" type="" class="form-control"
                          placeholder="Description">${amenityType.description}</textarea>
            </div>
            <div class="mb-3">
                <label for="attributes">Attributes</label>
                <textarea id="attributes" name="attributes" type="" class="form-control"
                          placeholder="attributeA:type,attributeB:type,etc."></textarea>
            </div>
            <div class="mb-3">
                <label for="metrics">Metrics</label>
                <textarea id="metrics" name="metrics" type="" class="form-control"
                          placeholder="metricA,metricB,etc."></textarea>
            </div>
            <button type="submit" class="btn btn-primary w-100" name="action" value="submit">Create</button>
        </form>
    </div>
</div>
</body>
</html>
