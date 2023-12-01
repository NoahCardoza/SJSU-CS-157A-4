<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="hg" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <title>Little Hidden Gems</title>
        <%@include file="includes/head.jsp" %>
        <style>
            .b-example-divider {
                width: 100%;
                height: 3rem;
                background-color: rgba(0, 0, 0, .1);
                border: solid rgba(0, 0, 0, .15);
                border-top-width: medium;
                border-right-width: medium;
                border-bottom-width: medium;
                border-left-width: medium;
                border-width: 1px 0;
                box-shadow: inset 0 .5em 1.5em rgba(0, 0, 0, .1), inset 0 .125em .5em rgba(0, 0, 0, .15);
            }
        </style>
    </head>
    <body>
        <%@include file="includes/nav.jsp" %>
        <main>
            <div class="container col-xxl-8 px-4 py-5">
                <%@include file="./includes/alerts.jsp" %>
            </div>
            <div class="container col-xxl-8 px-4 py-5">
                <div class="px-4 py-5 my-5 text-center">
                    <img class="d-block mx-auto mb-4" src="<c:url value="/img/logo-sm.png"/>" alt="Little Hidden Gems" width="80" height="80">
                    <h1 class="display-5 fw-bold text-body-emphasis">Little Hidden Gems</h1>
                    <div class="col-lg-6 mx-auto">
                        <p class="lead mb-4 fs-4">Where community collaboration meets the power of location-based knowledge sharing.</p>
                        <div class="d-grid gap-2 d-sm-flex justify-content-sm-center">
                            <a href="#signup" class="btn btn-primary btn-lg px-4 gap-3">Sign Up</a>
                            <a href="#learn-more" class="btn btn-outline-secondary btn-lg px-4">Learn More</a>
                        </div>
                    </div>
                </div>
            </div>
            <div class="b-example-divider"></div>
            <div id="learn-more" class="container col-xxl-8 px-4 py-5">
                <div class="row flex-lg-row-reverse justify-content-center align-items-center g-5 py-5">
                    <div class="col-10 col-sm-6 col-lg-4">
                        <img src="<c:url value="/img/logo-lg.png"/>" class="d-block mx-auto img-fluid" alt="Hidden Gems" width="300" height="300" loading="lazy">
                    </div>
                    <div class="col-lg-8">
                        <h1 class="display-5 fw-bold text-body-emphasis lh-1 mb-3">Our Mission</h1>
                        <p class="lead fs-4">We believe that local insights should be accessible to all, and that's why
                            we've created a space for people to report, review, and vote on locations and their amenities.
                            Think of it as a <span class="fw-bold">geo-based wiki</span>, driven by your experiences and expertise.
                        </p>
                        <div class="d-grid gap-2 d-md-flex justify-content-md-start">
                            <a href="#signup" class="btn btn-primary btn-lg px-4 me-md-2">Sign Up</a>
                            <a href="<c:url value="/login" />" class="btn btn-outline-secondary btn-lg px-4">Login</a>
                        </div>
                    </div>
                </div>
            </div>
            <div class="b-example-divider"></div>
            <div class="px-4 pt-5 my-5 text-center border-bottom">
                <h1 class="display-4 fw-bold lh-1 text-body-emphasis">Map</h1>
                <div class="col-lg-6 mx-auto">
                    <p class="lead mb-4 fs-4">
                        Whether you want to share
                        the hidden gems in your neighborhood, rave about a fantastic new park, or help fellow travelers discover
                        the best rest stops along a highway, this is the place to do it. Together, we're building a valuable
                        resource that empowers everyone to explore, connect, and make informed decisions about the places we
                        love.
                    </p>
                    <div class="d-grid gap-2 d-sm-flex justify-content-sm-center mb-5">
                        <button type="button" class="btn btn-primary btn-lg px-4 me-sm-3">Visit Map</button>
                        <button type="button" class="btn btn-outline-secondary btn-lg px-4">Search</button>
                    </div>
                </div>
                <div class="overflow-hidden" style="max-height: 30vh;">
                    <div class="container px-5">
                        <img src="<c:url value="/img/example.png" />" class="img-fluid mb-4" alt="Example image" width="700" height="500" loading="lazy">
                    </div>
                </div>
            </div>
            <div class="b-example-divider"></div>
            <div id="signup" class="container col-xl-10 col-xxl-8 px-4 py-5">
                <div class="row align-items-center g-lg-5 py-5">
                    <div class="col-lg-7 text-center text-lg-start">
                        <h2 class="display-4 fw-bold lh-1 text-body-emphasis mb-3">Join Us!</h2>
                        <p class="col-lg-10 fs-4">
                            Join us in creating a global community of location enthusiasts, and let's map the world, one
                            review at a time.
                        </p>
                    </div>
                    <div class="col-md-10 mx-auto col-lg-5">
                        <form class="p-4 p-md-5 border rounded-3 bg-body-tertiary" method="post" action="<c:url value="/signup"/>">
                            <div class="form-floating mb-3">
                                <input type="text" name="username" class="form-control" id="username" placeholder="">
                                <label for="username">Username</label>
                            </div>
                            <div class="form-floating mb-3">
                                <input type="text" name="name" class="form-control" id="name" placeholder="" aria-describedby="nameHelp">
                                <label for="name">Name</label>
                                <div id="nameHelp" class="form-text">By default your name is not shared with other users.</div>
                            </div>
                            <div class="form-floating mb-3">
                                <input type="email" name="email" class="form-control" id="email" aria-describedby="emailHelp" placeholder="">
                                <label for="email" >Email</label>
                                <div id="emailHelp" class="form-text">We'll never share your email with anyone else.</div>
                            </div>
                            <div class="form-floating mb-3">
                                <input type="password" name="password" class="form-control" id="password" placeholder="">
                                <label for="password">Password</label>

                            </div>
                            <button class="w-100 btn btn-lg btn-primary" type="submit" name="action" value="submit">Sign up</button>
                            <hr class="my-4">
                            <small class="text-body-secondary">By clicking "Sign up", you agree to the terms of use.</small>
                        </form>
                    </div>
                </div>
            </div>
        </main>
        <hg:footer />
    </body>
</html>