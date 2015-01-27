<%-- 
    Document   : edit
    Created on : 11-ene-2015, 14:01:49
    Author     : Álex
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    if (session.getAttribute("user") == null) 
       response.sendRedirect("../index.html");
    
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="http://localhost:8084/AdminPanel/css/styles.css"/>
        <link rel="stylesheet" type="text/css" href="http://localhost:8084/AdminPanel/css/topbar.css"/>
        <link rel="stylesheet" type="text/css" href="http://localhost:8084/AdminPanel/css/jquery-ui.min.css"/>
        <script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>
        <script src="https://code.jquery.com/ui/1.11.2/jquery-ui.min.js" type="text/javascript"></script>
        <script src="http://localhost:8084/AdminPanel/js/topbar.js"></script>
        <script src="http://localhost:8084/AdminPanel/js/functions.js"></script>
        <title>Lighttpd Administration Panel</title>
    </head>
    <body>
        <div id='cssmenu'>
            <ul>
                <li><a href="app.jsp">Home</a></li>
                <li><a href="activate.jsp">Activate website</a></li>
                <li><a href="view.jsp">View websites</a></li>
                <li class="serverStatus"><a href="#">Server status: </a></li>
                <li class="shutdown"><a href="#">Power</a>
                    <ul>
                        <li><a href="#">Shutdown</a></li>
                        <li><a href="#">Reboot</a></li>
                    </ul>
                </li>
            </ul>
        </div>
        <div id="content">
            <div id="confStatus"></div>
            <input type="button" id="confButton" value="button" />
            <table class="preferences">
                <tr>
                    
                </tr>
            </table>
        </div>
    </body>
</html>
