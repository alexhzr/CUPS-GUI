/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;


import beans.PrinterBean;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import org.cups4j.CupsClient;
import org.cups4j.CupsPrinter;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
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
        } catch (Exception ex) {
            Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
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
    
    public void uploadFile(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
       
        String UPLOAD_DIRECTORY = "/opt/ppd";
        int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
        int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
        int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB
        
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setFileSizeMax(MAX_FILE_SIZE);
        upload.setSizeMax(MAX_REQUEST_SIZE);
        
        File uploadDir = new File(UPLOAD_DIRECTORY);
        if (!uploadDir.exists()) 
            uploadDir.mkdir();
        
        try {
            // parses the request's content to extract file data
            @SuppressWarnings("unchecked")
           List<FileItem> formItems = upload.parseRequest(request);
            if (formItems != null && formItems.size() > 0) {
                // iterates over form's fields
                for (FileItem item : formItems) {
                    // processes only fields that are not form fields
                    if (!item.isFormField()) {
                        String fileName = new File(item.getName()).getName();
                        String filePath = UPLOAD_DIRECTORY + File.separator + fileName;
                        File storeFile = new File(filePath);
                        item.write(storeFile);
                        PrintWriter out = response.getWriter();                        
                        Runtime runtime = Runtime.getRuntime();
                        Process process = runtime.exec("/opt/script.sh "+fileName);
                        out.write("uplad success");
                        out.close();
                    }
                }
            }
        } catch (Exception ex) {
            PrintWriter out = response.getWriter();
            out.write("There was an error: " + ex.getMessage().toString());
            out.close();
        }
        
    }
    
    public void listPrinter(HttpServletRequest request, HttpServletResponse response) {
        try {
            CupsClient client = new CupsClient("192.168.1.230", 631);
            PrinterBean pb = new PrinterBean();
            for(CupsPrinter printer : client.getPrinters()) {
                String pName = printer.getName();
                pb.setPrinterList("<div class='printer-menu' id='"+pName+"'>"+
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
                                    "<li><a href='#permissions-"+pName+"-design'>Design</a></li>"+
                                    "<li><a href='#permissions-"+pName+"-sales'>Sales</a></li>"+
                            "</ul>"+
                            "<div id='permissions-"+pName+"-admin'></div>"+
                            "<div id='permissions-"+pName+"-design'></div>"+
                            "<div id='permissions-"+pName+"-sales'></div>"+					
                    "</div>"+
            "</div>");
            }
            request.setAttribute("printerList", pb);
        } catch (Exception ex) {
            Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addPolicy(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec("ssh 192.168.1.230:/opt/script/addPermission "+request.getParameter("printerName")+" "+request.getParameter("group")+" "+request.getParameter("command")+" "+request.getParameter("commandValue"));
    }

    public void download(HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException, ServletException {
        response.setContentType("text/html");  
        PrintWriter out = response.getWriter();
        //indicar el archivo
        String filename = "consultas.txt";   
        //la ruta donde esta el archivo dentro de cups
        String filepath = "/opt";   
        response.setContentType("APPLICATION/OCTET-STREAM");   
        response.setHeader("Content-Disposition","attachment; filename=\"" + filename + "\"");   
        FileInputStream fileInputStream = new FileInputStream(filepath + filename);  
        int i;   
        while((i = fileInputStream.read()) != -1)   
            out.write(i);      
        fileInputStream.close();   
        out.close();             	
    }
    public void addPrinter (HttpServletRequest request, HttpServletResponse response) {
        
    }
    public void deletePrinter (HttpServletRequest request, HttpServletResponse response) throws IOException {
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec("ssh cups /opt/script/deletePrinter "+request.getParameter("pName"));
    }
}
