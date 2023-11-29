<%--@elvariable id="entityUrl" type="java.lang.String"--%>
<%--@elvariable id="entityName" type="java.lang.String"--%>
<%--@elvariable id="revisions" type="java.util.List<com.example.demo.beans.entities.Revision>"--%>
<%--@elvariable id="user" type="com.example.demo.beans.entities.User"--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <title>LHG | Revisions | ${param['type']} | ${param['id']} </title>
        <%@include file="../../includes/head.jsp" %>
    </head>
    <body>
        <%@include file="../../includes/nav.jsp" %>
        <div class="container mt-5">
            <nav aria-label="breadcrumb">
                <ol class="breadcrumb">
                    <li class="breadcrumb-item"><a href="${entityUrl}">${entityName}</a></li>
                    <li class="breadcrumb-item active" aria-current="page">Revisions</li>
                </ol>
            </nav>

            <h1 class="mb-2">Revision History</h1>
            <p class="lead">${param['type']}: ${entityName}</p>
            <hr class="my-3">
            <c:if test="${revisions.size() > 0}">
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
                                    <a href="<c:url value="/revisions?f=get&id=${revision.id}"/>" class="btn btn-sm btn-primary">View</a>
                                    <form action="<c:url value="/revisions?f=vote"/>" class="d-inline" method="post">
                                        <input type="hidden" name="id" value="${revision.id}">
                                        <button type="submit" name="action" value="up" class="btn btn-sm btn-${revision.voted == 1 ? 'outline-' : ''}success" title="Up Vote"><i class="bi bi-caret-up-fill"></i></button>
                                        <button type="submit" name="action" value="down" class="btn btn-sm btn-${revision.voted == -1 ? 'outline-' : ''}danger" title="Down Vote"><i class="bi bi-caret-down-fill"></i></button>
                                    </form>

                                    <c:if test="${(user.administrator || user.moderator) && !revision.isReverted()}">
                                        <form action="<c:url value="/revisions?f=revert"/>" method="post" class="d-inline">
                                            <input type="hidden" name="id" value="${revision.id}">
                                            <button type="submit" name="method" value="revert" class="btn btn-sm btn-warning" title="Revert"><i class="bi bi-arrow-counterclockwise"></i></button>
                                        </form>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:if>
            <c:if test="${!(revisions.size() > 0)}">
                <p>No revisions found for this entity.</p>
            </c:if>
        </div>
    </body>
</html>
