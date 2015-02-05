 ##31/01/2015
- Cambiada la manera de la que funciona el parseador. Ahora el XML tiene el atributo "groups" que equivale a los grupos de LDAP que pueden ejecutar la operación. Por lo tanto, el Hashtable en vez de ser <String, String> es <String, ItemOperation>, una nueva clase que he creado y que va a contener el nombre de la clase a invocar y los grupos que pueden invocarla.

- Añadido usuario ldapSearcher al árbol LDAP para poder leer el atributo memberUid (uid de usuario en el grupo) de los grupos con el fin de saber si un usuario concreto puede ejecutar la operación.