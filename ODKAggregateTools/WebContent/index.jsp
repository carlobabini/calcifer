<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="it.angelobabini.odkaggregatetools.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>ODKAggregateExporter</title>
</head>
<body>
Status: <%= OdkToCalcifer.instance().isContinuos() %>
<br/>
Last run: <%= OdkToCalcifer.instance().getLastRun() %>
<br/>
<input type="button" name="" value="stop" onclick="javascript:window.open('action.jsp?action=stopOdkToCalcifer', 'ifStart');" /> <iframe id="ifStart" name="ifStart" style="width:100px; height:30px; border:0px;"></iframe>
<br/>
<input type="button" name="" value="start" onclick="javascript:window.open('action.jsp?action=startOdkToCalcifer', 'ifStop');" /> <iframe id="ifStop" name="ifStop" style="width:100px; height:30px; border:0px;"></iframe>
</body>
</html>