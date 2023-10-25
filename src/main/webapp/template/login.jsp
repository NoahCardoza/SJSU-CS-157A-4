<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--@elvariable id="alert" type="com.example.demo.bean.Alert"--%>

<!DOCTYPE html>
<html>
    <head>
        <title>Login Page</title>
        <%@include file="../includes/head.jsp" %>
    </head>
    <body>

        <%@include file="../includes/nav.jsp" %>

        <div class="container-sm mt-5" style="max-width: 600px">
            <h3>Login</h3>

            <form method="post">
                <c:if test="${alert != null}">
                    <div class="alert alert-${alert.color}" role="alert">
                            ${alert.message}
                    </div>
                </c:if>
                <div class="mb-3">
                    <label for="exampleInputEmail1" class="form-label">Email address</label>
                    <input type="email" name="email" class="form-control" id="exampleInputEmail1" aria-describedby="emailHelp">
                    <div id="emailHelp" class="form-text">We'll never share your email with anyone else.</div>
                </div>
                <div class="mb-3">
                    <label for="exampleInputPassword1" class="form-label">Password</label>
                    <input type="password" name="password" class="form-control" id="exampleInputPassword1">
                </div>
                <div class="mb-3 form-check">
                    <input type="checkbox" name="keepMeLoggedIn" class="form-check-input" id="exampleCheck1">
                    <label class="form-check-label" for="exampleCheck1">Keep me logged in</label>
                </div>
                <button type="submit" class="btn btn-primary">Submit</button>
            </form>
        </div>
    </body>
</html>
