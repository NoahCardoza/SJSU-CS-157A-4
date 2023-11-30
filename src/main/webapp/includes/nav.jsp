<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%--@elvariable id="user" type="com.example.demo.beans.entities.User"--%>

<%
    Boolean isLoggedIn = session.getAttribute("user_id") != null;
    request.setAttribute("isLoggedIn", isLoggedIn);

    String path = (String) request.getAttribute("jakarta.servlet.forward.servlet_path");
    if (path == null) {
        path = String.valueOf(request.getRequestURI());
    }
%>

<nav class="navbar navbar-expand-sm navbar-light bg-light px-2">
    <div class="container-fluid">
        <a class="navbar-brand" href="#">
            <img src="<c:url value="/img/logo-sm.png"/>" alt="Hidden Gems" style="height: 30px">
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0 w-100">
                <li class="nav-item">
                    <a class="nav-link <%= path.equals(request.getContextPath() + "/") ? "active" : "" %>" aria-current="page" href="<c:url value="/" />">Home</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link <%= path.startsWith("/locations") ? "active" : "" %>" href="<c:url value="/locations?f=map"/>">Map</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link <%= path.startsWith("/search") ? "active" : "" %>" href="<c:url value="/search"/>">Search</a>
                </li>
                <c:if test="${user.administrator}">
                    <li class="nav-item">
                        <a class="nav-link <%= path.startsWith("/admin") ? "active" : "" %>" href="<c:url value="/admin"/>">Admin</a>
                    </li>
                </c:if>
                <c:if test="${user.administrator || user.moderator}">
                    <li class="nav-item">
                        <a class="nav-link <%= path.startsWith("/moderator") ? "active" : "" %>" href="<c:url value="/moderation"/>">Moderation</a>
                    </li>
                </c:if>
                <c:if test="${!isLoggedIn}">
                    <li class="nav-item ms-sm-auto">
                        <a class="nav-link <%= path.startsWith("/login") ? "active" : "" %>" href="<c:url value="/login" />">Login</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link <%= path.equals("/signup") ? "active" : "" %>" href="signup">Signup</a>
                    </li>
                </c:if>
                <c:if test="${isLoggedIn}">
                    <li class="nav-item ms-sm-auto">
                        <a class="nav-link" href="<c:url value="/users?f=get&id=${user.id}" />">
                            <i class="bi bi-person-circle"></i>
                            ${user.username}
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="<c:url value="/login?f=logout" />">Logout</a>
                    </li>
                </c:if>
            </ul>
        </div>
    </div>
</nav>