/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;


import com.unboundid.ldap.sdk.LDAPException;
import controllers.ItemOperation;
import controllers.ServerController;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.xml.parsers.ParserConfigurationException;
import operations.Operation;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.xml.sax.SAXException;
import saxParser.XMLSAXParser;

/**
 *
 * @author Álex
 */
@WebServlet(name = "MainServlet", urlPatterns = {"/MainServlet"})
public class MainServlet extends HttpServlet {    
    Hashtable<String, ItemOperation> hashContainer;
    
    public void init() throws ServletException {
        super.init();
        try {
            ServletContext context = getServletContext();
            hashContainer = XMLSAXParser.getHashtable(context.getResource("/WEB-INF").getPath());
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(MainServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(MainServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainServlet.class.getName()).log(Level.SEVERE, null, ex);
        
        }
    }
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @SuppressWarnings("empty-statement")
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, LDAPException {
        response.setContentType("text/html;charset=UTF-8");
        Map<String, String[]> parametros = request.getParameterMap();
        if (!ServletFileUpload.isMultipartContent(request)) {
            String codop = request.getParameter("op");
            if(codop != null) {
                if(hashContainer.containsKey(codop)) {
                    if(ServerController.getInstance().userAllowed(hashContainer.get(codop).getGroups(), request)) {
                        Class c = Class.forName("operations."+hashContainer.get(codop).getClassName());
                        Operation operation = (Operation) c.newInstance();
                        operation.doIt(request, response);
                    } else {
                        RequestDispatcher rd = request.getRequestDispatcher("notallowed.html");
                        rd.forward(request, response);
                    }
                } else {
                    RequestDispatcher rd = request.getRequestDispatcher("notop.html");
                    rd.forward(request, response);
                }
            } else {
                RequestDispatcher rd = request.getRequestDispatcher("nullop.html");
                rd.forward(request, response);
            } 
        } else {
            ServerController.getInstance().uploadFile(request, response);
        }
            
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MainServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(MainServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(MainServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LDAPException ex) {
            Logger.getLogger(MainServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MainServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(MainServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(MainServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LDAPException ex) {
            Logger.getLogger(MainServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
