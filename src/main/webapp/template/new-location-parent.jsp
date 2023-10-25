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
            <h1>Location Parent Select</h1>
            <form method="POST" class="mb-5">
                <select
                        name="parentId"
                        class="form-select mb-3"
                        aria-label="Select a parent location"
                        onchange='
                            (function onchange(self) {
                                document.getElementById("parentName").value = event.target.querySelector("[value=\"" + event.target.value + "\"]").innerText
                                self.form.submit();
                            })(this)'
                >
                    <option value="0" ${form.parentId == null ? 'selected' : ''}>None</option>
                    <c:forEach var="location" items="${locations}">
                        <option ${location.id == form.parentId ? 'selected' : ''} value="${location.id}">${location.name}</option>
                    </c:forEach>
                </select>

                <input type="hidden" name="parentName" id="parentName" value="${form.parentName}">
                <input type="hidden" name="name" value="${form.name}" />
                <input type="hidden" name="description" value="${form.description}" />
                <input type="hidden" name="address" value="${form.address}" />
                <input type="hidden" name="longitude" value="${form.longitude}" />
                <input type="hidden" name="latitude" value="${form.latitude}" />

                <button type="submit" class="btn btn-secondary w-100" name="action" value="back">Back</button>
            </form>
            <form action="../new" method="post" >
                <input type="hidden" name="parentId" value="${form.parentId}">
                <input type="hidden" name="parentName"  value="${form.parentName}">
                <input type="hidden" name="name" value="${form.name}" />
                <input type="hidden" name="description" value="${form.description}" />
                <input type="hidden" name="address" value="${form.address}" />
                <input type="hidden" name="longitude" value="${form.longitude}" />
                <input type="hidden" name="latitude" value="${form.latitude}" />
                <button type="submit" class="btn btn-primary w-100" name="action" value="select">Select</button>
            </form>
        </div>
    </body>
</html>
