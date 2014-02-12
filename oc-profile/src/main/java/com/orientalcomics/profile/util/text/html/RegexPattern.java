// PROJECT : http://code.google.com/p/owaspantisamy
package com.orientalcomics.profile.util.text.html;

import java.util.regex.Pattern;

/**
 * 
 * An extension of the Pattern to give it a "lookup name" that we can use from a
 * centralized store.
 * 
 * @author Arshan Dabirsiaghi
 *
 */
class RegexPattern {
	
	private String name;
	private Pattern pattern;
	
	/**
	 * Constructor for RegexPattern. The "name" parameter is a lookup name for retrieving the Pattern parameter
	 * passed in later.
	 * @param name The lookup name by which we will retrieve this Pattern later.
	 * @param pattern The Pattern to lookup based on the "name".
	 */
	public RegexPattern(String name, Pattern pattern) {
		this.name = name;
		this.pattern = pattern;
	}

	/**
	 * 
	 * @return Return the name of the <code>RegexPattern</code>.
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name Set the name of the <code>RegexPattern</code>.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Return the Pattern of the <code>RegexPattern</code>.
	 */
	public Pattern getPattern() {
		return pattern;
	}

	/**
	 * @param pattern Set the Pattern of the <code>RegexPattern</code>.
	 */
	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}
}
