<%-- 
    Document   : success
    Created on : 26-ene-2015, 12:56:37
    Author     : Álex
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    if (session.getAttribute("user") == null) 
        response.sendRedirect("index.html");
    else {
        out.println("Hello, "+session.getAttribute("user"));
    }
    
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Success!</h1>
        <form method="POST" action="MainServlet">
            <input type="hidden" name="op" value="lqo381hx"/>
            <input type="submit" value="Log out"/>
        </form>
    </body>
</html>
