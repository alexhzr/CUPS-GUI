/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package saxParser;

import controllers.ItemOperation;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Álex
 */
public class MyHandler extends DefaultHandler {
    private String key;
    private String className;
    private String groups;
    private ItemOperation itemOperation;
    private Hashtable<String, ItemOperation> hashContainer = new Hashtable<String, ItemOperation>();
    
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (qName.equalsIgnoreCase("operation")) {
               key = attributes.getValue("id");
               className = attributes.getValue("class");
               List<String> groups = Arrays.asList(attributes.getValue("groups").split(", "));
               itemOperation = new ItemOperation(className, groups);
            }
    }
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("operation")) {
            hashContainer.put(key, itemOperation);
        }
    }

    public Hashtable<String, ItemOperation> getHashContainer() {
        return hashContainer;
    }
    
}
