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
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Jaime
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
    
}
