<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%--@elvariable id="users" type="java.util.List<com.example.demo.beans.entities.User>"--%>
<%--@elvariable id="user" type="com.example.demo.beans.entities.User"--%>

<c:set var="currentUser" value="${user}"/>
<!DOCTYPE html>
<html>
    <head>
        <title>LHG | Locations</title>
        <%@include file="../../includes/head.jsp" %>
    </head>
    <body>
        <%@include file="../../includes/nav.jsp" %>
        <div class="container mt-5">
            <h1>Users</h1>
            <%@include file="/includes/alerts.jsp" %>
            <form class="form" action="/moderation" method="get">
                <div class="mb-2">
                    <label for="q">Search</label>
                    <input id="q" name="q" type="text" class="form-control" value="${param['q']}" />
                </div>
                <button type="submit" class="btn btn-primary float-end">Search</button>
            </form>
            <table class="table">
                <tr>
                    <td>ID</td>
                    <td>Username</td>
                    <td>Email</td>
                    <td width="220px">Created At</td>
                    <td width="110px">Role</td>
                    <td>Actions</td>
                </tr>
                <c:forEach var="user" items="${users}">
                    <tr>
                        <td scope="row"><a href="<c:url value="/users?f=get&id=${user.id}"/>">${user.id}</a></td>
                        <td>${user.username}</td>
                        <td>${user.email}</td>
                        <td>${user.createdAt}</td>
                        <td>
                            <form
                                    action="<c:url value="/moderation?f=users"/>"
                                    method="post"
                            >
                                <select
                                        class="form-select form-select-sm"
                                        <c:if test="${currentUser.id == user.id || (currentUser.id != 1 && (!currentUser.administrator || user.administrator))}">disabled</c:if>
                                        onchange="$(this).parent().submit()"
                                        name="role"
                                >
                                    <option value="1" <c:if test="${!user.administrator}">selected</c:if>>User</option>
                                    <option value="2" <c:if test="${user.moderator}">selected</c:if>>Mod</option>
                                    <c:if test="${user.administrator || currentUser.id == 1}">
                                        <option value="3" <c:if test="${user.administrator}">selected</c:if>>Admin</option>
                                    </c:if>
                                </select>
                                <input type="hidden" name="action" value="role_change"/>
                                <input type="hidden" name="user_id" value="${user.id}"/>
                                <input name="q" type="hidden" value="${param['q']}" />
                            </form>
                        </td>
                        <td>
                            <form
                                    action="<c:url value="/moderation?f=users"/>"
                                    method="post"
                            >
                                <input type="hidden" name="user_id" value="${user.id}"/>
                                <input name="q" type="hidden" value="${param['q']}" />
                                <div class="btn-group btn-group-sm">
                                    <a href="<c:url value="/users?f=get&id=${user.id}"/>"
                                       class="btn btn-primary">View</a>
                                    <c:if test="${user.banned}">
                                            <button type="submit" name="action" value="unban" class="btn btn-danger">Unban</button>
                                    </c:if>
                                    <c:if test="${!user.banned}">
                                            <input type="hidden" name="user_id" value="${user.id}"/>
                                            <button type="submit" name="action" value="ban" class="btn btn-danger">Ban</button>
                                    </c:if>
                                </div>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </body>
</html>
