<%--@elvariable id="revisions" type="java.util.List<com.example.demo.beans.entities.Revision>"--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--@elvariable id="amenityTypeAttributes" type="com.example.demo.servlets.search.AmenityTypeAttributeGrouper"--%>

<!DOCTYPE html>
<html>
    <head>
        <title>Amenities</title>
        <%@include file="../../includes/head.jsp" %>
    </head>
    <body>
        <%@include file="../../includes/nav.jsp" %>
        <div class="container mt-5">
            <h1 class="mb-2">${amenity.name}</h1>
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
            <a class="btn btn-secondary" href="/amenities?f=edit&id=${location.id}">Edit</a>
            <a class="btn btn-warning" href="/revisions?f=list&type=Location&id=${location.id}">Revision</a>
        </div>
    </body>
</html>