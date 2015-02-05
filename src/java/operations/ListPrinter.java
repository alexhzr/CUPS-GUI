/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operations;

import controllers.ServerController;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Marta
 */
public class ListPrinter extends Operation {

    @Override
    public void doIt(HttpServletRequest request, HttpServletResponse response) {
        try {
            ServerController.getInstance().listPrinter(request, response);
        } catch (Exception ex) {
            Logger.getLogger(ListPrinter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
