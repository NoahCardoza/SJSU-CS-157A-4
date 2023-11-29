<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>

<%--@elvariable id="metrics" type="java.util.List<com.example.demo.beans.entities.AmenityTypeMetricRecordWithName>"--%>
<%--@elvariable id="review" type="com.example.demo.beans.entities.Review"--%>
<%--@elvariable id="submitButtonText" type="java.lang.String"--%>
<%--@elvariable id="headerText" type="java.lang.String"--%>
<%--@elvariable id="editMode" type="java.lang.Boolean"--%>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>LHG | Review | Create</title>
    <%@include file="../../includes/head.jsp" %>
</head>
<body>
<%@include file="../../includes/nav.jsp" %>
<div class="container mt-5">
    <div class="panel-primary">
        <div class="panel-heading">
            <h1 class="panel-title">${headerText}</h1>
        </div>
        <form method="post" enctype="multipart/form-data">
            <%@include file="../../includes/alerts.jsp" %>

            <div class="row">
                <div class="form-group col-md-6 mb-3">
                    <div class="form-group">
                        <label for="username">Title</label>
                        <input class="form-control" type="text" id="username" name="name" value="${review.name}">
                    </div>
                    <div class="clearfix"></div>
                    <div class="form-group">
                        <label for="comment">Review</label>
                        <textarea class="form-control" rows="8" id="comment" required="required" name="description">${review.description}</textarea>
                    </div>
                </div>
                <div class="form-group col-md-6 mb-3">
                    <p>Metrics</p>
                    <c:forEach var="metric" items="${metrics}">
                        <div class="form-group">
                            <label for="metric-${metric.amenityMetricId}">${metric.name}</label>
                            <input class="form-control" required min="0" max="5" type="text" id="metric-${metric.amenityMetricId}" name="metric-${metric.amenityMetricId}" placeholder="Rate 0 to 5" value="${metric.value}"/>
                        </div>
                    </c:forEach>
                </div>
                <div class="form-group mb-3 d-flex flex-row container">
                    <div class="row gx-3" id="selected-images">
                    </div>
                </div>
                <div class="form-group mb-3">
                    <c:if test="${!editMode}">
                        <label for="image-input" class="form-label">Choose image</label>
                        <input
                                name="images"
                                multiple
                                ${imageRequired ? 'required' : ''}
                                accept="image/*,.heic,.heif"
                                class="form-control"
                                type="file"
                                id="image-input"
                        >
                    </c:if>
                    <c:if test="${editMode}">
                        <p>
                            Editing review images is currently not supported.
                        </p>
                    </c:if>
                </div>
                <div class="clearfix"></div>
                <div class="form-group mt-3">
                    <button class="btn btn-primary" type="submit">${submitButtonText}</button>
                </div>
            </div>
        </form>
    </div>
</div>
<script>
    $(document).ready(function () {
        $('#image-input').on('change', function () {
            const files = $(this).prop('files');
            const selectedImages = $('#selected-images');
            selectedImages.empty();
            for (let i = 0; i < files.length; i++) {
                const file = files[i];
                console.log(file)
                const name = file.name.toLowerCase();
                if (name.endsWith('.heic') || name.endsWith('.heif')) {
                    selectedImages.append(`
                        <div class="col">
                            <div class="img-thumbnail d-flex justify-content-center align-items-center" style="height: 200px; width: 200px;">
                                <p class="fs-6">
                                    HEIC previews are not supported
                                </p>
                            </div>
                        </div>
                    `);
                } else {
                    const reader = new FileReader();
                    reader.onload = function (e) {
                        const img = $('<div class="col"><img class="img-thumbnail" width="200" height="200" /></div>');
                        img.children(0).attr('src', e.target.result);
                        selectedImages.append(img);
                    };
                    reader.readAsDataURL(file);
                }
            }
        });
    });
</script>
</body>
</html>

