<%@ page import="java.io.IOException" %>
<%@ page import="java.util.List" %>
<jsp:useBean id="printerList" class="beans.PrinterBean" scope="request"/>
<%
    if (session.getAttribute("username") == null) 
        response.sendRedirect("index.html");

%>

<!doctype html>
<html>
	<head>
		<meta charset="utf-8">
		<title>CUPS Administration Panel</title>
		<link rel="stylesheet" href="css/jquery-ui.min.css" />
		<link rel="stylesheet" href="css/styles.css" />
		<script type="text/javascript" src="js/jquery.js"></script>
		<script type="text/javascript" src="js/jquery-ui.min.js"></script>
		<script type="text/javascript" src="js/scripts.js"></script>
	</head>
	<body>
		<div id="header">
			<h1>Hi, admin</h1>
			<span id="exitButton">Log out</span>
		</div>
		<div id="delete-printer-confirm" title="Are you sure?">
			<p><span id="delete-printer-icon" class="ui-icon ui-icon-alert"></span>Deleted data will not be able to be recovered.</p>
		</div>
		<div id="error-alert" class="ui-state-error">
			<div><span id="error-alert-icon" class="ui-icon ui-icon-alert"></span>An error ocurred while sending information.</div>
		</div>
		<div id="policy-menu">
		<h3>Politicas de impresión</h3>
			<p class="policy-option">Opcion 1</p>
			<p class="policy-option">Opcion 2</p>
			<p class="policy-option">Opcion 3</p>
			<p class="policy-option">Opcion 4</p>
		</div>
		<div id="wrapper">

			<span id="add-printer-button">Add printer</span>
			<div id="new-printer-dialog">
				<div><h4>Printer name:</h4><input type="text" name="new-printer-name" /></div>
                                <div><h4>Uri device:</h4><input type="text" name="uri-device" /></div>
				<div><h4>Drivers file:</h4><input type="file" name="new-priner-driver-file" /></div>
				<input type="button" name="new-printer-submit" value="Save" />
				<input type="button" name="new-printer-cancel" value="Cancel" />
			</div>
                        <div id="printerList">
			<%= printerList.getPrinterList() %>
                        </div>
                </div>
	</body>
</html>