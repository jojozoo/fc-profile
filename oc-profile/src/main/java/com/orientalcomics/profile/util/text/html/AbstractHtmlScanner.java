// PROJECT : http://code.google.com/p/owaspantisamy
package com.orientalcomics.profile.util.text.html;

import java.util.ArrayList;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.orientalcomics.profile.exception.ProfileException;


public abstract class AbstractHtmlScanner {

	protected HtmlCorrectRule policy;
	protected ArrayList<String> errorMessages = new ArrayList<String>();

	protected ResourceBundle messages;
	protected Locale locale = Locale.getDefault();

	protected boolean isNofollowAnchors = false;
	protected boolean isValidateParamAsEmbed = false;

	public abstract CleanResults scan(String html) throws ProfileException;

	public abstract CleanResults getResults();

	public AbstractHtmlScanner(HtmlCorrectRule policy) {
		this.policy = policy;
		initializeErrors();
	}

	public AbstractHtmlScanner() throws ProfileException {
		policy = HtmlCorrectRule.getInstance();
		initializeErrors();
	}

	protected void initializeErrors() {
	    String baseName = this.getClass().getPackage().getName()+".resources.html-corrector";
		try {
			messages = ResourceBundle.getBundle(baseName, locale);
		} catch (MissingResourceException mre) {
			messages = ResourceBundle.getBundle(baseName, new Locale(Constants.DEFAULT_LOCALE_LANG, Constants.DEFAULT_LOCALE_LOC));
		}
	}

	protected void addError(String errorKey, Object[] objs) {
		errorMessages.add(ErrorMessageUtil.getMessage(messages, errorKey, objs));
	}
}
