/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operations;

import controllers.ServerController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Manrique
 */
public class AddPrinter extends Operation {

    @Override
    public void doIt(HttpServletRequest request, HttpServletResponse response) {
        ServerController.getInstance().addPrinter(request, response);
    }
    
}
