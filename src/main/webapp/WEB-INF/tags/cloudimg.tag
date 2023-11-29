<%@ tag import="com.example.demo.CloudImg" %>
<%@tag description="Prepares URL for CDN" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/WEB-INF/custom-functions.tld" prefix="cfn" %>
<%@taglib prefix="hg" tagdir="/WEB-INF/tags" %>
<%@attribute name="value" type="java.lang.String" %>
<%@attribute name="size" type="java.lang.Integer" %>
<cfn:to-cdn-url url="${value}" size="${size}"/>