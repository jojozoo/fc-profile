// PROJECT : http://code.google.com/p/owaspantisamy
package com.orientalcomics.profile.util.text.html;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * A model for CSS properties and the "rules" they must follow (either literals
 * or regular expressions) in order to be considered valid.
 * 
 * @author Jason Li
 * 
 */
class Property {
	private final String name;

	private String onInvalid;

	private String description;

	private List<String> allowedValues = new ArrayList<String>();

	private List<Pattern> allowedRegExp = new ArrayList<Pattern>();

	private List<String> shorthandRefs = new ArrayList<String>();

	public Property(String name) {
		this.name = name;
	}
	
	/**
	 * Add the specified value to the allowed list of valid values.
	 * @param safeValue The new valid value to add to the list.
	 */
	public void addAllowedValue(String safeValue) {
		this.allowedValues.add(safeValue);
	}
	
	/**
	 * Add the specified value to the allowed list of valid regular expressions.
	 * @param safeRegExpValue The new valid regular expression to add to the list.
	 */
	public void addAllowedRegExp(Pattern safeRegExpValue) {
		this.allowedRegExp.add(safeRegExpValue);
	}
	
	/**
	 * Add the specified value to the allowed list of valid shorthand values.
	 * @param shorthandValue The new valid shorthand value to add to the list.
	 */
	public void addShorthandRef(String shorthandValue) {
		this.shorthandRefs.add(shorthandValue);
	}

	/**
	 * Return a <code>List</code> of allowed regular expressions as added
	 * by the <code>addAllowedRegExp()</code> method.
	 * @return A <code>List</code> of allowed regular expressions.
	 */
	public List<Pattern> getAllowedRegExp() {
		return allowedRegExp;
	}
	
	/**
	 * Set a new <code>List</code> of allowed regular expressions.
	 * @param allowedRegExp The new <code>List</code> of allowed regular expressions.
	 */
	public void setAllowedRegExp(List<Pattern> allowedRegExp) {
		this.allowedRegExp = allowedRegExp;
	}

	/**
	 * @return A <code>List</code> of allowed literal values.
	 */
	public List<String> getAllowedValues() {
		return allowedValues;
	}

	/**
	 * Set a new <code>List</code> of allowed literal values.
	 * @param allowedValues The new <code>List</code> of allowed literal values.
	 */
	public void setAllowedValues(List<String> allowedValues) {
		this.allowedValues = allowedValues;
	}

	/**
	 * @return A <code>List</code> of allowed shorthand references.
	 */
	public List<String> getShorthandRefs() {
		return shorthandRefs;
	}

	/**
	 * Set a new <code>List</code> of allowed shorthand references.
	 * @param shorthandRefs The new <code>List</code> of allowed shorthand references.
	 */
	public void setShorthandRefs(List<String> shorthandRefs) {
		this.shorthandRefs = shorthandRefs;
	}
	
	/**
	 * 
	 * @return The name of the property.
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return The <code>onInvalid</code> action associated with the Property.
	 */
	public String getOnInvalid() {
		return onInvalid;
	}

	/**
	 * 
	 * @param onInvalid The new <code>onInvalid</code> action to define for this property.
	 */
	public void setOnInvalid(String onInvalid) {
		this.onInvalid = onInvalid;
	}

	/**
	 * 
	 * @return The description associated with this Property.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * @param description The new description of this Property.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}
