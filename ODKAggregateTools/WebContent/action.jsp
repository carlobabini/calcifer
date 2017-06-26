<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="it.angelobabini.odkaggregatetools.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%
String action = request.getParameter("action");
String result = "Action unknown";
if("startOdkToCalcifer".equals(action)) {
	OdkToCalcifer.instance().startContinuos();
	result = "ok";
}
if("stopOdkToCalcifer".equals(action)) {
	OdkToCalcifer.instance().stopContinuos();
	result = "ok";
}

%>
<title>Action</title>
</head>
<body>
<%= result %>
</body>
</html>