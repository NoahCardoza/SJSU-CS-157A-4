<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib uri="/WEB-INF/custom-functions.tld" prefix="cfn" %>
<%@ taglib prefix="hg" tagdir="/WEB-INF/tags" %>

<%--@elvariable id="locations" type="List<com.example.demo.daos.LocationDao>"--%>
<%--@elvariable id="amenityTypeAttributes" type="com.example.demo.servlets.search.AmenityTypeAttributeGrouper"--%>
<%--@elvariable id="amenityTypes" type="java.util.List<com.example.demo.beans.entities.AmenityType>"--%>

<hg:form
    hideSubmitButton="${true}"
    defaultToAllSelected="${true}"
    amenityTypes="${amenityTypes}"
    amenityTypeAttributes="${amenityTypeAttributes}"
/>
