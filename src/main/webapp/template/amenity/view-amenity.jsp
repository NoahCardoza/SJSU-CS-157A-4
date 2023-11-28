<%--@elvariable id="revisions" type="java.util.List<com.example.demo.beans.entities.Revision>"--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="hg" tagdir="/WEB-INF/tags" %>
<%--@elvariable id="amenityTypeAttributes" type="com.example.demo.servlets.search.AmenityTypeAttributeGrouper"--%>
<%--@elvariable id="amenityTypes" type="java.util.List<com.example.demo.beans.entities.AmenityType>"--%>
<%--@elvariable id="user" type="com.example.demo.beans.entities.User"--%>
<%--@elvariable id="amenity" type="com.example.demo.beans.entities.Amenity"--%>
<%--@elvariable id="images" type="java.util.List<java.lang.String>"--%>
<!DOCTYPE html>
<html>
    <head>
        <title>LHG | Amenity | ${amenity.name}</title>
        <%@include file="../../includes/head.jsp" %>
    </head>
    <body>
        <%@include file="../../includes/nav.jsp" %>
        <div class="container mt-5" style="max-width: 600px">
            <div class="row">
                <h1 class="mb-2">${amenity.name}</h1>
            </div>
            <%@include file="/includes/alerts.jsp" %>
            <div class="row">
                <div class="w-100 overflow-x-auto mb-3" style="white-space:nowrap">
                    <c:forEach var="imageUrlIndex" begin="${1}" end="${images.size()}">
                        <c:set var="imageUrl" value="${images.get(imageUrlIndex - 1)}" />
                        <img
                                class="d-inline img-thumbnail"
                                src="<hg:cloudimg value="${imageUrl}" size="400" />"
                                onclick="$('#amenity-img-carousel').carousel(${imageUrlIndex - 1}); $('#amenity-img-carousel-modal').modal('show');"
                                alt=""
                                style=" max-height: 100px;">
                    </c:forEach>
                </div>
                <div id="amenity-img-carousel-modal" class="modal" tabindex="-1">
                    <div class="modal-dialog m-0 modal-dialog-centered min-vw-100 vh-100">
                        <div class="modal-content bg-transparent border-0">
                            <div id="amenity-img-carousel" class="carousel slide w-100">
                                <div class="carousel-inner bg-transparent">
                                    <c:forEach var="imageUrlIndex" begin="${1}" end="${images.size()}">
                                        <c:set var="imageUrl" value="${images.get(imageUrlIndex - 1)}" />
                                        <div class="carousel-item ${imageUrlIndex == 1 ? 'active' : ''}" id="review-img-${imageUrlIndex}">
                                            <img class="d-block mx-auto"  style="max-height: calc(100vh - 4rem); max-width: calc(100vw - 12rem);" src="<hg:cloudimg value="${imageUrl}" size="800" />" alt="">
                                        </div>
                                    </c:forEach>
                                </div>
                                <button class="carousel-control-prev" type="button" data-bs-target="#amenity-img-carousel" data-bs-slide="prev">
                                    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                                    <span class="visually-hidden">Previous</span>
                                </button>
                                <button class="carousel-control-next" type="button" data-bs-target="#amenity-img-carousel" data-bs-slide="next">
                                    <span class="carousel-control-next-icon" aria-hidden="true"></span>
                                    <span class="visually-hidden">Next</span>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col mt-2 d-flex justify-content-center">
                <div class="btn-group btn-group-sm">
                <a class="btn btn-secondary" href="<c:url value="/amenities?f=edit&id=${amenity.id}"/>">Edit</a>
                <a class="btn btn-warning" href="<c:url value="/revisions?f=list&type=Amenity&id=${amenity.id}"/>">Revisions</a>
                <a class="btn btn-secondary" href="<c:url value="/reviews?f=create&amenityId=${amenity.id}"/>">Create Review</a>

                <c:if test="${user.administrator}">
                    <a class="btn btn-danger" href="<c:url value="/amenities?f=delete&id=${amenity.id}"/>">Delete</a>
                </c:if>
<%--                <a class="btn btn-secondary" href="<c:url value="/reviews?f=list&id=${amenity.id}"/>">Reviews</a>--%>
                </div>
            </div>
             <div class="col">
                <div>
                <p>${amenityTypeAttributes.textAttributes}</p>
                </div>
            </div>
            <div class="col">
              <p>${amenityTypeAttributes.booleanAttributes}</p>
            </div>
            <div class="col">
              <p>${amenityTypeAttributes.numberAttributes}</p>
            </div>
            <div class="col">
              <p>${amenityTypeMetrics}</p>
            </div>
            <div class="col">
                <div class="row gy-3">
                    <c:forEach var="review" items="${reviews}">
                        <div class="col col-12">
<%--                                ${review}--%>
                                    <hg:reviewCard review="${review}"/>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </body>
    <hg:footer/>
</html>