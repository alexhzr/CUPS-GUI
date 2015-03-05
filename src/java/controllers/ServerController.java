/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;


import beans.PrinterBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;
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
import threads.DeletePrinterThread;
import threads.MyThread;

/**
 *
 * @author Álex
 */
public class ServerController {
    private Hashtable<String, MyThread> threadsList = new Hashtable();
    
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
                if(groups.contains("10000")) {
                    rd = request.getRequestDispatcher("admin.jsp");
                    request.setAttribute("groupsList", LDAPConn.getInstance().getPrintingGroups());
                }
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
                String className = printer.getName();
                pb.setPrinterList("<div class='printer-menu' id='"+className+"'>"+
                    "<div class='printer-info'>"+
                            "<h3>"+className+"</h3>"+
                            "<div class='queue'>Queue: "+printer.getJobs(WhichJobsEnum.NOT_COMPLETED, null, true).size()+"</div>"+
                            "<div class='status'>Status: On</div>"+
                    "</div>"+
                    "<button class='show-hide-button' onclick=\"showHide('settings-"+className+"')\">Show/Hide settings</button>"+
                    "<span class='delete-printer-button' onclick=\"deletePrinter('"+className+"')\">Delete</span>"+
                    "<div class='settings-statements' id='settings-"+className+"' >"+
                            "<ul>"+
                                    "<li><a href='#settings-"+className+"-permissions'>Admin</a></li>"+
                                    "<li><a href='#settings-"+className+"-printers'>Design</a></li>"+
                                    "<li><a href='#settings-"+className+"-groups'>Sales</a></li>"+
                            "</ul>"+
                            "<div id='settings-"+className+"-permissions'></div>"+
                            "<div id='settings-"+className+"-printers'></div>"+
                            "<div id='settings-"+className+"-groups'></div>"+					
                    "</div>"+
            "</div>");
                }
            if(request.getParameter("fromAjax") == null) {
                request.setAttribute("printerList", pb);
            } else {
                PrintWriter out = response.getWriter();
                out.write(pb.getPrinterList());
                out.close();
            }
            
        } catch (Exception ex) {
            Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addSetting(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec("ssh 192.168.1.230:/opt/script/addSetting "+request.getParameter("printerName")+" "+request.getParameter("group")+" "+request.getParameter("command")+" "+request.getParameter("commandValue"));
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
    
    public void deletePrinter(HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException {
        PrintWriter out = response.getWriter();
        String pName = request.getParameter("pName");
        if(!threadsList.contains(pName)) {
            DeletePrinterThread dpt = new DeletePrinterThread(request, pName);
            dpt.start();
            threadsList.put(dpt.getName(), dpt);
        } 
        
        out.write(pName);
        out.close();
    }
    
    public void askFinished(HttpServletRequest request, HttpServletResponse response) {
        try {
            PrintWriter out = response.getWriter();
            String threadName = request.getParameter("threadName");
            boolean isFinished = threadsList.get(threadName).isFinished();
            if(isFinished)
                threadsList.remove(threadName);
            response.setContentType("application/json");
            OperationStatus operationStatus = new OperationStatus(isFinished, "hola");
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            out.write(gson.toJson(operationStatus));
        } catch (IOException ex) {
            Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

