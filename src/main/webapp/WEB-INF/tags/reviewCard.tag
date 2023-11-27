<%@ tag description="" pageEncoding="UTF-8" %>
<%@ taglib prefix="hg" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="review" type="com.example.demo.beans.entities.Review" %>

<%--@elvariable id="user" type="com.example.demo.beans.entities.User"--%>

<div class="card">
    <div class="card-header d-flex justify-content-between align-items-center" >
        <span class="text-muted">
            <i class="bi bi-person-circle"></i>
                ${review.user.username}
        </span>
        <span class="small text-muted">
            <fmt:setLocale value="en_US" />
            <fmt:formatDate pattern="d MMMM, YYYY 'at' hh:mm:ss a" value = "${review.createdAt}" />
        </span>
    </div>
    <div class="card-body">
        <div class="d-flex justify-content-between align-items-center">
            <h5>${review.name}</h5>
            <div>
                <hg:rating value="${review.calculateAverageRating()}" />
            </div>
        </div>
        <p>${review.description}</p>
        <div>
            <c:forEach var="metric" items="${review.metrics}">
                <div class="row align-items-center">
                    <div class="col-4 fw-bold">${metric.name}</div>
                    <div class="col-8">
                        <div class="progress">
                            <div class="progress-bar ${
                                            metric.value <= 1 ? 'bg-danger' :
                                            metric.value <= 2 ? 'bg-warning' :
                                            metric.value <= 3 ? 'bg-info' :
                                            metric.value <= 4 ? 'bg-primary' :
                                            'bg-success'
                                        }" role="progressbar" style="width: ${(metric.value / 5) * 100}%" aria-valuenow="${metric.value}" aria-valuemin="0" aria-valuemax="5">${metric.value}/5</div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
        <div data-bs-toggle="modal" data-bs-target="#review-carousel-modal-${review.id}">
            <c:forEach var="imageUrl" items="${review.images}">
                <img src="<hg:cloudimg value="${imageUrl}" size="200" />" class="img-thumbnail" alt="">
            </c:forEach>
        </div>
<%--        <div id="review-carousel-modal-${review.id}" class="modal" tabindex="-1">--%>
<%--            <div class="vh-100 vw-100">--%>
<%--                <div class="container">--%>
<%--                    <div id="review-carousel-${review.id}" class="carousel slide">--%>
<%--                        <div class="carousel-inner">--%>
<%--                            <c:forEach var="imageUrl" items="${review.images}">--%>
<%--                                <div class="carousel-item active">--%>
<%--                                    <img class="d-block w-100" src="<hg:cloudimg value="${imageUrl}" />" alt="">--%>
<%--                                </div>--%>
<%--                            </c:forEach>--%>
<%--                        </div>--%>
<%--                        <button class="carousel-control-prev" type="button" data-bs-target="#review-carousel-${review.id}" data-bs-slide="prev">--%>
<%--                            <span class="carousel-control-prev-icon" aria-hidden="true"></span>--%>
<%--                            <span class="visually-hidden">Previous</span>--%>
<%--                        </button>--%>
<%--                        <button class="carousel-control-next" type="button" data-bs-target="#review-carousel-${review.id}" data-bs-slide="next">--%>
<%--                            <span class="carousel-control-next-icon" aria-hidden="true"></span>--%>
<%--                            <span class="visually-hidden">Next</span>--%>
<%--                        </button>--%>
<%--                    </div>--%>
<%--                </div>--%>
<%--            </div>--%>
<%--        </div>--%>
    </div>
    <div class="card-footer d-flex justify-content-between">
        <form action="<c:url value="/reviews?f=vote"/>" class="d-inline" method="post">
            <input type="hidden" name="id" value="${review.id}">
            <div class="btn-group" role="group" aria-label="Votes">
                <button type="submit" name="action" value="up" class="btn btn-sm btn-${review.voted == 1 ? 'outline-' : ''}success" title="Up Vote"><i class="bi bi-caret-up-fill"></i></button>
                <span class="btn btn-sm small" title="Votes">${review.votes}</span>
                <button type="submit" name="action" value="down" class="btn btn-sm btn-${review.voted == -1 ? 'outline-' : ''}danger" title="Down Vote"><i class="bi bi-caret-down-fill"></i></button>
            </div>
        </form>
        <div>
            <c:if test="${user.administrator || user.moderator}">
                <a class="btn btn-sm btn-warning" href="<c:url value="/reviews?f=hide&id=${review.id}"/>" title="${review.hidden ? 'Show' : 'Hide'}">
                    <i class="bi bi-eye${review.hidden ? '' : '-slash'}"></i>
                </a>
            </c:if>
            <c:if test="${user.id == review.userId}">
                <a class="btn btn-sm btn-primary" href="<c:url value="/reviews?f=edit&id=${review.id}"/>" title="Edit">
                    <i class="bi bi-pencil-square"></i>
                </a>
                <a class = "btn btn-sm btn-danger" title="Delete" href="<c:url value="/reviews?f=delete&id=${review.id}"/>">
                    <i class="bi bi-trash"></i>
                </a>
            </c:if>
        </div>
    </div>
</div>



