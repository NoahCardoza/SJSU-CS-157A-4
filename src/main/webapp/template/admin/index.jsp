<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

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
            <div class="row">
                <div class="col d-flex justify-content-between">
                    <h2 >Amenity Types</h2>
                    <div>
                        <a href="<c:url value="/admin?f=amenityTypeCreate"/>" class="btn btn-primary ">Create</a>
                    </div>
                </div>
                <div class="row">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>Name</th>
                                <th>Icon</th>
                                <th>Description</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${amenityTypes}" var="amenityType">
                                <tr>
                                    <td scope="row">${amenityType.name}</td>
                                    <td>${amenityType.icon}</td>
                                    <td>${amenityType.description}</td>
                                    <td>
                                        <a href="<c:url value="/admin?f=amenityTypeEdit&id=${amenityType.id}"/>" class="btn btn-primary">Edit</a>
                                        <form action="<c:url value="/admin?f=amenityTypeDelete"/>" method="post" class="d-inline">
                                            <input type="hidden" name="id" value="${amenityType.id}">
                                            <button type="submit" class="btn btn-danger">Delete</button>
                                        </form>
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
