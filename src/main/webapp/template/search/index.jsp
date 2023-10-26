<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%--@elvariable id="locations" type="List<com.example.demo.daos.LocationDao>"--%>

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
            <form class="col-4">
                <div class="row mb-2">
                    <label for="amenityType" class="form-label">Amenity Type</label>
                    <select class="form-select" name="amenityTypeId" id="amenityType">
                        <c:forEach var="amenityType" items="${amenityTypes}">

                            <option
                                    ${param.get('amenityTypeId') == amenityType.id ? 'selected' : ''}
                                    value="${amenityType.id}"
                            >${amenityType.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="row mb-2">
                    <label for="amenityTypeAttribute" class="form-label">Amenity Type</label>
                    <select class="form-select" name="amenityTypeAttributeId" id="amenityTypeAttribute">
                        <c:forEach var="amenityTypeAttribute" items="${amenityTypeAttributes}">

                            <option
                                ${param.get('amenityTypeAttributeId') == amenityTypeAttribute.id ? 'selected' : ''}
                                    value="${amenityTypeAttribute.id}"
                            >${amenityTypeAttribute.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="row">
                    <button type="submit" class="btn btn-primary">Search</button>
                </div>
            </form>

            <div class="col-8" style="">
                <div class="row">
                <c:forEach var="amenity" items="${amenities}">
                    <div class="col-4">
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
