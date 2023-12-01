<%@ tag description="" pageEncoding="UTF-8" %>
<%@ taglib prefix="hg" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ attribute name="review" type="com.example.demo.beans.entities.Review" %>

<%--@elvariable id="user" type="com.example.demo.beans.entities.User"--%>
<% request.setAttribute("CR","\n"); %>

<div class="card" id="review-${review.id}">
    <div class="card-header d-flex justify-content-between align-items-center" >
        <div class="text-muted d-flex align-items-center">
            <a href="<c:url value="/users?f=get&id=${review.user.id}"/>" class="btn btn-link text-muted ps-0 text-decoration-none">
                <i class="bi bi-person-circle me-2"></i>${review.user.username}
            </a>
        </div>
        <span class="small text-muted">
            <%-- TODO: make timeZone dynamic --%>
            <fmt:formatDate pattern="d MMMM, YYYY 'at' hh:mm:ss a" value = "${review.createdAt}" timeZone="GMT-8" />
        </span>
    </div>
    <div class="card-body">
        <div class="d-flex justify-content-between align-items-center">
            <h5>${review.name}</h5>
            <div>
                <hg:rating value="${review.calculateAverageRating()}" />
            </div>
        </div>
        <p>
            ${fn:replace(review.description, CR, "<br/>")}
        </p>
        <c:if test="${!review.createdAt.equals(review.updatedAt)}">
            <span class="small text-muted">
                Last updated on
                <%-- TODO: make timeZone dynamic --%>
                <fmt:formatDate pattern="d MMMM, YYYY 'at' hh:mm:ss a" value = "${review.updatedAt}" timeZone="GMT-8" />
            </span>
        </c:if>
        <hr class="my-2"/>
        <div class="row align-items-center gx-1 gy-3">
            <c:forEach var="metric" items="${review.metrics}">
                    <div class="col-12 col-sm-4 fw-bold">${metric.name}</div>
                    <div class="col-12 col-sm-8">
                        <div class="progress">
                            <div class="progress-bar ${
                                            metric.value <= 1 ? 'bg-danger' :
                                            metric.value <= 2 ? 'bg-warning' :
                                            metric.value <= 3 ? 'bg-info' :
                                            metric.value <= 4 ? 'bg-primary' :
                                            'bg-success'
                                        }" role="progressbar" style="width: ${(metric.value / 5) * 100}%" aria-valuenow="${metric.value}" aria-valuemin="0" aria-valuemax="5"
                            >
                                    ${metric.value}/5
                            </div>
                            <c:if test="${metric.value == 0}">
                                <div class="progress-bar bg-secondary" role="progressbar" style="width: 100%" aria-valuenow="0" aria-valuemin="0" aria-valuemax="5">
                                    0/5
                                </div>
                            </c:if>
                        </div>
                </div>
            </c:forEach>
        </div>
        <c:if test="${review.images.size() > 0}">
            <hr class="my-2"/>
            <div>
                <c:forEach var="imageUrlIndex" begin="${1}" end="${review.images.size()}">
                    <c:set var="imageUrl" value="${review.images.get(imageUrlIndex - 1)}" />
                    <button class="img-thumbnail">
                        <img
                                src="<hg:cloudimg value="${imageUrl}" width="50" height="50" />"
                                class="review-image-preview"
                                onclick="$('#review-carousel-${review.id}').carousel(${imageUrlIndex - 1}); $('#review-carousel-modal-${review.id}').modal('show');"
                                alt="Review ${review.id}: Image ${imageUrlIndex}"
                        />
                    </button>
                </c:forEach>
            </div>
        </c:if>
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
                <a class="btn btn-sm btn-warning" href="<c:url value="/reviews?f=hide&id=${review.id}"/>" title="${review.isHidden() ? 'Show' : 'Hide'}">
                    <i class="bi bi-eye${review.isHidden() ? '' : '-slash'}"></i>
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
    <div id="review-carousel-modal-${review.id}" class="modal" tabindex="-1">
        <div class="modal-dialog m-0 modal-dialog-centered min-vw-100 vh-100">
            <div class="modal-content bg-transparent border-0">
                <div id="review-carousel-${review.id}" class="carousel slide w-100">
                    <div class="carousel-inner bg-transparent">
                        <c:forEach var="imageUrlIndex" begin="${1}" end="${review.images.size()}">
                            <c:set var="imageUrl" value="${review.images.get(imageUrlIndex - 1)}" />
                            <div class="carousel-item ${imageUrlIndex == 1 ? 'active' : ''}" id="review-${review.id}-img-${imageUrlIndex}">
                                <img class="d-block mx-auto"  style="max-height: calc(100vh - 4rem); max-width: calc(100vw - 12rem);" src="<hg:cloudimg value="${imageUrl}" func="fit" width="800" height="800" />" alt="">
                            </div>
                        </c:forEach>
                    </div>
                    <button class="carousel-control-prev" type="button" data-bs-target="#review-carousel-${review.id}" data-bs-slide="prev">
                        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                        <span class="visually-hidden">Previous</span>
                    </button>
                    <button class="carousel-control-next" type="button" data-bs-target="#review-carousel-${review.id}" data-bs-slide="next">
                        <span class="carousel-control-next-icon" aria-hidden="true"></span>
                        <span class="visually-hidden">Next</span>
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>



