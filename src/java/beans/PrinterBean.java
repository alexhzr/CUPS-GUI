/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

/**
 *
 * @author Álex
 */
public class PrinterBean {
    private String printerList;
    
    /*
    HACER: tiene que tener un listado de impresoras formateado en HTML
    */

    public PrinterBean() {
        this.printerList = "";
    }

    public String getPrinterList() {
        return printerList;
    }

    public void setPrinterList(String printerList) {
        this.printerList = this.printerList+printerList;
    }
    
}
