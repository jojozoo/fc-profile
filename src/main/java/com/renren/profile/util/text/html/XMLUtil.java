// PROJECT : http://code.google.com/p/owaspantisamy
package com.renren.profile.util.text.html;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

class XMLUtil {

	/**
	 * Helper function for quickly retrieving an attribute from a given
	 * element. 
	 * @param ele The document element from which to pull the attribute value.
	 * @param attrName The name of the attribute.
	 * @return The value of the attribute contained within the element
	 */
	public static String getAttributeValue (Element ele, String attrName) {
		return decode(ele.getAttribute(attrName));
	}
	
	/**
	 * Helper function for quickly retrieving an integer value of a given
	 * XML element.
	 * @param ele The document element from which to pull the integer value.
	 * @param tagName The name of the node.
	 * @return The integer value of the given node in the element passed in.
	 */
	
	public static int getIntValue(Element ele, String tagName, int defaultValue) {
	
		int toReturn = defaultValue;
		
		try {
			toReturn = Integer.parseInt(getTextValue(ele,tagName));
		} catch (Throwable t) { }
			
		return toReturn;
	}
	
	
	/**
	 * Helper function for quickly retrieving a String value of a given
	 * XML element.
	 * @param ele The document element from which to pull the String value.
	 * @param tagName The name of the node.
	 * @return The String value of the given node in the element passed in.
	 */
	public static String getTextValue(Element ele, String tagName) {
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if(nl != null && nl.getLength() > 0) {
			Element el = (Element)nl.item(0);
			if ( el.getFirstChild() != null ) {
				textVal = el.getFirstChild().getNodeValue();
			} else {
				textVal = "";
			}
		}
		return decode(textVal);
	}
	
	
	/**
	 * Helper function for quickly retrieving an boolean value of a given
	 * XML element.
	 * @param ele The document element from which to pull the boolean value.
	 * @param tagName The name of the node.
	 * @return The boolean value of the given node in the element passed in.
	 */
	public static boolean getBooleanValue(Element ele, String tagName) {
		
		boolean boolVal = false;
		NodeList nl = ele.getElementsByTagName(tagName);
		
		if ( nl != null && nl.getLength() > 0 ) {
			Element el = (Element)nl.item(0);
			boolVal = el.getFirstChild().getNodeValue().equals("true");
		}
		
		return boolVal;
	}
	
	/**
	 * Helper function for quickly retrieving an boolean value of a given
	 * XML element, with a default initialization value passed in a parameter.
	 * @param ele The document element from which to pull the boolean value.
	 * @param tagName The name of the node.
	 * @param defaultValue The default value of the node if it's value can't be processed.
	 * @return The boolean value of the given node in the element passed in.
	 */
	public static boolean getBooleanValue(Element ele, String tagName, boolean defaultValue) {
		
		boolean boolVal = defaultValue;
		NodeList nl = ele.getElementsByTagName(tagName);
		
		if ( nl != null && nl.getLength() > 0 ) {
			
			Element el = (Element)nl.item(0);
			
			if ( el.getFirstChild().getNodeValue() != null ) {
			
				boolVal = "true".equals(el.getFirstChild().getNodeValue());

			} else {
		
				boolVal = defaultValue;
				
			}
		}
		
		return boolVal;
	}
	

	/**
	 * Helper function for decode XML entities.
	 * @param str The XML-encoded String to decode.
	 * @return An XML-decoded String.
	 */
	public static String decode(String str) {
		
		if ( str == null ) {
			return null;
		}
		
		str = str.replaceAll("&gt;",">");
		str = str.replaceAll("&lt;","<");
		str = str.replaceAll("&quot;","\"");
		str = str.replaceAll("&amp;","&");
		
		return str;
	}
	
	public static String encode(String str) {
		
		if ( str == null ) {
			return null;
		}
		
		str = str.replaceAll(">","&gt;");
		str = str.replaceAll("<","&lt;");
		str = str.replaceAll("\"","&quot;");
		str = str.replaceAll("&","&amp;");
		
		return str;
	}
}
