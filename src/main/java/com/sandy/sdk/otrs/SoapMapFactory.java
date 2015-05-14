package com.sandy.sdk.otrs;

import java.util.Map;
import java.util.Set;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;

import org.w3c.dom.DOMException;

/**
 * Encodes maps the "otrs-way".
 *
 * @author tschechniker
 * @author Gregor Tudan
 */
class SoapMapFactory {
	protected SOAPElement createSoapMap(String name, Map<?, ?> values)
			throws DOMException, SOAPException {
	  SOAPElement element = SOAPFactory.newInstance().createElement(name);
	  element.setAttribute("xmlns:ns2", "http://xml.apache.org/xml-soap");
	  element.setAttribute("xsi:type", "ns2:Map");
	  SOAPElement item = SOAPFactory.newInstance().createElement("item"); 
	  Set<?> set = values.keySet();
	  for (Object key : set) {
	  item.addChildElement("key").addTextNode((String) key).setAttribute("xsi:type", "xsd:string");
	  item.addChildElement("value").addTextNode(values.get(key).toString()).setAttribute("xsi:type", "xsd:string");
	  }
	  element.addChildElement(item);
	  return element;
	}
}