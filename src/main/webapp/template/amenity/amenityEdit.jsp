<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib uri="/WEB-INF/custom-functions.tld" prefix="cfn" %>
<%@ taglib prefix="hg" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="form" type="List<com.example.demo.beans.forms.AmenityForm>"--%>
<%--@elvariable id="locations" type="List<com.example.demo.daos.LocationDao>"--%>
<%--@elvariable id="amenityTypeAttributes" type="com.example.demo.servlets.search.AmenityTypeAttributeGrouper"--%>

<!DOCTYPE html>
<html>
    <head>
        <title>LHG | Amenity | Edit</title>
        <%@include file="../../includes/head.jsp" %>
    </head>
    <body>
        <%@include file="../../includes/nav.jsp" %>
        <div class="container mt-5">
            <h1>Edit Amenity</h1>

           <form method="POST" class="mt-5" id="new-location-form">
                <div class="row">
                    <%@include file="../../includes/alerts.jsp" %>
                    <div class="col mb-3 d-flex justify-content-between align-items-center">
                            <p>Location: <b> ${form.locationName} </b> </p>
                    </div>
                </div>

                <div class="row">
                    <%@include file="../../includes/alerts.jsp" %>
                    <div class="col mb-3 d-flex justify-content-between align-items-center">
                            <p>Amenity Type: <b> ${form.typeName} </b></p>
                    </div>
                </div>

                <input type="hidden" name="locationId" id="locationId" value="${form.locationId}">
                <input type="hidden" name="locationName" id="locationName" value="${form.locationName}">
                <input type="hidden" name="typeId" id="typeId" value="${form.typeId}">
                <input type="hidden" name="typeName" id="typeName" value="${form.typeName}">
                <input type="hidden" name="amenityTypeAttributes" id="amenityTypeAttributes" value="${form.attributes}">

                <div class="form-group col-md-6 mb-3">
                    <p>Attributes</p>
                    <c:forEach var="amenityAttributeWithName" items="${amenityAttributesWithNames}">
                        <div class="form-group">
                            <label for="amenityTypeAttribute-${amenityAttributeWithName.amenityAttributeId}">${amenityAttributeWithName.name}</label>
                            <input class="form-control" type="text" id="amenityTypeAttribute-${amenityAttributeWithName.amenityAttributeId}" name="amenityTypeAttribute-${amenityAttributeWithName.amenityAttributeId}" value="${amenityAttributeWithName.value}"/>
                        </div>
                    </c:forEach>
                </div>

                <label for="name" class="form-label">Name</label>
                <input type="text" id="name" name="name" value="${form.name}" class="form-control mb-3" placeholder="Name" />
                <label for="description" class="form-label">Description</label>
                <textarea id="description" name="description" class="form-control mb-3" placeholder="Description">${form.description}</textarea>
                <input type="hidden" name="redirect" value="${pathWithQueryString}">
                <button type="submit" class="btn btn-primary w-100" name="action" value="submit">Create</button>
        </div>
    </body>
</html>
