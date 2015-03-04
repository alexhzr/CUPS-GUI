/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threads;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Álex
 */
public class MyThread extends Thread {
    protected Object params;
    protected boolean isFinished;
    
    public MyThread(Object params, String name) {
        super(name);
        this.params = params;
        this.isFinished = false;
    }

    public boolean isFinished() {
        return this.isFinished;
    }
    
    public void setParams(Object params) {
        this.params = params;
    }
    
    @Override
    public void run() {

    }
}
