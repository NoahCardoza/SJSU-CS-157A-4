<%--<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="hg" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="locations" type="List<com.example.demo.daos.LocationDao>"--%>

<!DOCTYPE html>
<html>
    <head>
        <title>Locations</title>
        <%@include file="../../includes/head.jsp" %>
        <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css"
              integrity="sha256-p4NxAoJBhIIN+hmNHrzRCf9tD/miZyoHS5obTRR9BMY="
              crossorigin=""/>
        <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"
                integrity="sha256-20nQCchB9co0qIjJZRGuk2/Z9VM+kNiyxNV1lvTlZBo="
                crossorigin=""></script>
        <style>
            html, body {
                margin: 0;
                height: 100%;
            }
            #map {
                height: 100%;
            }

            #amenities-container {
                animation: slide-in 0.5s forwards;
                width: 0px;
                height: 100vh;
                overflow-y: auto;
                overflow-x: hidden;
                transition: width 500ms ease-in-out;
            }

            #amenities-container-inner {
                width: 400px;
                height: 100%;
            }

            #amenities-container.open {
                width: 400px;
            }

        </style>
        <script src="/js/map.js"></script>
    </head>
    <body>
        <div class="d-flex flex-row" style="width: 100vw">
            <div style="flex: 1;">
                <%@include file="../../includes/nav.jsp" %>
                <div id="map" style="height: calc(100vh - 56px)"></div>
            </div>
            <div class="" id="amenities-container">
                <div class="p-3" id="amenities-container-inner"></div>
            </div>
        </div>

        <div class="toast-container position-fixed bottom-0 end-0 p-3">
            <div class="toast" role="alert" aria-live="assertive" aria-atomic="true" ></div>
        </div>
        <div class="search-container position-fixed start-0 p-3" style="top: 56px; bottom: 56px; z-index: 1000;">
            <div id="search-form-container" class="p-3 shadow-lg" style="background: white; width: 400px; border-radius: 10px; max-height: 100%; overflow-y: auto;">
                <%@include file="../locations/ajaxForm.jsp" %>
            </div>
            <button class="btn btn-primary mt-3 w-100 shadow-lg" onclick="$('#search-form').submit()">Search</button>
        </div>
    </body>
</html>
