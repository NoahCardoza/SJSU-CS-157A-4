<%@ page contentType="text/html" pageEncoding="UTF-8"%>

<%--@elvariable id="User" type="List<com.example.demo.daos.UserDao>"--%>
<%--@elvariable id="form" type="List<com.example.demo.beans.forms.SignupForm>"--%>

<!DOCTYPE html>
<html>
    <head>
        <title>LHG | Signup</title>
        <%@include file="../../includes/head.jsp" %>
    </head>
    <body>

        <%@include file="../../includes/nav.jsp" %>

        <div class="container-sm mt-5" style="max-width: 600px">
            <h3>Signup</h3>

            <form method="POST">
                <%@include file="../../includes/alerts.jsp" %>
                <div class="mb-3">
                   <label for="username" class="form-label">Username</label>
                   <input type="text" name="username" class="form-control" id="username" value="${form.username}">
                </div>
                <div class="mb-3">
                    <label for="name" class="form-label">Name</label>
                    <input type="text" name="name" class="form-control" id="name" aria-describedby="nameHelp" value="${form.name}">
                    <div id="nameHelp" class="form-text">By default your name is not shared with other users.</div>
                </div>
                <div class="mb-3">
                    <label for="email" class="form-label">Email Address</label>
                    <input type="email" name="email" class="form-control" id="email" aria-describedby="emailHelp" value="${form.email}">
                    <div id="emailHelp" class="form-text">We'll never share your email with anyone else.</div>
                </div>
                <div class="mb-3">
                    <label for="password" class="form-label">Password</label>
                    <input type="password" name="password" class="form-control" id="password" value="${form.password}">
                </div>
                <button type="submit" class="btn btn-primary" name="action" value="submit">Submit</button>
            </form>
        </div>
    </body>
</html>
