<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>

<%--@elvariable id="amenityTypes" type="java.util.List<com.example.demo.beans.entities.AmenityType>"--%>

<!DOCTYPE html>
<html>
    <head>
        <title>LHG | Admin</title>
        <%@include file="../../includes/head.jsp" %>
    </head>
    <body>
        <%@include file="../../includes/nav.jsp" %>
        <div class="container mt-5">
            <div class="row g-3">
                <div class="col-12 d-flex justify-content-between">
                    <h2 >Amenity Types</h2>
                    <div>
                        <a href="<c:url value="/admin?f=amenityTypeCreate"/>" class="btn btn-primary ">Create</a>
                    </div>
                </div>
                <div class="col-12">
                    <%@include file="/includes/alerts.jsp" %>
                </div>
                <div class="col-12">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>Name</th>
                                <th>Description</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${amenityTypes}" var="amenityType">
                                <tr>
                                    <td scope="row">${amenityType.name}</td>
                                    <td>${amenityType.description}</td>
                                    <td>
                                        <div class="btn-toolbar">
                                            <div class="btn-group btn-group-sm">
                                                <a href="<c:url value="/admin?f=amenityTypeEdit&id=${amenityType.id}"/>" class="btn btn-primary">Edit</a>
                                                <i></i>
                                            </div>
                                            <form action="<c:url value="/admin?f=amenityTypeDelete"/>" method="post" class="btn-group btn-group-sm">
                                                <input type="hidden" name="id" value="${amenityType.id}">
                                                <i></i>
                                                <button type="submit" class="btn btn-danger">Delete</button>
                                            </form>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </body>
</html>
