<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib uri="/WEB-INF/custom-functions.tld" prefix="cfn" %>

<%--@elvariable id="form" type="List<com.example.demo.beans.forms.AmenityForm>"--%>
<%--@elvariable id="locations" type="List<com.example.demo.daos.LocationDao>"--%>
<%--@elvariable id="amenityTypeAttributes" type="com.example.demo.servlets.search.AmenityTypeAttributeGrouper"--%>

<!DOCTYPE html>
<html>
    <head>
        <title>Amenity</title>
        <%@include file="../../includes/head.jsp" %>
    </head>
    <body>
        <%@include file="../../includes/nav.jsp" %>
        <div class="container mt-5">
            <h1>Add an Amenity</h1>

            <form method="POST" class="mt-5" id="amenity-form" class="col-4" onsubmit="if ($('#amenityType').val() === '0') {
                location.href = '/amenities?f=create';
                return false;
                }">

                <div class="row mb-2">
                    <label for="location" class="form-label">Location</label>
                    <select class="form-select" name="locationID" id="location">
                        <option value="0">All</option>
                        <c:forEach var="location" items="${locations}">
                            <option ${param.get('locationID') == amenityType.id ? 'selected' : ''} value="${location.id}"
                            >${location.name}</option>
                        </c:forEach>
                    </select>
                </div>

                 </br>

                <div class="row mb-2">
                    <label for="amenityType" class="form-label">Amenity Type</label>
                    <select class="form-select" name="amenityTypeId" id="amenityType">
                        <option value="0">All</option>
                        <c:forEach var="amenityType" items="${amenityTypes}">
                            <option ${param.get('amenityTypeId') == amenityType.id ? 'selected' : ''} value="${amenityType.id}"
                            >${amenityType.name}</option>
                        </c:forEach>
                    </select>
                </div>

                <!-- CURRENTLY NOT SHOWING UP -->
                <c:if test="${not empty amenityTypeAttributes}">
                    ${amenityTypeAttributes.textAttributes}
                    ${amenityTypeAttributes.booleanAttributes}
                    ${amenityTypeAttributes.numberAttributes}
                </c:if>

                <input type="text" name="name" value="${form.name}" class="form-control mt-5 mb-3" placeholder="Name" />
                <textarea name="description" class="form-control mb-3" placeholder="Description">${form.description}</textarea>

                <button type="submit" class="btn btn-primary w-100" name="action" value="submit">Create</button>
            </form>
        </div>
    </body>
</html>
