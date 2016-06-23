<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	HttpServletRequest myrequest = request;
	String uriPrefix = myrequest.getScheme() + "://" + myrequest.getServerName() + ":" + myrequest.getServerPort() + myrequest.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Upload Page</title>
</head>
<body>	
	<h1>Upload a File:</h1>		
	<form action="<%= uriPrefix.toString() %>/resources/images/" method="post" enctype="multipart/form-data">
		<p>Select a file : <input type="file" name="file" size="50" /></p>
		<input type="submit" value="Upload It" />
	</form>	
	<br/>
	<p><a href="index.jsp">Back to Home</a>
</body>
</html>