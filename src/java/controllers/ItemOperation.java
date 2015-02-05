/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.util.List;

/**
 *
 * @author Álex
 */
public class ItemOperation {
    private String className;
    private List<String> groups;

    public ItemOperation(String className, List<String> groups) {
        this.className = className;
        this.groups = groups;
    }

    public String getClassName() {
        return className;
    }

    public List<String> getGroups() {
        return groups;
    } 
  
}
