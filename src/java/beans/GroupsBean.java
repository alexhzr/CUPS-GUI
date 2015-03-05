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
public class GroupsBean {
    private String groupList;
    
    /*
    HACER: tiene que tener un listado de impresoras formateado en HTML
    */

    public GroupsBean() {
        this.groupList = "";
    }

    public String getGroupList() {
        return groupList;
    }

    public void setGroupList(String groupList) {
        this.groupList = this.groupList+groupList;
    }

    
}
