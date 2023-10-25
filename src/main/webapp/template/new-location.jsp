<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%--@elvariable id="locations" type="List<com.example.demo.orm.Location>"--%>
<%--@elvariable id="form" type="List<com.example.demo.bean.LocationForm>"--%>

<!DOCTYPE html>
<html>
    <head>
        <title>Locations</title>
        <%@include file="../includes/head.jsp" %>
    </head>
    <body>
        <%@include file="../includes/nav.jsp" %>
        <div class="container mt-5">
            <h1>New Location Details</h1>

            <form method="POST" class="mt-5" id="new-location-form">
                Parent Location: <b>
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
                            const form = document.getElementById('new-location-form');
                            form.action = 'new/parent';
                            form.submit();
                        })(this)"
                >Select Parent</button>

                <input type="hidden" name="parentId" id="parentId" value="${form.parentId}">
                <input type="hidden" name="parentName" id="parentName" value="${form.parentName}">

                <input type="text" name="name" value="${form.name}" class="form-control mt-5 mb-3" placeholder="Name" />
                <textarea name="description" class="form-control mb-3" placeholder="Description">${form.description}</textarea>
                <input type="text" name="address" value="${form.address}" class="form-control mb-3" placeholder="Address" />
                <input type="text" name="longitude" value="${form.longitude}" class="form-control mb-3" placeholder="Longitude" />
                <input type="text" name="latitude" value="${form.latitude}" class="form-control mb-3" placeholder="Latitude" />

                <button type="submit" class="btn btn-primary w-100" name="action" value="submit">Create</button>
            </form>
        </div>
    </body>
</html>
