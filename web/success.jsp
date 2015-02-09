<%-- 
    Document   : success
    Created on : 26-ene-2015, 12:56:37
    Author     : Álex
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    if (session.getAttribute("username") == null) 
        response.sendRedirect("index.html");
    else {
        out.println("Hello, "+session.getAttribute("username"));
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
        
        <form method="POST" action="MainServlet">
            <input type="hidden" name="op" value="mkxjhdy3"/>
            <input type="submit" value="sample"/>
        </form>
        
        <form method="POST" action="MainServlet" enctype="multipart/form-data">
            <input type="hidden" name="op" value="kos8821s"/>
            <input type="file" />
            <input type="submit" value="subir"/>
        </form>
    </body>
</html>
