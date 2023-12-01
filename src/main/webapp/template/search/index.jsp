<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib uri="/WEB-INF/custom-functions.tld" prefix="cfn" %>
<%@ taglib prefix="hg" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="locations" type="List<com.example.demo.daos.LocationDao>"--%>
<%--@elvariable id="amenityTypeAttributes" type="com.example.demo.servlets.search.AmenityTypeAttributeGrouper"--%>
<%--@elvariable id="amenityTypes" type="java.util.List<com.example.demo.beans.entities.AmenityType>"--%>
<%--@elvariable id="amenities" type="java.util.List<com.example.demo.beans.entities.AmenityWithImage>"--%>

<!DOCTYPE html>
<html>
    <head>
        <title>LHG | Search</title>
        <%@include file="../../includes/head.jsp" %>
    </head>
    <body>
        <%@include file="../../includes/nav.jsp" %>
        <div class="p-5 mx-auto" style="max-width: 1200px;">
            <h1 class="mb-2">Search</h1>
            <div class="row gx-3 gy-2">
                <div class="col-12 col-sm-6 col-md-4">
                    <hg:form
                        autoSubmit="${true}"
                        amenityTypes="${amenityTypes}"
                        amenityTypeAttributes="${amenityTypeAttributes}"
                    />
                </div>
                <div class="col-12 col-sm-6 col-md-8" style="">
                    <div class="row g-2">
                        <c:forEach var="amenity" items="${amenities}">
                            <div class="col-12 col-md-6 col-lg-4">
                                <div class="card">
                                    <img class="card-img-top" src="<hg:cloudimg value="${amenity.image.url}" width="200" height="200" />" style="height: 200px; width: 100%; object-fit: cover;" alt="">
                                    <div class="card-body">
                                        <h5 class="card-title">${amenity.name}</h5>
                                        <p class="card-text">${amenity.description}</p>
                                        <a type="select" href="<c:url value="/amenities?f=get&id=${amenity.id}"/>" class="btn btn-primary">Select</a>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
        <hg:footer />
    </body>
</html>
