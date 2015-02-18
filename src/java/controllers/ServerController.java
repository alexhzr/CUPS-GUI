/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;


import beans.PrinterBean;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.cups4j.CupsClient;
import org.cups4j.CupsPrinter;
import org.cups4j.WhichJobsEnum;

import servlets.MainServlet;

/**
 *
 * @author Álex
 */
public class ServerController {
    
    private ServerController() {
    }
    
    public static ServerController getInstance() {
        return ThreadsControllerHolder.INSTANCE;
    }
    
    private static class ThreadsControllerHolder {

        private static final ServerController INSTANCE = new ServerController();
    }
    
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException, ServletException, LDAPException {
        try {
            LDAPConnection c = new LDAPConnection("192.168.1.10", 389, "cn="+request.getParameter("username")+",ou=printing,dc=iliberis,dc=com", (String) request.getParameter("password"));
            
            if (c.isConnected()) {
                HttpSession session = LDAPConn.getInstance().loadGroups(request);
                ServerController.getInstance().listPrinter(request, response);
                RequestDispatcher rd;
                List<String> groups = (List<String>) session.getAttribute("groups");
                if(groups.contains("10000"))
                    rd = request.getRequestDispatcher("admin.jsp");
                else rd = request.getRequestDispatcher("success.jsp");
                rd.forward(request, response);
            }
            
        } catch (LDAPException ex) {
            // LDAPException(resultCode=49): Credenciales invalidas
            // LDAPException(resultCode=89): DN o pass vacios
            System.out.println("No connection: "+ex.getExceptionMessage());
            Logger.getLogger(MainServlet.class.getName()).log(Level.SEVERE, null, ex);
            RequestDispatcher rd = request.getRequestDispatcher("index.html");
            rd.forward(request, response);
        }
        
    }
    
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException, ServletException {
        HttpSession session = request.getSession(false);
        session.invalidate();
        RequestDispatcher rd = request.getRequestDispatcher("index.html");
        rd.forward(request, response);
    }
    
    public void sample(HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException {
        try (PrintWriter out = response.getWriter()) {
            out.write("sample operation");

        } 
    }
    
    public void shutdown(HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException {
        try (PrintWriter out = response.getWriter()) {
            out.println("Shutdown");
        } 
    }
    
    public void reboot(HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException {
        try (PrintWriter out = response.getWriter()) {
            out.println("reboot");
        } 
    }
    
    public boolean userAllowed(List<String> opGroups, HttpServletRequest request) {
        Boolean userAllowed = false;
        HttpSession session = request.getSession(false);
        if(opGroups.contains("*"))
            userAllowed = true;
        else if(session != null) {
            for(String group : (ArrayList<String>) session.getAttribute("groups")) {
                if(opGroups.contains(group)) {
                    userAllowed = true;
                    break;
                }
            }
        }
        
        return userAllowed;
    }
    
    public void listPrinter(HttpServletRequest request, HttpServletResponse response) {
        try {
            CupsClient client = new CupsClient("192.168.1.230", 631);
            PrinterBean pb = new PrinterBean();
            for(CupsPrinter printer : client.getPrinters()) {
                String pName = printer.getName();
                pb.setPrinterList("<div class='printer-menu' id='printer-'"+pName+"'>"+
                    "<div class='printer-info'>"+
                            "<h3>"+pName+"</h3>"+
                            "<div class='queue'>Queue: "+printer.getJobs(WhichJobsEnum.NOT_COMPLETED, null, true).size()+"</div>"+
                            "<div class='status'>Status: On</div>"+
                    "</div>"+
                    "<button class='show-hide-button' onclick=\"showHide('permissions-"+pName+"')\">Show/Hide policies</button>"+
                    "<span class='delete-printer-button' onclick='deleteItem(this)'>Delete</span>"+
                    "<div class='policy-statements' id='permissions-"+pName+"' >"+
                            "<ul>"+
                                    "<li><a href='#permissions-"+pName+"-admin'>Admin</a></li>"+
                                    "<li><a href='#permissions-"+pName+"design'>Design</a></li>"+
                                    "<li><a href='#permissions-"+pName+"sales'>Sales</a></li>"+
                            "</ul>"+
                            "<div id='permissions-"+pName+"-admin'></div>"+
                            "<div id='permissions-"+pName+"design'></div>"+
                            "<div id='permissions-"+pName+"sales'></div>"+					
                    "</div>"+
            "</div>");
            }
                pb.setPrinterList("<div class='printer-menu' id='printer-1'>"+
                    "<div class='printer-info'>"+
                            "<h3>imp1</h3>"+
                            "<div class='queue'>Queue: 1</div>"+
                            "<div class='status'>Status: On</div>"+
                    "</div>"+
                    "<button class='show-hide-button' onclick=\"showHide('permissions-1')\">Show/Hide policies</button>"+
                    "<span class='delete-printer-button' onclick='deleteItem(this)'>Delete</span>"+
                    "<div class='policy-statements' id='permissions-1' >"+
                            "<ul>"+
                                    "<li><a href='#permissions-1-admin'>Admin</a></li>"+
                                    "<li><a href='#permissions-1-design'>Design</a></li>"+
                                    "<li><a href='#permissions-1-sales'>Sales</a></li>"+
                            "</ul>"+
                            "<div id='permissions-1-admin'></div>"+
                            "<div id='permissions-1-design'></div>"+
                            "<div id='permissions-1-sales'></div>"+					
                    "</div>"+
            "</div>");

            request.setAttribute("printerList", pb);
        } catch (Exception ex) {
            Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
