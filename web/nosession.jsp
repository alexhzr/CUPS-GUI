<%-- 
    Document   : nosession
    Created on : 27-ene-2015, 20:46:16
    Author     : Álex
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    if (session.getAttribute("user") != null) 
        response.sendRedirect("index.html");
    
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>No session</h1>
    </body>
</html>
