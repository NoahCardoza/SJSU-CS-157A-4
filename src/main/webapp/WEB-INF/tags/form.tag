<%@ attribute name="hideSubmitButton" %>
<%@ attribute name="className" %>
<%@tag description="" pageEncoding="UTF-8" %>
<%@attribute name="amenityTypeAttributes" type="com.example.demo.servlets.search.AmenityTypeAttributeGrouper" %>
<%@attribute name="amenityTypes" type="java.util.List<com.example.demo.beans.entities.AmenityType>" %>
<%@attribute name="autoSubmit" type="java.lang.Boolean"%>
<%@attribute name="defaultToAllSelected" type="java.lang.Boolean"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib uri="/WEB-INF/custom-functions.tld" prefix="cfn" %>

<c:set var="defaultToAllSelected" value="${(empty defaultToAllSelected) ? false : true}" />
<c:set var="autoSubmit" value="${(empty autoSubmit) ? false : true}" />

<c:set var="onsubmit" value="if ($('#amenityType').val() === '0') {
location.href = '/search';
return false;
}"/>

<form id="search-form" onsubmit="${autoSubmit ? onsubmit : ''}" class="${className}" >
    <div class="form-control mb-2">
        <label for="amenityType" class="form-label">Amenity Type</label>
        <select class="form-select" autocomplete="off" name="amenityTypeId" id="amenityType" onchange="$('#search-form').submit()">
            <option value="0" ${defaultToAllSelected ? 'selected' : ''} >All</option>
            <c:forEach var="amenityType" items="${amenityTypes}">
                <option ${param.get('amenityTypeId') == amenityType.id ? 'selected' : ''} value="${amenityType.id}"
                >${amenityType.name}</option>
            </c:forEach>
        </select>
    </div>

    <c:if test="${not empty amenityTypeAttributes}">
        ${amenityTypeAttributes.textAttributes}
        ${amenityTypeAttributes.booleanAttributes}
        ${amenityTypeAttributes.numberAttributes}
    </c:if>
    <c:if test="${not hideSubmitButton}">
        <button type="submit" class="btn btn-primary mt-3 w-100">Search</button>
    </c:if>
</form>