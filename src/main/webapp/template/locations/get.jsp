<%--@elvariable id="revisions" type="java.util.List<com.example.demo.beans.entities.Revision>"--%>
<%--@elvariable id="location" type="com.example.demo.beans.entities.Location"--%>
<%--@elvariable id="amenities" type="java.util.List<com.example.demo.beans.entities.AmenityWithImage>"--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="hg" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html>
<head>
    <title>LHG | Location | ${location.name}</title>
    <%@include file="../../includes/head.jsp" %>
</head>
<body>
<%@include file="../../includes/nav.jsp" %>
<div class="container mt-5">
    <div class="d-flex flex-column-reverse flex-sm-row justify-content-sm-between mb-2">
        <div class="mt-3 mt-sm-0">
            <h1 class="mb-3">${location.name}</h1>
            <p>${location.description}</p>
        </div>
        <div class="d-flex flex-column justify-content-center align-items-center">
            <img  class="img-thumbnail" width="200" height="200" src="<c:url value="/locations?f=mapImage&id=${location.id}" />"  alt="Map image"/>

            <div class="btn-group btn-group-sm mt-2">
                <a class="btn btn-primary" href="<c:url value="/amenities?f=create&location_id=${location.id}"/>" title="Create Amenity">
                    <i class="bi bi-plus-circle"></i> Amenity
                </a>
                <a class="btn btn-primary" href="<c:url value="/locations?f=edit&id=${location.id}"/>" title="Edit">
                    <i class="bi bi-pencil-square"></i> Edit
                </a>
                <a class="btn btn-primary" href="<c:url value="/revisions?f=list&type=Location&id=${location.id}"/>" title="Revisions">
                    <i class="bi bi-clock-history"></i> Revisions
                </a>
            </div>
        </div>
    </div>

    <div class="row">
        <c:forEach var="amenity" items="${amenities}">
            <div class="col-12 col-sm-6 col-md-4 g-3">
                <div class="card">
                    <img class="card-img-top" src="<hg:cloudimg value="${amenity.image.url}" width="200" height="200" />" style="height: 200px; width: 100%; object-fit: cover;">
                    <div class="card-body">
                        <h5 class="card-title">${amenity.name}</h5>
                        <p class="card-text">${amenity.description}</p>
                        <a href="<c:url value="/amenities?f=get&id=${amenity.id}"/>" class="btn btn-primary">Select</a>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>
</body>
</html>
