// PROJECT : http://code.google.com/p/owaspantisamy
package com.renren.profile.util.text.html;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * A model for HTML attributes and the "rules" they must follow (either literals or regular expressions) in
 * order to be considered valid.
 * 
 * @author Arshan Dabirsiaghi
 *
 */

class Attribute implements Cloneable {

	private String name;
	private String description;
	private String onInvalid;
	private List<String> allowedValues = new ArrayList<String>();
	private List<Pattern> allowedRegExp = new ArrayList<Pattern>();
	
	public Attribute(String name) {
		this.name = name;
	}
	
	/**
	 * 
	 * @param safeValue A legal literal value that an attribute can have, according to the Policy
	 */
	public void addAllowedValue(String safeValue) {
		this.allowedValues.add(safeValue);
	}
	
	/**
	 * 
	 * @param safeRegExpValue A legal regular expression value that an attribute could have, according to the Policy
	 */
	public void addAllowedRegExp(Pattern safeRegExpValue) {
		this.allowedRegExp.add(safeRegExpValue);
	}

	/**
	 *  
	 * @return A <code>List</code> of regular expressions that an attribute can be validated from.
	 */
	public List<Pattern> getAllowedRegExp() {
		return allowedRegExp;
	}
	
	/**
	 * 
	 * @param allowedRegExp A <code>List</code> of regular expressions that an attribute can be validated from.
	 */
	public void setAllowedRegExp(List<Pattern> allowedRegExp) {
		this.allowedRegExp = allowedRegExp;
	}

	/**
	 * 
	 * @return A <code>List</code> of literal values that an attribute could have, according to the Policy.
	 */
	public List<String> getAllowedValues() {
		return allowedValues;
	}

	/**
	 * 
	 * @param allowedValues A <code>List</code> of regular expressions that an attribute can be validated from.
	 */
	public void setAllowedValues(List<String> allowedValues) {
		this.allowedValues = allowedValues;
	}

	/**
	 * 
	 * @return The name of an Attribute object.
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name The new name of an Attribute object.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return The <code>onInvalid</code> value a tag could have, from the list of "filterTag", "removeTag" and "removeAttribute" 
	 */
	public String getOnInvalid() {
		return onInvalid;
	}

	
	/**
	 * 
	 * @param onInvalid The new <code>onInvalid</code> value of an Attribute object.
	 */
	public void setOnInvalid(String onInvalid) {
		this.onInvalid = onInvalid;
	}

	/**
	 * 
	 * @return The description of what the tag does.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * @param description The new description of what the tag does.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * We need to implement <code>clone()</code> to make the Policy file work with common attributes and the ability
	 * to use a common-attribute with an alternative <code>onInvalid</code> action.
	 */
	public Object clone() {
		
		Attribute toReturn = new Attribute(name);
		
		toReturn.setDescription(description);
		toReturn.setOnInvalid(onInvalid);
		toReturn.setAllowedValues(allowedValues);
		toReturn.setAllowedRegExp(allowedRegExp);
		
		return toReturn;
	}
}
