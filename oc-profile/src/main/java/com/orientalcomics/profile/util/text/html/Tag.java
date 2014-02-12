// PROJECT : http://code.google.com/p/owaspHTML
package com.orientalcomics.profile.util.text.html;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 
 * A model for HTML "tags" and the rules dictating their validation/filtration. Also contains information
 * about their allowed attributes.
 * 
 * There is also some experimental (unused) code in here for generating a valid regular expression according to a policy
 * file on a per-tag basis.
 * 
 * @author Arshan Dabirsiaghi
 *
 */
class Tag {

	/*
	 * These are the fields pulled from the policy XML.
	 */
	private HashMap<String, Attribute> allowedAttributes = new HashMap<String, Attribute>();
    private List<ParentTag> parentTagNames = null;// 必须在此父Tag中(只要满足一个即可)
	private String name;
	private String action;
		
	/**
	 * 
	 * @return The action for this tag which is one of <code>filter</code>, <code>validate</code> or <code>remove</code>.
	 */
	public String getAction() {
		return action;
	}

	/**
	 * 
	 * @param action The new action for this tag which is one of <code>filter</code>, <code>validate</code> or <code>remove</code>.
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * Constructor.
	 * @param name The name of the tag, such as "b" for &lt;b&gt; tags.
	 */
	public Tag(String name) {
		this.name = name;
	}
	
	/**
	 * Adds a fully-built Attribute to the list of Attributes allowed for this tag.
	 * @param attr The Attribute to add to the list of allowed Attributes.
	 */
	public void addAttribute(Attribute attr) {
		allowedAttributes.put(attr.getName().toLowerCase(),attr);
	}
	
	public void addParentTag(ParentTag parentTag) {
	    if(parentTagNames == null){
	        parentTagNames = new ArrayList<ParentTag>();
	    }
	    parentTagNames.add(parentTag);
    }
	
	
	/* --------------------------------------------------------------------------------------------------*/
	
	
	
	/**
	 * Returns a regular expression for validating individual tags. Not used by the HTML scanner, but you might find some use for this.
	 * @return A regular expression for the tag, i.e., "^<b>$", or "<hr(\s)*(width='((\w){2,3}(\%)*)'>"
	 */
	
	public String getRegularExpression() {
		
		StringBuffer regExp;
		
		/*
		 * For such tags as <b>, <i>, <u>
		 */
		if ( allowedAttributes.size() == 0 ) {
			return "^<" + name + ">$";
		}
		
		regExp = new StringBuffer("<"+ ANY_NORMAL_WHITESPACES + name + OPEN_TAG_ATTRIBUTES);
		
		Iterator<String> attributes = allowedAttributes.keySet().iterator();

		while ( attributes.hasNext() ) {
			
			Attribute attr = (Attribute) allowedAttributes.get((String)attributes.next());
			// <p (id=#([0-9.*{6})|sdf).*>
			
			regExp.append(attr.getName() + ANY_NORMAL_WHITESPACES + "=" + ANY_NORMAL_WHITESPACES + "\"" + OPEN_ATTRIBUTE);
			
			Iterator<?> allowedValues = attr.getAllowedValues().iterator();
			Iterator<?> allowedRegExps = attr.getAllowedRegExp().iterator();
			
			if ( attr.getAllowedRegExp().size() + attr.getAllowedValues().size() > 0 ) {
				
				/*
				 * Go through and add static values to the regular expression.
				 */
				while( allowedValues.hasNext()) {
					String allowedValue = (String)allowedValues.next();
					
					regExp.append( escapeRegularExpressionCharacters(allowedValue) );
					
					if ( allowedValues.hasNext() || allowedRegExps.hasNext() ) {
						regExp.append(ATTRIBUTE_DIVIDER);
					}
				}
				
				/*
				 * Add the regular expressions for this attribute value to the mother regular expression.
				 */
				while (allowedRegExps.hasNext() ) {
					Pattern allowedRegExp = (Pattern)allowedRegExps.next();
					regExp.append(allowedRegExp.pattern());
					
					if (allowedRegExps.hasNext()) {
						regExp.append(ATTRIBUTE_DIVIDER);
					}
				}
				
				if ( attr.getAllowedRegExp().size() + attr.getAllowedValues().size() > 0 ) {
					regExp.append(CLOSE_ATTRIBUTE);
				}
				
				regExp.append("\"" + ANY_NORMAL_WHITESPACES);
				
				if ( attributes.hasNext() ) {
					regExp.append(ATTRIBUTE_DIVIDER);
				}
			}
			
		}

		regExp.append(CLOSE_TAG_ATTRIBUTES + ANY_NORMAL_WHITESPACES + ">");
		
		return regExp.toString();
	}

	private String escapeRegularExpressionCharacters(String allowedValue) {

		String toReturn = allowedValue;
		
		if ( toReturn == null ) {
			return null;
		}
		
		for(int i=0;i<REGEXP_CHARACTERS.length();i++) {
		    toReturn = toReturn.replaceAll("\\" + String.valueOf(REGEXP_CHARACTERS.charAt(i)),"\\"+REGEXP_CHARACTERS.charAt(i));
		}
		
		return toReturn;
	}

	/** Begin Variables Needed For Generating Regular Expressions **/
	private final static String ANY_NORMAL_WHITESPACES = "(\\s)*";
	private final static String OPEN_ATTRIBUTE = "(";
	private final static String ATTRIBUTE_DIVIDER = "|";
	private final static String CLOSE_ATTRIBUTE = ")";
	//private final static String OPEN_VALUES = "(";
	//private final static String VALUE_DIVIDER = "|";
	//private final static String CLOSE_VALUE = ")";
	private final static String OPEN_TAG_ATTRIBUTES = ANY_NORMAL_WHITESPACES + OPEN_ATTRIBUTE;
	private final static String CLOSE_TAG_ATTRIBUTES = ")*";
	private final static String REGEXP_CHARACTERS = "\\(){}.*?$^-+";


	/**
	 * 
	 * @return A <code>HashMap</code> of allowed attributes that the tag is allowed to contain.
	 */
	public HashMap<String, Attribute> getAllowedAttributes() {
		return allowedAttributes;
	}

	/**
	 * @param allowedAttributes The new <code>HashMap</code> of allowed attributes that the tag is allowed to contain.
	 */
	public void setAllowedAttributes(HashMap<String, Attribute> allowedAttributes) {
		this.allowedAttributes = allowedAttributes;
	}

	public List<ParentTag> getParentTagNames(){
	    return parentTagNames;
	}
	
	/**
	 * 
	 * @return The String name of the tag.
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name The new name of the tag.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns an <code>Attribute</code> associated with a lookup name.
	 * @param name The name of the allowed attribute by name.
	 * @return The <code>Attribute</code> object associated with the name, or 
	 */
	public Attribute getAttributeByName(String name) {

		return (Attribute) allowedAttributes.get(name);
		
	}

}
