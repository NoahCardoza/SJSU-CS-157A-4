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
                        <th>Votes</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="revision" items="${revisions}">
                                <tr>
                                    <td>${revision.createdAt}</td>
                                    <td><a href="<c:url value="/users?f=get&id=${revision.user.id}"/>">${revision.user.username}</a></td>
                                    <td>${revision.edits.size()}</td>
                                    <td>${revision.votes}</td>
                                    <td>
                                        <a href="<c:url value="/revisions?f=get&id=${revision.id}"/>" class="btn btn-primary">View</a>
                                        <form action="<c:url value="/revisions?f=vote"/>" class="d-inline" method="post">
                                            <input type="hidden" name="id" value="${revision.id}">
                                            <button type="submit" name="action" value="up" class="btn btn-${revision.voted == 1 ? 'outline-' : ''}success">Up Vote</button>
                                            <button type="submit" name="action" value="down" class="btn btn-${revision.voted == -1 ? 'outline-' : ''}danger">Down Vote</button>
                                        </form>

                                        <c:if test="${(user.administrator || user.moderator) && !revision.isReverted()}">
                                            <form action="<c:url value="/revisions?f=revert"/>" method="post" class="d-inline">
                                                <input type="hidden" name="id" value="${revision.id}">
                                                <button type="submit" name="method" value="revert" class="btn btn-warning">Revert</button>
                                            </form>
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
