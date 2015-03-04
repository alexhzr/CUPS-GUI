/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

/**
 *
 * @author Álex
 */
public class OperationStatus {
    private boolean isFinished;
    private String errorDescription;

    public OperationStatus(boolean isFinished, String errorDescription) {
        this.isFinished = isFinished;
        this.errorDescription = errorDescription;
    }

    public boolean isIsFinished() {
        return isFinished;
    }

    public void setIsFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }
    
    
}
