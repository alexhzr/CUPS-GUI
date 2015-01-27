/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import controllers.ServerController;

/**
 *
 * @author Álex
 */
public abstract class Operation {
    public abstract void doIt(HttpServletRequest request, HttpServletResponse response);
}
