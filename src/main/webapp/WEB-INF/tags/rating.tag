<%@tag description="" pageEncoding="UTF-8" %>
<%@taglib prefix="hg" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="value" type="java.lang.Float" %>


<div class="ratings">
    <c:forEach begin="1" end="5" var="i">
        <i class="bi bi-star${value >= i ? '-fill' : value > i - 1 ? '-half' : '-fill'} ${value > i - 1 ? 'rating-color' : ''}"></i>
    </c:forEach>
</div>