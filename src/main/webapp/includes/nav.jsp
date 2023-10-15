<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.lang.Boolean" %>

<%
    Boolean isLoggedIn = session.getAttribute("user_id") != null;
    request.setAttribute("isLoggedIn", isLoggedIn);

    String path = request.getServletPath();
%>



<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <div class="container-fluid">
        <a class="navbar-brand" href="#">Hidden Gems</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link <%= path.equals("/index.jsp") ? "active" : "" %>" aria-current="page" href="index.jsp">Home</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link <%= path.equals("/login.jsp") ? "active" : "" %>" href="<%= isLoggedIn ? "logout.jsp" : "login.jsp" %>"><%= isLoggedIn ? "Logout" : "Login" %></a>
                </li>
                <c:if test="${!isLoggedIn}">
                    <li class="nav-item">
                        <a class="nav-link <%= path.equals("/signup.jsp") ? "active" : "" %>" href="signup.jsp">Signup</a>
                    </li>
                </c:if>
            </ul>
            <form class="d-flex mb-0" action="/search.jsp" method="get">
                <input class="form-control me-2" name="q" type="search" placeholder="Search" aria-label="Search">
                <button class="btn btn-outline-success" type="submit">Search</button>
            </form>
        </div>
    </div>
</nav>