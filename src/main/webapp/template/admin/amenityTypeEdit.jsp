<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%--@elvariable id="amenityType" type="com.example.demo.beans.entities.AmenityType"--%>

<!DOCTYPE html>
<html>
    <head>
        <title>Locations</title>
        <%@include file="../../includes/head.jsp" %>
    </head>
    <body>
        <%@include file="../../includes/nav.jsp" %>
        <div class="container mt-5">
            <h1>Amenity Type Edit</h1>

            <form method="POST" class="mt-5" id="new-location-form">
                <input name="id" type="hidden" value="${amenityType.id}"/>
                <input name="parentAmenityTypeId" type="hidden" value="${amenityType.parentAmenityTypeId}"/>
                <label for="name">Name</label>
                <input id="name" name="name" type="text" class="form-control" placeholder="Name" value="${amenityType.name}"/>
                <label for="description">Description</label>
                <textarea id="description" name="description" type="" class="form-control" placeholder="Description">${amenityType.description}</textarea>
                <label for="icon">Icon</label>
                <input id="icon" name="icon" type="text" class="form-control" placeholder="Icon" value="${amenityType.icon}"/>
                <button type="submit" class="btn btn-primary w-100" name="action" value="submit">Update</button>
            </form>
        </div>
    </body>
</html>
