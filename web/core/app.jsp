<%-- 
    Document   : app
    Created on : 16-dic-2014, 19:53:02
    Author     : Álex
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    if (session.getAttribute("user") == null) 
        response.sendRedirect("index.html");
    
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="../css/styles.css"/>
        <link rel="stylesheet" type="text/css" href="../css/topbar.css"/>
        <link rel="stylesheet" type="text/css" href="css/styles.css"/>
        <link rel="stylesheet" type="text/css" href="css/topbar.css"/>
        <script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>
        <script src="js/topbar.js"></script>
            
        <title>Lighttpd Administration Panel</title>
    </head>
    <body>
        <div id='cssmenu'>
            <ul>
                <li><a href="preferences.jsp">Edit preferences</a></li>
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
            <p class="welcome">Welcome to Lighttpd Administration Panel. Please, choose an option from the topbar and start managing the server.</p>
        </div>
    </body>
</html>
