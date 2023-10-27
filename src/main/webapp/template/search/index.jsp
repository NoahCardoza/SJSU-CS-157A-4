<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib uri="/WEB-INF/custom-functions.tld" prefix="cfn" %>

<%--@elvariable id="locations" type="List<com.example.demo.daos.LocationDao>"--%>
<%--@elvariable id="amenityTypeAttributes" type="com.example.demo.servlets.search.AmenityTypeAttributeGrouper"--%>

<!DOCTYPE html>
<html>
    <head>
        <title>Locations</title>
        <%@include file="../../includes/head.jsp" %>
    </head>
    <body>
        <%@include file="../../includes/nav.jsp" %>
        <div class="container mt-5">
            <h1 class="mb-2">Search</h1>
            <div class="row">
            <form id="search-form" class="col-4" onsubmit="if ($('#amenityType').val() === '0') {
                location.href = '/search';
                return false;
                }">
                <div class="row mb-2">
                    <label for="amenityType" class="form-label">Amenity Type</label>
                    <select class="form-select" name="amenityTypeId" id="amenityType" onchange="$('#search-form').submit()">
                        <option value="0">All</option>
                        <c:forEach var="amenityType" items="${amenityTypes}">
                            <option ${param.get('amenityTypeId') == amenityType.id ? 'selected' : ''} value="${amenityType.id}"
                            >${amenityType.name}</option>
                        </c:forEach>
                    </select>
                </div>

                <c:if test="${not empty amenityTypeAttributes}">
                    ${amenityTypeAttributes.textAttributes}
                    ${amenityTypeAttributes.booleanAttributes}
                    ${amenityTypeAttributes.numberAttributes}
                </c:if>
                <div class="row">
                    <button type="submit" class="btn btn-primary">Search</button>
                </div>
            </form>

            <div class="col-8" style="">
                <div class="row">
                <c:forEach var="amenity" items="${amenities}">
                    <div class="col-4 mb-4">
                        <div class="card">
                            <img class="card-img-top" src="${amenity.image.url}" style="height: 200px; width: 100%; object-fit: cover;">
                            <div class="card-body">
                                <h5 class="card-title">${amenity.name}</h5>
                                <p class="card-text">${amenity.description}</p>
                                <a href="/amenities?id=f=get&id=${amenity.id}" class="btn btn-primary">Select</a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
                    <div>
            </div>
                </div>
        </div>
    </body>
</html>
