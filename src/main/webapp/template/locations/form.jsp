<%--@elvariable id="headerText" type="java.lang.String"--%>
<%--@elvariable id="titleText" type="java.lang.String"--%>
<%--@elvariable id="pathWithQueryString" type="java.lang.String"--%>
<%--@elvariable id="locations" type="List<com.example.demo.daos.LocationDao>"--%>
<%--@elvariable id="form" type="List<com.example.demo.beans.forms.LocationForm>"--%>
<%--@elvariable id="editMode" type="java.lang.Boolean"--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <title>LHG | Location | ${titleText}</title>
        <%@include file="../../includes/head.jsp" %>
    </head>
    <body>
        <%@include file="../../includes/nav.jsp" %>
        <div class="container mt-5">
            <h1>${headerText}</h1>
            <img src="<c:url value="/locations?f=mapImage&latitude=${form.latitude}&longitude=${form.longitude}"/>">
            <form method="POST" class="mt-5" id="new-location-form">
                <c:if test="${!editMode}">
                    <div class="row">
                        <%@include file="../../includes/alerts.jsp" %>
                        <div class="col mb-3 d-flex justify-content-between align-items-center">
                            <div>
                                Parent Location:
                                <b>
                                    <c:choose>
                                        <c:when test="${empty form.parentName}">
                                            None
                                        </c:when>
                                        <c:otherwise>
                                            ${form.parentName}
                                        </c:otherwise>
                                    </c:choose>
                                </b>
                            </div>
                            <button
                                    class="btn btn-primary float-end"
                                    onclick="$('#new-location-form').attr('action', '${param['id'] ? '/locations?f=parentSelect&id=' + param['id'] : '/locations?f=parentSelect'}').submit()"
                            >Select Parent</button>
                        </div>
                    </div>
                </c:if>

                <input type="hidden" name="parentId" id="parentId" value="${form.parentId}">
                <input type="hidden" name="parentName" id="parentName" value="${form.parentName}">

                <label for="name" class="form-label">Name</label>
                <input type="text" id="name" name="name" required="true" value="${form.name}" class="form-control mb-3" placeholder="Name" />
                <label for="description" class="form-label">Description</label>
                <textarea id="description" name="description" class="form-control mb-3" placeholder="Description">${form.description}</textarea>
                <label for="address" class="form-label">Address</label>
                <input type="text" id="address" name="address" value="${form.address}" class="form-control mb-3" placeholder="Address" />
<%--                <label for="longitude" class="form-label">Longitude</label>--%>
                <input type="hidden" id="longitude" name="longitude" value="${form.longitude}" class="form-control mb-3" placeholder="Longitude" />
<%--                <label for="latitude" class="form-label">Latitude</label>--%>
                <input type="hidden" id="latitude" name="latitude" value="${form.latitude}" class="form-control mb-3" placeholder="Latitude" />
                <input type="hidden" name="redirect" value="${pathWithQueryString}">
                <button type="submit" class="btn btn-primary w-100" name="action" value="submit">${primaryButtonText}</button>
            </form>
        </div>
    </body>
</html>
