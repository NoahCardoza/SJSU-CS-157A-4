<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="hg" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="stats" type="com.example.demo.beans.entities.UserStats"--%>
<%--@elvariable id="profile" type="com.example.demo.beans.entities.User"--%>
<%--@elvariable id="reviews" type="java.util.List<com.example.demo.beans.entities.Review>"--%>
<%--@elvariable id="user" type="com.example.demo.beans.entities.User"--%>
<%--@elvariable id="form" type="com.example.demo.beans.forms.UserForm"--%>

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
                    <div class="rounded-circle img-fluid bg-dark-subtle text-bg-light mx-auto"
                         style="width: 100px; height: 100px">
                        <i class="bi bi-person-fill" style="font-size: 64px"></i>
                    </div>
                    <div class="mt-3 mb-4">

                        <h4 class="mb-2">
                            ${profile.privateProfile ? '<i class="bi bi-lock-fill"></i>' : ''}
                            ${
                            user.id == profile.id
                                ? profile.name
                                : profile.privateProfile
                                    ? 'Private Profile'
                                    : profile.username
                        }</h4>
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
                    <form method="post">
                        <div class="card-body">
                            <div class="mb-3">
                                <label for="username" class="form-label">Username</label>
                                <input type="text" class="form-control" id="username" name="username"
                                       value="${form.username}" required>
                            </div>
                            <div class="mb-3">
                                <label for="email" class="form-label">Email</label>
                                <input type="hidden" class="form-control" name="email" value="${form.email}">
                                <input type="email" class="form-control" id="email" name="email" value="${form.email}"
                                       disabled>
                                <div id="emailHelp" class="form-text">This feature is coming soon.</div>
                            </div>
                            <div class="mb-3">
                                <label for="name" class="form-label">Name</label>
                                <input type="text" name="name" class="form-control" id="name" aria-describedby="nameHelp" value="${form.name}">
                            </div>
                            <div class="mb-3">
                                <input type="checkbox" class="form-check-input" id="isPrivate" name="isPrivate" ${form.privateProfile ? 'checked' : ''}>
<!--                                ${form}-->
                                <label for="isPrivate" class="form-check-label">Private Profile</label>
                                <div id="isPrivateHelp" class="form-text">If this is unchecked people will be able to see your name.</div>
                            </div>
                            <div class="mb-3">
                                <label for="oldPassword" class="form-label">Password</label>
                                <input type="password" class="form-control" id="oldPassword" name="oldPassword">
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
                        </div>
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
<hg:footer/>
</body>
</html>
