// PROJECT : http://code.google.com/p/owaspantisamy
package com.orientalcomics.profile.util.text.html;



class Constants {
	public static String[] allowedEmptyTags = {
			"br", "hr", "a", "img", "link", "iframe", "script", "object", "applet", "frame", "base", "param", "meta", "input", "textarea", "embed", "basefont", "col"
	};

	public static final String DEFAULT_ENCODING_ALGORITHM = "UTF-8";

	public static final Tag BASIC_PARAM_TAG_RULE;

	static {
		Attribute paramNameAttr = new Attribute("name");
		Attribute paramValueAttr = new Attribute("value");
		paramNameAttr.addAllowedRegExp(HtmlCorrectRule.ANYTHING_REGEXP);
		paramValueAttr.addAllowedRegExp(HtmlCorrectRule.ANYTHING_REGEXP);
		BASIC_PARAM_TAG_RULE = new Tag("param");
		BASIC_PARAM_TAG_RULE.addAttribute(paramNameAttr);
		BASIC_PARAM_TAG_RULE.addAttribute(paramValueAttr);
		BASIC_PARAM_TAG_RULE.setAction(HtmlCorrectRule.ACTION_VALIDATE);
	}

	public static final String DEFAULT_LOCALE_LANG = "en";
	public static final String DEFAULT_LOCALE_LOC = "US";

}
