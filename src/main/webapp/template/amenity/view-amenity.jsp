<%--@elvariable id="revisions" type="java.util.List<com.example.demo.beans.entities.Revision>"--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="hg" tagdir="/WEB-INF/tags" %>
<%--@elvariable id="amenityTypeAttributes" type="com.example.demo.servlets.amenities.AttributeGrouper"--%>
<%--@elvariable id="amenityTypes" type="java.util.List<com.example.demo.beans.entities.AmenityType>"--%>
<%--@elvariable id="user" type="com.example.demo.beans.entities.User"--%>
<%--@elvariable id="amenity" type="com.example.demo.beans.entities.Amenity"--%>
<%--@elvariable id="images" type="java.util.List<java.lang.String>"--%>
<%--@elvariable id="recordedMetrics" type="java.util.List<com.example.demo.beans.entities.AmenityTypeMetricRecordAveragesWithName>"--%>
<%--@elvariable id="reviews" type="java.util.List<com.example.demo.beans.entities.Review>"--%>

<!DOCTYPE html>
<html>
    <head>
        <title>LHG | Amenity | ${amenity.name}</title>
        <%@include file="../../includes/head.jsp" %>
    </head>
    <body>
        <%@include file="../../includes/nav.jsp" %>
        <div class="container mt-5" style="max-width: 800px">
            <div class="row g-3">
                <div class="col-12">
                    <%@include file="/includes/alerts.jsp" %>
                </div>
                <div class="col-12">
                    <h1>${amenity.name}</h1>
                </div>
                <c:if test="${recordedMetrics.size() > 0}">
                    <div class="col-12">
                        <div class="d-flex justify-content-around flex-wrap ">
                            <c:forEach var="metric" items="${recordedMetrics}">
                                <c:if test="${not empty metric.value}">
                                    <div class="mx-2">
                                        <label class="form-label" for="attribute-${metric.amenityTypeMetricId}" >${metric.name}</label>
                                        <c:choose>
                                            <c:when test="${not empty metric.value}">
                                                <hg:rating value="${metric.value}" />
                                            </c:when>
                                            <c:otherwise>
                                                <input type="text" class="form-control form-control-sm" value="N/A" disabled id="attribute-${metric.amenityTypeMetricId}">
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </c:if>
                            </c:forEach>
                        </div>
                        <div class="col-12">
                            <hr/>
                        </div>
                    </div>
                </c:if>
                <div class="col-12">
                    <div class="w-100 overflow-x-auto" style="white-space:nowrap">
                        <c:forEach var="imageUrlIndex" begin="${1}" end="${images.size()}">
                            <c:set var="imageUrl" value="${images.get(imageUrlIndex - 1)}" />
                            <img
                                    class="d-inline img-thumbnail"
                                    src="<hg:cloudimg value="${imageUrl}" width="400" height="400" />"
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
                                                <img class="d-block mx-auto"  style="max-height: calc(100vh - 4rem); max-width: calc(100vw - 12rem);" src="<hg:cloudimg value="${imageUrl}" func="fit" width="800" height="800" />" alt="">
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
            </div>
            <div class="row flex-md-row-reverse mt-3 g-3">
                <div class="col-12 col-md-4">
                    <div class="row gy-2 justify-content-center">
                    <div class="col-12 d-flex justify-content-center">
                        <div class="btn-group btn-group-sm">
                            <a class="btn btn-primary" href="<c:url value="/amenities?f=edit&id=${amenity.id}"/>" title="Edit">
                                <i class="bi bi-pencil-square"></i> Edit
                            </a>
                            <a class="btn btn-primary" href="<c:url value="/revisions?f=list&type=Amenity&id=${amenity.id}"/>" title="Revisions">
                                <i class="bi bi-clock-history"></i> Revisions
                            </a>
                            <a class="btn btn-primary" href="<c:url value="/reviews?f=create&amenityId=${amenity.id}"/>" title="Create Review">
                                <i class="bi bi-plus-circle"></i> Review
                            </a>
                            <%--                TODO: implement amenity delete if time permits, we need to deal with a lot of constrains    --%>
                            <%--                <c:if test="${user.administrator}">--%>
                            <%--                    <a class="btn btn-danger" href="<c:url value="/amenities?f=delete&id=${amenity.id}"/>">Delete</a>--%>
                            <%--                </c:if>--%>
                        </div>
                    </div>
                    <c:if test="${amenityTypeAttributes.booleanAttributes.size() > 0}">
                        <div class="col-12">
                            <hr/>
                        </div>
                                <c:forEach var="attribute" items="${amenityTypeAttributes.booleanAttributes}">
                                    <div>
                                        <span class="badge rounded-pill text-bg-light">
                                            <c:choose>
                                                <c:when test="${attribute.value == 'T'}">
                                                    <i class="bi bi-check2-circle text-success"></i>
                                                </c:when>
                                                <c:when test="${attribute.value == 'F'}">
                                                    <i class="bi bi-x-circle text-danger"></i>
                                                </c:when>
                                                <c:otherwise>
                                                    <i class="bi bi-question-circle text-warning"></i>
                                                </c:otherwise>
                                            </c:choose>
                                            <span class="ms-2">${attribute.name}</span>
                                        </span>
                                    </div>
                                </c:forEach>
                        <div class="col-12">
                            <hr/>
                        </div>
                    </c:if>


                    <c:if test="${amenityTypeAttributes.numberAttributes.size() > 0}">
                         <c:forEach var="attribute" items="${amenityTypeAttributes.numberAttributes}">
                                <div class="col-12">
                                    <div>
                                        <label class="form-label" for="attribute-${attribute.attributeId}">${attribute.name}</label>
                                         <c:choose>
                                            <c:when test="${not empty attribute.value}">
                                                <input type="number" class="form-control form-control-sm" value="${attribute.value}" disabled id="attribute-${attribute.attributeId}">
                                            </c:when>
                                            <c:otherwise>
                                                <input type="text" class="form-control form-control-sm" value="N/A" disabled id="attribute-${attribute.attributeId}">
                                            </c:otherwise>
                                         </c:choose>
                                    </div>
                                </div>
                        </c:forEach>
                        <div class="col-12">
                            <hr/>
                        </div>
                    </c:if>
                    <c:if test="${amenityTypeAttributes.textAttributes.size() > 0}">
                        <c:forEach var="attribute" items="${amenityTypeAttributes.textAttributes}">
                            <div class="col-12">
                                <div>
                                    <label class="form-label" for="attribute-${attribute.attributeId}" >${attribute.name}</label>
                                    <c:choose>
                                        <c:when test="${not empty attribute.value}">
                                            <input type="text" class="form-control form-control-sm" value="${attribute.value}" disabled id="attribute-${attribute.attributeId}">
                                        </c:when>
                                        <c:otherwise>
                                            <input type="text" class="form-control form-control-sm" value="N/A" disabled id="attribute-${attribute.attributeId}">
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </c:forEach>
                    </c:if>
                </div>
                </div>
                <div class="col-12 col-md-8">
                    <c:if test="${reviews.size() > 0}">
                        <div class="row g-3">
                            <c:forEach var="review" items="${reviews}">
                                <div class="col-12">
                                    <hg:reviewCard review="${review}"/>
                                </div>
                            </c:forEach>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </body>
    <hg:footer/>
</html>