<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%--@elvariable id="amenityType" type="com.example.demo.beans.entities.AmenityType"--%>

<!DOCTYPE html>
<html>
    <head>
        <title>LHG | Admin | Amenity Type | Edit</title>
        <%@include file="../../includes/head.jsp" %>
    </head>
    <body>
        <%@include file="../../includes/nav.jsp" %>
        <div class="container mt-5 g-3">
            <div class="row">
                <div class="col-12">
                    <h1>Amenity Type Edit</h1>
                </div>
                <div class="col-12">
                    <%@include file="/includes/alerts.jsp" %>
                </div>
                <div class="col-12">
                    <form method="POST">
                        <input name="id" type="hidden" value="${amenityType.id}"/>
                        <input name="parentAmenityTypeId" type="hidden" value="${amenityType.parentAmenityTypeId}"/>

                        <div class="mb-3">
                            <label for="name">Name</label>
                            <input id="name" name="name" type="text" class="form-control" placeholder="Name" value="${amenityType.name}"/>
                        </div>
                        <div class="mb-3">
                            <label for="description">Description</label>
                            <textarea id="description" name="description" type="" class="form-control" placeholder="Description">${amenityType.description}</textarea>
                        </div>

                        <button type="submit" class="btn btn-primary w-100" name="action" value="submit">Update</button>
                    </form>
                </div>
            </div>
        </div>
    </body>
</html>
