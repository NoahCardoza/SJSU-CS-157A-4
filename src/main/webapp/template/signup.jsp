<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--@elvariable id="alert" type="com.example.demo.bean.Alert"--%>
<%--@elvariable id="newUser" type="List<com.example.demo.orm.NewUser>"--%>
<%--@elvariable id="form" type="List<com.example.demo.bean.SignupForm>"--%>

<!DOCTYPE html>
<html>
    <head>
        <title>Signup</title>
        <%@include file="../includes/head.jsp" %>
    </head>
    <body>

        <%@include file="../includes/nav.jsp" %>

        <div class="container-sm mt-5" style="max-width: 600px">
            <h3>Signup</h3>

            <form method="POST">
                <div class="mb-3">
                   <label for="exampleInputUsername1" class="form-label">Username</label>
                   <input type="username" name="username" class="form-control" id="inputUsername" value="${form.username}">
                </div>
                <div class="mb-3">
                    <label for="exampleInputEmail1" class="form-label">Email Address</label>
                    <input type="email" name="email" class="form-control" id="inputEmail" aria-describedby="emailHelp" value="${form.email}">
                    <div id="emailHelp" class="form-text">We'll never share your email with anyone else.</div>
                </div>
                <div class="mb-3">
                    <label for="exampleInputPassword1" class="form-label">Password</label>
                    <input type="password" name="password" class="form-control" id="inputPassword" value="${form.password}">
                </div>
                <button type="submit" class="btn btn-primary" name="action" value="submit">Submit</button>
            </form>
        </div>
    </body>
</html>
