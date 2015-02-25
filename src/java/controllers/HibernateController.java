/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import hibernate.HibernateUtil;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pojos.Classes;
import pojos.ClassesPermission;
import pojos.Permission;

/**
 *
 * @author Marta
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
    
    public void listClass(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        List list = session.createQuery("FROM Classes").list();
        for(Iterator iterator = list.iterator(); iterator.hasNext();) {
            Classes c = (Classes) iterator.next();
            out.write("Nombre: "+c.getNombre());
        }
        /*PrintWriter out = null;
        Session session = null;
        Transaction tx = null;
        try {
            out = response.getWriter();
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            List list = session.createQuery("FROM Zona").list();
            for(Iterator iterator = list.iterator(); iterator.hasNext();) {
                Zona zona = (Zona) iterator.next();
                out.write("Nombre: "+zona.getNombre());
            }
        } catch (IOException ex) {
            Logger.getLogger(ListZone.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            out.close();
            session.close();*/
    }
    
    public void addValuePermission(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String valor = "valor del permiso";
        
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        
        // session.createQuery("select classes.id from Classes classes where classes.nombre like 'clase1'");
        //select classes.id from Classes classes where classes.nombre like 'clase1'
        
        ClassesPermission value = new ClassesPermission();
        value.setValor(valor);
        session.save(value);
        tx.commit();
    }
    
}
