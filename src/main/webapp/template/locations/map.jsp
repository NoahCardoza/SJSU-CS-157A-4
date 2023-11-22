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

            body.new-amenity #map {

                cursor: cell!important;
            }

        </style>
        <script src="<c:url value="/js/map.js"/>"></script>
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
        <div class="toolbar-container position-fixed bottom-0 end-0 start-0 text-center p-3" style="z-index: 1000;">
            <button class="btn btn-primary" id="report-new-amenity">New Amenity</button>
        </div>
        <div class="search-container position-fixed start-0 p-3" style="top: 56px; bottom: 56px; z-index: 1000;">
            <div id="search-form-container" class="p-3 shadow-lg" style="background: white; width: 400px; border-radius: 10px; max-height: 100%; overflow-y: auto;">
                <%@include file="../locations/ajaxForm.jsp" %>
            </div>
            <button class="btn btn-primary mt-3 w-100 shadow-lg" onclick="$('#search-form').submit()">Search</button>
        </div>
        <div id="new-location-modal" class="modal" tabindex="-1">
            <div class="modal modal-dialog modal-dialog-centered modal-dialog-scrollable">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Choose a location</h5>
                    </div>
                    <div class="modal-body">
                        <p>
                            Choose a nearby location from the list or create a new location to report an amenity.
                        </p>
                        <label for="new-amenity-location-select" class="form-label">Location</label>
                        <select class="form-select form-select-sm" name="location_id" id="new-amenity-location-select"></select>
                    </div>
                    <div class="modal-footer">
                        <button id="new-location-cancel" type="button" class="btn btn-secondary" data-bs-dismiss="modal" data-bs-target="#new-location-modal">Cancel</button>
                        <button id="new-location-submit" type="button" class="btn btn-primary">Submit</button>
                    </div>
                </div>
            </div>
        </div>

    </body>
</html>
