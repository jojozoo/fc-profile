// PROJECT : http://code.google.com/p/owaspantisamy
package com.orientalcomics.profile.util.text.html;

class HTMLEntityEncoder {
	
	/**
	 * A helper method for HTML entity-encoding a String value.
	 * @param value A String containing HTML control characters.
	 * @return An HTML-encoded String.
	 */
	public static String htmlEntityEncode(String value) {
		
		StringBuffer buff = new StringBuffer();
		
		if ( value == null ) {
			return null;
		}
		
		for(int i=0;i<value.length();i++) {
			
			char ch = value.charAt(i);
			
			if ( ch == '&' ) {
				buff.append("&amp;");
			} else if ( ch == '<') {
				buff.append("&lt;");
			} else if ( ch == '>') {
				buff.append("&gt;");
			} else if ( Character.isWhitespace(ch ) ) {
				buff.append(ch);
			} else if ( Character.isLetterOrDigit(ch) ) {
				buff.append(ch);
			} else if ( (int)ch >= 32 && (int)ch <= 126 ) {
				buff.append( "&#" + (int)ch + ";" );			
			}
			
		}
		
		return buff.toString();
		
	}
}
