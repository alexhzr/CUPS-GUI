package pojos;
// Generated 19-feb-2015 12:35:08 by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * Permission generated by hbm2java
 */
public class Permission  implements java.io.Serializable {


     private Integer id;
     private String description;
     private Set classesPermissions = new HashSet(0);

    public Permission() {
    }

	
    public Permission(String description) {
        this.description = description;
    }
    public Permission(String description, Set classesPermissions) {
       this.description = description;
       this.classesPermissions = classesPermissions;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    public Set getClassesPermissions() {
        return this.classesPermissions;
    }
    
    public void setClassesPermissions(Set classesPermissions) {
        this.classesPermissions = classesPermissions;
    }




}


