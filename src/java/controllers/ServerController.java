/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;


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
                RequestDispatcher rd = request.getRequestDispatcher("admin.html");
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
    
    public void uploadFile(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
       
        String UPLOAD_DIRECTORY = "C:\\Users\\Jaime\\Documents";
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
    
    public void listPrinter(HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = response.getWriter();
        CupsClient client = new CupsClient("192.168.1.230", 631);
        List<CupsPrinter> printer = client.getPrinters();
        for(int i=0; i<printer.size(); i++) {
            String name = printer.get(i).getName();
            out.write(name);
        }
    }
    public void Download(HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException, ServletException {
                
        response.setContentType("text/html");  
        PrintWriter out = response.getWriter();
        //indicar el archivo
        String filename = "consultas.txt";   
        //la ruta donde esta el archivo
        String filepath = "C:\\Users\\Jaime\\Desktop\\";   
        response.setContentType("APPLICATION/OCTET-STREAM");   
        response.setHeader("Content-Disposition","attachment; filename=\"" + filename + "\"");   

        FileInputStream fileInputStream = new FileInputStream(filepath + filename);  

        int i;   
        while ((i=fileInputStream.read()) != -1) {  
        out.write(i);   
        }   
        fileInputStream.close();   
        out.close();   
          	
    }
}
