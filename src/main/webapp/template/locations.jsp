<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%--@elvariable id="locations" type="List<com.example.demo.orm.Location>"--%>

<!DOCTYPE html>
<html>
    <head>
        <title>Locations</title>
        <%@include file="../includes/head.jsp" %>
    </head>
    <body>
        <%@include file="../includes/nav.jsp" %>
        <div class="container mt-5">
            <h1>Locations</h1>
            <table class="table">
                <tr>
                    <td>ID</td>
                    <td>Name</td>
                    <td>Address</td>
                    <td>Description</td>
                    <td>Long</td>
                    <td>Lat</td>
                </tr>
                <c:forEach var="location" items="${locations}">
                    <tr>
                        <td scope="row"><a href="location/${location.id}">${location.id}</a></td>
                        <td>${location.name}</td>
                        <td>${location.address}</td>
                        <td>${location.description}</td>
                        <td>${location.longitude}</td>
                        <td>${location.latitude}</td>
                    </tr>
                </c:forEach>
            </table>
            <a class="btn btn-primary float-end" href="location/new">New</a>
        </div>
    </body>
</html>