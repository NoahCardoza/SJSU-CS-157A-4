<%--@elvariable id="alert" type="com.example.demo.beans.Alert"--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${alert != null}">
    <div class="alert alert-${alert.color}" role="alert">
            ${alert.message}
    </div>
</c:if>