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
        <title>LHG | Amenity | Amenity Type Select</title>
        <%@include file="../../includes/head.jsp" %>
    </head>
    <body>
        <%@include file="../../includes/nav.jsp" %>
        <div class="container mt-5">
            <h1>Amenity Type Select</h1>
            <form method="POST" class="mb-5">
                <select
                        name="typeId"
                        class="form-select mb-3"
                        aria-label="Select an Amenity Type"
                        onchange='
                            (function onchange(self) {
                                console.log(event.target.value);
                                document.getElementById("typeName").value = event.target.querySelector("[value=\"" + event.target.value + "\"]").innerText
                                self.form.submit();
                            })(this)'
                >
                    <option value="0" ${form.typeId == null ? 'selected' : ''}>None</option>

                    <c:forEach var="amenityType" items="${amenityTypes}">
                        <option ${amenityType.id == form.typeId ? 'selected' : ''} value="${amenityType.id}">${amenityType.name}</option>
                    </c:forEach>
                </select>

                <input type="hidden" name="typeName" id="typeName" value="${form.typeName}">
                <button type="submit" class="btn btn-secondary w-100" name="action" value="back">Revert</button>

                <c:forEach items="${postParams}" var="p">
                    <c:if test="${!fn:startsWith(p.key, 'amenityType') && p.key != 'action'}">
                        <input type="hidden" name="${p.key}" value="${p.value}">
                    </c:if>
                </c:forEach>
            </form>

            <c:forEach var="amenityTypeAttribute" items="${amenityTypeAttributes}">
                    <div class="container mt-5">
                       <label for="attributeTypeName" class="form-label">${amenityTypeAttribute.name}</label>
                       <input type="text" name="attributeTypeName" id="attributeTypeName" class="form-control mb-3" placeholder="Value" />
                    </div>
                </c:forEach>

            <form action="${postParams['redirect']}" method="post">
                <input type="hidden" name="typeId" value="${form.typeId}">
                <input type="hidden" name="typeName" value="${form.typeName}">
                <c:forEach items="${postParams}" var="p">
                    <c:if test="${!fn:startsWith(p.key, 'amenityType') && p.key != 'action'}">
                        <input type="hidden" name="${p.key}" value="${p.value}">
                    </c:if>
                </c:forEach>
                <button type="submit" class="btn btn-primary w-100" name="action" value="select">Select</button>
            </form>
        </div>
    </body>
</html>
