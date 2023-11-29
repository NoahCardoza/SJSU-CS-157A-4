<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="hg" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="stats" type="com.example.demo.beans.entities.UserStats"--%>
<%--@elvariable id="profile" type="com.example.demo.beans.entities.User"--%>
<%--@elvariable id="reviews" type="java.util.List<com.example.demo.beans.entities.Review>"--%>
<%--@elvariable id="user" type="com.example.demo.beans.entities.User"--%>

<!DOCTYPE html>
<html>
<head>
    <title>LHG | User | ${profile.username}</title>
    <%@include file="../../includes/head.jsp" %>
</head>
<body>
<%@include file="../../includes/nav.jsp" %>
<div class="container mt-5" style="max-width: 800px">
    <div class="row g-3">
        <div class="col-12">
            <%@include file="/includes/alerts.jsp" %>
        </div>
        <div class="col-12">
            <div class="card" style="border-radius: 15px;">
                <div class="card-body text-center">
                    <img alt="" src="https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-chat/ava2-bg.webp"
                         class="rounded-circle img-fluid" style="width: 100px;">
                    <div class="mt-3 mb-4">
                        <h4 class="mb-2">Jane Doe</h4>
                    </div>
                    <p class="text-muted mb-4">@${profile.username}</p>
                    <div class="d-flex justify-content-around text-center mt-5 mb-2">
                        <div>
                            <p class="mb-2 h5">${stats.reviews}</p>
                            <p class="text-muted mb-0">Reviews</p>
                        </div>
                        <div class="px-3">
                            <p class="mb-2 h5">${stats.amenities}</p>
                            <p class="text-muted mb-0">Amenities Reported</p>
                        </div>
                        <div>
                            <p class="mb-2 h5">${stats.locations}</p>
                            <p class="text-muted mb-0">Locations Reported</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <c:if test="${user.id == profile.id}">
            <div class="col-12">
                <h3 class="text-center">Your Profile</h3>
            </div>
            <div class="col-12">
                <div class="card">
                    <form action="<c:url value="/users?f=edit" />" method="post">
                        <div class="card-body">
                            <div class="mb-3">
                                <label for="username" class="form-label">Username</label>
                                <input type="text" class="form-control" id="username" name="username"
                                       value="${profile.username}" required>
                            </div>
                            <div class="mb-3">
                                <label for="email" class="form-label">Email</label>
                                <input type="email" class="form-control" id="email" name="email"
                                       value="${profile.email}" disabled>
                                <div id="emailHelp" class="form-text">This feature is coming soon.</div>
                            </div>
                            <div class="mb-3">
                                <label for="oldPassword" class="form-label">Password</label>
                                <input type="password" class="form-control" id="oldPassword" name="password" required>
                            </div>
                            <div class="mb-3">
                                <label for="newPassword" class="form-label">New Password</label>
                                <input type="password" class="form-control" id="newPassword" name="newPassword">
                            </div>
                        </div>
                        <div class="card-footer text-end">
                            <button type="submit" class="btn btn-primary">Save</button>
                        </div>
                    </form>
                </div>
            </div>
        </c:if>
        <div class="col-12">
            <h3 class="text-center">Reviews</h3>
        </div>
        <c:choose>
        <c:when test="${reviews.size() > 0}">
        <c:forEach var="review" items="${reviews}">
        <div class="col-12">
            <hg:reviewCard review="${review}"/>
            <div
            </c:forEach>
            </c:when>
            <c:otherwise>
                <div class="col-12">
                    <h3 class="text-center small text-muted">No Reviews</h3>
                </div>
            </c:otherwise>
            </c:choose>

        </div>
    </div>
</div>
<hg:footer/>
</body>
</html>