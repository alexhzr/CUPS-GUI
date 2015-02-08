/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;


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
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            
            LDAPConnection c = new LDAPConnection("192.168.1.10", 389, "cn="+username+",ou=printing,dc=iliberis,dc=com", (String) password);
            
            if (c.isConnected()) {
                HttpSession session = LDAPConn.getInstance().loadGroups(request);      
                RequestDispatcher rd = request.getRequestDispatcher("success.jsp");
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
            HttpSession session = request.getSession(false);
            ArrayList<String> userGroups = (ArrayList<String>) session.getAttribute("groups");
            for (String group : userGroups) {
                out.write(group);
            }
            out.println();
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
    
    public void listPrinter(HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = response.getWriter();
        CupsClient client = new CupsClient("192.168.1.230", 631);
        List<CupsPrinter> printer = client.getPrinters();
        for(int i=0; i<printer.size(); i++) {
            String name = printer.get(i).getName();
            out.write(name);
        }
    }
    /*
    private boolean checkUserPermissions(HttpServletRequest request, HttpServletResponse response) throws LDAPException {
        if (request.getSession(false) == null) {
            if (items.contains("*"))
                return true;
            else return false;
        } else {
            LDAPConnection c = LDAPConn.getInstance().getConnection();
            
            request.getSession(false).getAttribute("username");
            if (items.contains("267") || (items.contains("*"))) 
                return true;
            else return false;
        }
    }
    */    
}
