<%@tag description="Prepares URL for CDN" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="hg" tagdir="/WEB-INF/tags" %>
<%@attribute name="value" type="java.lang.String" %>
<%@attribute name="size" type="java.lang.Integer" %>
<%--<%@attribute name="height" type="java.lang.Integer" %>--%>
<%-- remove protocol --%>
<c:set var="url" value="${value.substring(value.indexOf(':') + 3)}"/>
<%-- remove query string --%>
<c:set var="questionMarkIndex" value="${url.indexOf('?')}"/>
<c:if test="${questionMarkIndex != -1}">
    <c:set var="url" value="${url.substring(0, questionMarkIndex)}"/>
</c:if>
<%-- create CDN URL --%>
<c:if test="${not empty size}">
    <c:set var="url" value="${url}?width=${size}&height=${size}"/>
</c:if>
<%-- output: --%>https://ctosadjgda.cloudimg.io/${url}