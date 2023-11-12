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
                <table class="table">
                    <thead>
                    <tr>
                        <th scope="col">Date</th>
                        <th scope="col">User</th>
                        <th>Changes</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="revision" items="${revisions}">

                                <tr>
                                    <td>${revision.createdAt}</td>
                                    <td>${revision.userId}</td>
                                    <td>${revision.edits.size()}</td>
                                    <td>
                                        <a href="/revisions?f=get&id=${revision.id}" class="btn btn-primary">View</a>
                                        <form action="/revisions?f=vote" class="d-inline" method="post">
                                            <input type="hidden" name="id" value="${revision.id}">
                                            <button type="submit" name="action" value="up" class="btn btn-primary">Up Vote</button>
                                            <button type="submit" name="action" value="down" class="btn btn-primary">Down Vote</button>
                                        </form>
                                        <c:if test="${user.administrator || user.moderator}">
                                            <a href="/revisions?f=revert&id=${revision.id}" class="btn btn-danger">Revert</a>
                                        </c:if>
                                    </td>
                                </tr>

                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </body>
</html>
