/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threads;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Álex
 */
public class DeletePrinterThread extends MyThread {
    public DeletePrinterThread(Object params, String name) {
        super(params, name);
    }
    
    @Override
    public void run() {
        HttpServletRequest request = (HttpServletRequest) this.params;
        Runtime runtime = Runtime.getRuntime();
        try {
            /*try {
            //Process process = runtime.exec("ssh cups /opt/script/deletePrinter "+this.getName());
            } catch (IOException ex) {
            Logger.getLogger(DeletePrinterThread.class.getName()).log(Level.SEVERE, null, ex);
            }*/
            sleep(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(DeletePrinterThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.isFinished = true;
    }
}
