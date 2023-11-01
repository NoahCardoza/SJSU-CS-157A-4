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
            <h1 class="mb-2">Revision History: ${location.name}</h1>
            <div class="row">
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
                                        <td>${edit.columnName}</td>
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
    </body>
</html>
