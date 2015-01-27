/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package saxParser;

import java.util.Hashtable;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Álex
 */
public class MyHandler extends DefaultHandler {
    private String key;
    private String value;
    public Hashtable<String, String> hashContainer = new Hashtable<String, String>();
    
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (qName.equalsIgnoreCase("operation")) {
               key = attributes.getValue("id");
               value = attributes.getValue("class");
            }
    }
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("operation")) {
            hashContainer.put(key, value);
        }
    }

    public Hashtable<String, String> getHashContainer() {
        return hashContainer;
    }
    
}
