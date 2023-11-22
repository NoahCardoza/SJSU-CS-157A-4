<%--@elvariable id="revisions" type="java.util.List<com.example.demo.beans.entities.Revision>"--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>

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
        <img width="200" height="200" src="<c:url value="/locations?f=mapImage&id=${location.id}" />"  alt="Map image"/>
    </div>
    <div class="col">
        <p>${location.description}</p>
    </div>
    <a class="btn btn-warning" href="<c:url value="/revisions?f=list&type=Location&id=${location.id}"/>">Revisions</a>
    <a class="btn btn-secondary" href="<c:url value="/locations?f=edit&id=${location.id}"/>">Edit</a>

    <div class="row">
        <c:forEach var="amenity" items="${amenities}">
            <div class="col-4">
                <div class="card">
                    <img class="card-img-top" src="${amenity.image.url}" style="height: 200px; width: 100%; object-fit: cover;">
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
