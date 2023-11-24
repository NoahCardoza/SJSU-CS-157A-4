<%@tag description="Prepares URL for CDN" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="hg" tagdir="/WEB-INF/tags" %>
<%@attribute name="value" type="java.lang.String" %>
<%@attribute name="size" type="java.lang.Integer" %>
<%--<%@attribute name="height" type="java.lang.Integer" %>--%>
<%-- remove protocol --%>
<c:set var="url" value="${value.substring(value.indexOf(':') + 3)}"/>
<%-- remove query string --%>
<c:set var="url" value="${url.substring(0, url.indexOf('?'))}"/>
<%-- create CDN URL --%>
<c:if test="${not empty size}">
    <c:set var="url" value="${url}?width=${size}&height=${size}"/>
</c:if>
https://ctosadjgda.cloudimg.io/${url}