package pojos;
// Generated 19-feb-2015 12:35:08 by Hibernate Tools 4.3.1



/**
 * ClassesPermission generated by hbm2java
 */
public class ClassesPermission  implements java.io.Serializable {


     private Integer id;
     private Classes classes;
     private Permission permission;
     private String valor;

    public ClassesPermission() {
    }

	
    public ClassesPermission(Classes classes, Permission permission) {
        this.classes = classes;
        this.permission = permission;
    }
    public ClassesPermission(Classes classes, Permission permission, String valor) {
       this.classes = classes;
       this.permission = permission;
       this.valor = valor;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Classes getClasses() {
        return this.classes;
    }
    
    public void setClasses(Classes classes) {
        this.classes = classes;
    }
    public Permission getPermission() {
        return this.permission;
    }
    
    public void setPermission(Permission permission) {
        this.permission = permission;
    }
    public String getValor() {
        return this.valor;
    }
    
    public void setValor(String valor) {
        this.valor = valor;
    }




}


