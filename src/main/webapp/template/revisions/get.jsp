<%--@elvariable id="revision" type="com.example.demo.beans.entities.Revision"--%>
<%--@elvariable id="revisions" type="java.util.List<com.example.demo.beans.entities.Revision>"--%>
<%--@elvariable id="user" type="com.example.demo.beans.entities.User"--%>
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
            <%@include file="../../includes/alerts.jsp" %>

            <h1 class="mb-2">Revision #${revision.id} <c:if test="${revision.isReverted()}">(Reverted)</c:if></h1>
            <hr>
            <h3>Metadata</h3>
            <div class="row">
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
                        <td>${revision.user.username}</td>
                    </tr>
                    </tbody>
                </table>
                <hr>
                <h3>Fields</h3>
                <table class="table">
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
                <c:if test="${(user.administrator || user.moderator) && !revision.isReverted()}">
                    <form action="<c:url value="/revisions?f=revert"/>" method="post">
                        <input type="hidden" name="id" value="${revision.id}">
                        <button type="submit" name="method" value="revert" class="btn btn-warning">Revert</button>
                    </form>
                </c:if>
            </div>
        </div>
    </body>
</html>
