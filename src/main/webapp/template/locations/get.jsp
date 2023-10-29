<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Locations</title>
        <%@include file="../../includes/head.jsp" %>
    </head>
    <body>
        <%@include file="../../includes/nav.jsp" %>
        <div class="container mt-5">
            <h1 class="mb-2">${location.name}</h1>
            <div class="col">
<%--                <img width="200" height="200"--%>
<%--                     src="https://maps.geoapify.com/v1/staticmap?style=osm-carto&width=200&height=200&center=lonlat:${location.latitude},${location.longitude}&marker=lonlat:${location.latitude},${location.longitude};color:%23ff0000;size:medium&zoom=14&apiKey=108efff5666c4ffd96cdb62b1d775d13">--%>
                    <img width="200" height="200" src="/locations?f=map&id=${location.id}">
                <div>
                    <p>${location.description}</p>
                </div>
            </div>
            <a class="btn btn-secondary" href="/locations?f=edit&id=${location.id}">Edit</a>
            <div class="row">
            <div class="col-12" style="">
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
