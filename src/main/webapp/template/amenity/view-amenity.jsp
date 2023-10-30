<%--@elvariable id="revisions" type="java.util.List<com.example.demo.beans.entities.Revision>"--%>
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
            <div class="row">
                <div class="col">
                    <img width="200" height="200" src="/locations?f=map&id=${location.id}">
                </div>
                <div class="col">
                        <p>${location.description}</p>
                </div>
            </div>
            <div class="row">
                <div class="accordion" id="accordionExample">
                    <div class="accordion-item">
                        <h2 class="accordion-header">
                            <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseTwo" aria-expanded="false" aria-controls="collapseTwo">
                                Revision History
                            </button>
                        </h2>
                        <div id="collapseTwo" class="accordion-collapse collapse" data-bs-parent="#accordionExample">
                            <div class="accordion-body">
                                <ul>
                                    <c:forEach var="revision" items="${revisions}">
                                        <li>
                                            <table class="table">
                                                <thead>
                                                <tr>
                                                    <th scope="col">Date</th>
                                                    <th scope="col">User</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <tr>
                                                    <td>${revision.createdAt}</td>
                                                    <td>${revision.userId}</td>
                                                </tr>
                                                </tbody>
                                            </table>
                                            <table>
                                                <thead>
                                                <tr>
                                                    <th scope="col">Field</th>
                                                    <th scope="col">Old Value</th>
                                                    <th scope="col">New Value</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <c:forEach var="edit" items="${revision.edits}">
                                                    <tr>
                                                        <td>${edit.column}</td>
                                                        <td>${edit.previousValue}</td>
                                                        <td>${edit.newValue}</td>
                                                    </tr>
                                                </c:forEach>
                                                </tbody>
                                            </table>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </div>
                        </div>
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