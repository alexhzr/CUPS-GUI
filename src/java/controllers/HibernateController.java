/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;


import config.HibernateUtil;
import hibernate.Classes;
import hibernate.ClassesPermission;
import hibernate.Permission;
import java.io.IOException;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.Transaction;


/**
 *
 * @author Jaime
=======
import pojos.Classes;
import pojos.ClassesPermission;
import pojos.Permission;

/**
 *
 * @author Marta
>>>>>>> origin/permissions
 */
public class HibernateController {
    
    private HibernateController() {
    }
    
    public static HibernateController getInstance() {
        return HibernateControllerHolder.INSTANCE;
    }
    private static class HibernateControllerHolder {

        private static final HibernateController INSTANCE = new HibernateController();
    }

    public void listClassesPermissions(HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException {
        Session session = HibernateUtil.getSessionFactory().openSession();
      Transaction tx = null;
      
         tx = session.beginTransaction();
         List lista = session.createQuery("FROM ClassesPermission").list(); 
         for (Iterator iterator = 
                           lista.iterator(); iterator.hasNext();){
            ClassesPermission classespermission = (ClassesPermission) iterator.next(); 
            
            System.out.println("permiso: " +classespermission.getValor());
            
         }
         tx.commit();
    }
    
     public void listPermission(HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException {
        Session session = HibernateUtil.getSessionFactory().openSession();
      Transaction tx = null;
      
         tx = session.beginTransaction();
         List lista = session.createQuery("FROM Permission").list(); 
         for (Iterator iterator = 
                           lista.iterator(); iterator.hasNext();){
            Permission permission = (Permission) iterator.next(); 
            
            System.out.println("permiso: " +permission.getDescription());
            
         }
         tx.commit();
    }
    
    public void addclass(HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException {
        // recoger el nombre con request.getparameter
        String nombre = "clase1";
       Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
         Classes clase = new Classes();
        
        clase.setNombre(nombre);
        
         
        //Save the employee in database
        session.save(clase);
         session.getTransaction().commit();
    }
    
    public void addpermission(HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException {
       
        String description = "descripcion de prueba del permiso";
       Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
         Permission permission = new Permission();
        
        permission.setDescription(description);
        
         
        //Save the employee in database
        session.save(permission);
         session.getTransaction().commit();
    }
    
    public void deleteClass(HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException {
     
        int id = 1;
        Session session = HibernateUtil.getSessionFactory().openSession();
      Transaction tx = null;
      
         tx = session.beginTransaction();
        session.createQuery("delete from Classes where id="+id).executeUpdate();
    }
    
    public void listClass(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        List list = session.createQuery("FROM Classes").list();
        for(Iterator iterator = list.iterator(); iterator.hasNext();) {
            Classes c = (Classes) iterator.next();
            out.write("Nombre: "+c.getNombre());
        }
    }
    
    public void addValuePermission(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String valuePermission = request.getParameter("value");
        String namePermission = request.getParameter("permission");
        String nameClass = request.getParameter("class");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Classes idClass = (Classes) session.createQuery("select classes.id from Classes classes where classes.nombre like '"+nameClass+"'").uniqueResult();
        Permission idPermission = (Permission) session.createQuery("select permission.id from Permission permission where permission.description like '"+namePermission+"'").uniqueResult();
        ClassesPermission cp = new ClassesPermission();
        cp.setValor(valuePermission);
        cp.setClasses(idClass);
        cp.setPermission(idPermission);
        session.save(cp);
        tx.commit();
    }
    
    public void DeletePermission(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idPermission = request.getParameter("idPermission");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.createQuery("delete from Pemission where description="+idPermission).executeUpdate();
        tx.commit();
    }
    
    public void DeleteClassePermission(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.createQuery("delete from ClassesPemission where id="+id).executeUpdate();
        tx.commit();

    }
    
}
