<%--@elvariable id="revisions" type="java.util.List<com.example.demo.beans.entities.Revision>"--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--@elvariable id="amenityTypeAttributes" type="com.example.demo.servlets.search.AmenityTypeAttributeGrouper"--%>
<%--@elvariable id="amenityTypes" type="java.util.List<com.example.demo.beans.entities.AmenityType>"--%>

<!DOCTYPE html>
<html>
    <head>
        <title>LHG | Amenity | ${amenity.name}</title>
        <%@include file="../../includes/head.jsp" %>
    </head>
    <body>
        <%@include file="../../includes/nav.jsp" %>
        <div class="container mt-5">
            <h1 class="mb-2">${amenity.name}</h1>
            </br>
            <a class="btn btn-secondary" href="<c:url value="/amenities?f=edit&id=${amenity.id}"/>">Edit</a>
            <a class="btn btn-warning" href="<c:url value="/revisions?f=list&type=Location&id=${location.id}"/>">Revision</a>
            <a class = "btn btn-secondary" href="<c:url value="/reviews?f=create&amenityId=${amenity.id}"/>">Create Review</a>

            <a class="btn btn-danger" href="<c:url value="/amenities?f=delete&id=${amenity.id}"/>">Delete</a>

            <a class = "btn btn-secondary" href="<c:url value="/reviews?f=list&id=${amenity.id}"/>">Reviews</a>


             </br>
             </br>
             <c:forEach var="image" items="${images}">
              <div class="col-4 mb-4">
                 <div class="card">
                   <img class="card-img-top" src="${image}" style="height: 200px; width: 100%; object-fit: cover;">
                 </div>
              </div>
            </c:forEach>
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
              <p>${reviews}</p>
            </div>
        </div>
    </body>
</html>