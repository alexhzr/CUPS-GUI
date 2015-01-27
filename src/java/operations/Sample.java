/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operations;

import controllers.ServerController;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Álex
 */
public class Sample extends Operation{

    @Override
    public void doIt(HttpServletRequest request, HttpServletResponse response) {
        try {
            ServerController.getInstance().sample(request, response);
        } catch (IOException ex) {
            Logger.getLogger(Sample.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Sample.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
