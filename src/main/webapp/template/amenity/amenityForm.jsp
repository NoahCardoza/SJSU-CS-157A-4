<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%--@elvariable id="locations" type="List<com.example.demo.daos.LocationDao>"--%>
<%--@elvariable id="form" type="List<com.example.demo.beans.forms.AmenityForm>"--%>

<!DOCTYPE html>
<html>
    <head>
        <title>Amenity</title>
        <%@include file="../../includes/head.jsp" %>
    </head>
    <body>
        <%@include file="../../includes/nav.jsp" %>
        <div class="container mt-5">
            <h1>Add an Amenity</h1>

            <form method="POST" class="mt-5" id="new-amenity-form">
                Location : <b>
                    <c:choose>
                        <c:when test="${empty form.parentName}">
                            None
                        </c:when>
                        <c:otherwise>
                            ${form.parentName}
                        </c:otherwise>
                    </c:choose>
                </b>
                <button
                        class="btn btn-primary float-end"
                        onclick="(function(self) {
                            const form = document.getElementById('new-amenity-form');
                            form.action = '/amenities?f=locationSelect';
                            form.submit();
                        })(this)"
                >Select Location</button>
                 </br>
                 </br>
                 </br>
                Category : <b>
                   <c:choose>
                        <c:when test="${empty form.parentName}">
                            None
                        </c:when>
                        <c:otherwise>
                              ${form.parentName}
                        </c:otherwise>
                   </c:choose>
                </b>
                <button
                        class="btn btn-primary float-end"
                        onclick="(function(self) {
                            const form = document.getElementById('new-amenity-form');
                            form.action = '/amenities?f=parentSelect';
                            form.submit();
                        })(this)"
                   >Select Amenity Category</button>

                <input type="hidden" name="parentId" id="parentId" value="${form.parentId}">
                <input type="hidden" name="parentName" id="parentName" value="${form.parentName}">

                <input type="text" name="name" value="${form.name}" class="form-control mt-5 mb-3" placeholder="Name" />
                <textarea name="description" class="form-control mb-3" placeholder="Description">${form.description}</textarea>

                <%
                %>

                <button type="submit" class="btn btn-primary w-100" name="action" value="submit">Create</button>
            </form>
        </div>
    </body>
</html>
