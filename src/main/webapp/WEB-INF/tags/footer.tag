<%@tag description="Standard website footer." pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="hg" tagdir="/WEB-INF/tags" %>

<footer class="bd-footer py-4 py-md-5 mt-5 bg-body-tertiary">
    <div class="container py-4 py-md-5 px-4 px-md-3 text-body-secondary">
        <div class="row">
            <div class="col-lg-3 mb-3 ">
                <a class="d-inline-flex align-items-center mb-2 text-body-emphasis text-decoration-none" href="/" aria-label="Bootstrap">
                    <img src="<c:url value="/img/logo-sm.png" />" alt="LGH" height="50" width="50" class="me-1">
                    <span class="fs-5">Little Hidden Gems</span>
                </a>
                <ul class="list-unstyled small">
                    <li class="mb-2">Made with love by the Little Hidden Gems Team.</li>
                    <li class="mb-2">Code is open-sourced on <a href="https://github.com/NoahCardoza/SJSU-CS-157A-4" target="_blank">GitHub</a>.</li>
                    <li class="mb-2">Currently v1.1.5.</li>
                </ul>
            </div>
            <div class="col-6 col-lg-2 mb-3"></div>
            <div class="col-6 col-lg-2 mb-3"></div>
            <div class="col-6 col-lg-2 offset-lg-1 mb-3">
                <h5>Links</h5>
                <ul class="list-unstyled">
                    <li class="mb-2"><a href="<c:url value="/" />">Home</a></li>
                    <li class="mb-2"><a href="<c:url value="/locations?f=map"/>">Map</a></li>
                    <li class="mb-2"><a href="<c:url value="/locations"/>">Locations</a></li>
                    <li class="mb-2"><a href="<c:url value="/admin"/>">Admin</a></li>
                    <li class="mb-2"><a href="<c:url value="/moderation"/>">Moderation</a></li>
                </ul>
            </div>
            <div class="col-6 col-lg-2 mb-3">
                <h5>Popular Locations</h5>
                <ul class="list-unstyled">
                    <li class="mb-2"><a href="<c:url value="/locations?f=get&id=1"/>">San José State</a></li>
                    <li class="mb-2"><a href="<c:url value="/locations?f=get&id=3"/>">Engineering Building</a></li>
                </ul>
            </div>
        </div>
    </div>
</footer>