<%@ page import="java.util.HashMap" %>
<%@ page import="com.example.demo.Util" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    request.setAttribute("postParams", Util.getPostParameters(request));
%>

<!DOCTYPE html>
<html>
    <head>
        <title>LHG | Amenity | Location Select</title>
        <%@include file="../../includes/head.jsp" %>
    </head>
    <body>
        <%@include file="../../includes/nav.jsp" %>
        <div class="container mt-5">
            <h1>Amenity Location Select</h1>
            <form method="POST" class="mb-5">
                <select
                        name="locationId"
                        class="form-select mb-3"
                        aria-label="Select a location"
                        onchange='
                            (function onchange(self) {
                                console.log(event.target.value);
                                document.getElementById("locationName").value = event.target.querySelector("[value=\"" + event.target.value + "\"]").innerText
                                self.form.submit();
                            })(this)'
                >
                    <option value="0" ${form.locationId == null ? 'selected' : ''}>None</option>

                    <c:forEach var="location" items="${locations}">
                        <option ${location.id == form.locationId ? 'selected' : ''} value="${location.id}">${location.name}</option>
                    </c:forEach>
                </select>

                <input type="hidden" name="locationName" id="locationName" value="${form.locationName}">
                <button type="submit" class="btn btn-secondary w-100" name="action" value="back">Revert</button>

                <c:forEach items="${postParams}" var="p">
                    <c:if test="${!fn:startsWith(p.key, 'location') && p.key != 'action'}">
                        <input type="hidden" name="${p.key}" value="${p.value}">
                    </c:if>
                </c:forEach>
            </form>

            <form action="${postParams['redirect']}" method="post">
                <input type="hidden" name="locationId" value="${form.locationId}">
                <input type="hidden" name="locationName" value="${form.locationName}">
                <c:forEach items="${postParams}" var="p">
                    <c:if test="${!fn:startsWith(p.key, 'location') && p.key != 'action'}">
                        <input type="hidden" name="${p.key}" value="${p.value}">
                    </c:if>
                </c:forEach>
                <button type="submit" class="btn btn-primary w-100" name="action" value="select">Select</button>
            </form>
        </div>
    </body>
</html>
