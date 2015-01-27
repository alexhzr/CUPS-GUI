/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package saxParser;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
 
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
 
import org.xml.sax.SAXException;
/**
 *
 * @author Álex
 */
public class XMLSAXParser {
    public static Hashtable getHashtable() throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = saxParserFactory.newSAXParser();
            MyHandler handler = new MyHandler();
            saxParser.parse(new File("cups-operations.xml"), handler);
            Hashtable<String, String> hashContainer = handler.getHashContainer();
            return hashContainer;
    }
}
