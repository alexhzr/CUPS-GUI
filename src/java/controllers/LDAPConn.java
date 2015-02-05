/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import com.unboundid.ldap.sdk.Filter;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.LDAPSearchException;
import com.unboundid.ldap.sdk.SearchRequest;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchResultEntry;
import com.unboundid.ldap.sdk.SearchScope;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Álex
 */
public class LDAPConn {
    private final String host = "192.168.1.10";
    private final Integer port = 389;
    private final String username = "cn=admin, dc=iliberis, dc=com";
    private final String password = "admin";
    private LDAPConnection connection = null;
    
    private LDAPConn() {
        try {
            connection = new LDAPConnection(host, port, username, (String) password);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public static LDAPConn getInstance() {
        return LDAPConnHolder.INSTANCE;
    }
    
    private static class LDAPConnHolder {
        private static final LDAPConn INSTANCE = new LDAPConn();
    }

    public LDAPConnection getConnection() {
        return connection;
    }
    
    public HttpSession loadGroups(HttpServletRequest request) throws LDAPSearchException {
        HttpSession session = request.getSession(true);
        String baseDN = "ou=groups, ou=printing, dc=iliberis, dc=com";
        Filter filter = Filter.createPresenceFilter("cn");
        SearchResult searchResult = connection.search(baseDN, SearchScope.SUB, filter, "memberUid", "gidNumber");
        
        if (searchResult.getEntryCount() != 0) {
            List<SearchResultEntry> entry = searchResult.getSearchEntries();
            ArrayList<String> groups = new ArrayList();
            for (SearchResultEntry srEntry : entry) {
                //Compares 'memberUid' returned from search with 'username' attribute from HTTP session.
                //If 'memberUid' equals 'username', then gidNumber is added to 'groups' HTTP session attribute.
                if (srEntry.getAttribute("memberUid").toString().contains((CharSequence) session.getAttribute("username"))) 
                    groups.add(srEntry.getAttribute("gidNumber").toString());
            }
        }
        
        return session;
    }
}
