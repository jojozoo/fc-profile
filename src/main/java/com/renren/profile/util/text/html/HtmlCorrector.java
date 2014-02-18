// PROJECT : http://code.google.com/p/owaspantisamy
package com.renren.profile.util.text.html;

import java.io.File;

import com.renren.profile.util.sys.ProfileException;

/**
 * 
 * This is the only class from which the outside world should be calling. The
 * <code>scan()</code> method holds the meat and potatoes of AntiSamy. The file
 * contains a number of ways for <code>scan()</code>'ing depending on the
 * accessibility of the htmlCorrectRule file.
 * 
 * @author Arshan Dabirsiaghi
 * 
 */

public class HtmlCorrector {
	private HtmlCorrectRule htmlCorrectRule = null;

	public HtmlCorrector() {
	}

	public HtmlCorrector(HtmlCorrectRule htmlCorrectRule) {
		this.htmlCorrectRule = htmlCorrectRule;
	}

	/**
	 * The meat and potatoes. The <code>scan()</code> family of methods are the
	 * only methods the outside world should be calling to invoke AntiSamy.
	 * 
	 * @param taintedHTML
	 *            Untrusted HTML which may contain malicious code.
	 * @param inputEncoding
	 *            The encoding of the input.
	 * @param outputEncoding
	 *            The encoding that the output should be in.
	 * @return A <code>CleanResults</code> object which contains information
	 *         about the scan (including the results).
	 * @throws <code>ProfileException</code> When there is a problem encountered
	 *         while scanning the HTML.
	 * @throws <code>ProfileException</code> When there is a problem reading the
	 *         htmlCorrectRule file.
	 */

	public CleanResults scan(String taintedHTML) throws ProfileException {

		if (htmlCorrectRule == null) {
			throw new ProfileException("No htmlCorrectRule loaded");
		}

		return this.scan(taintedHTML, this.htmlCorrectRule);
	}

	public CleanResults scan(String taintedHTML, HtmlCorrectRule htmlCorrectRule) throws ProfileException {
        return new HtmlSAXScanner(htmlCorrectRule).scan(taintedHTML);
	}

	/**
	 * This method wraps <code>scan()</code> using the htmlCorrectRule object passed in.
	 */
	public CleanResults scan(String taintedHTML, String filename) throws ProfileException {

		HtmlCorrectRule htmlCorrectRule = null;

		/*
		 * Get or reload the htmlCorrectRule document (antisamy.xml). We'll need to pass
		 * that to the scanner so it knows what to look for.
		 */
		htmlCorrectRule = HtmlCorrectRule.getInstance(filename);

		return this.scan(taintedHTML, htmlCorrectRule);
	}

	/**
	 * This method wraps <code>scan()</code> using the htmlCorrectRule File object passed
	 * in.
	 */
	public CleanResults scan(String taintedHTML, File htmlCorrectRuleFile) throws ProfileException {

		HtmlCorrectRule htmlCorrectRule = null;

		/*
		 * Get or reload the htmlCorrectRule document (antisamy.xml). We'll need to pass
		 * that to the scanner so it knows what to look for.
		 */
		htmlCorrectRule = HtmlCorrectRule.getInstance(htmlCorrectRuleFile);

		return this.scan(taintedHTML, htmlCorrectRule);
	}

	public HtmlCorrectRule getHtmlCorrectRule() {
		return htmlCorrectRule;
	}

	public void setHtmlCorrectRule(HtmlCorrectRule htmlCorrectRule) {
		this.htmlCorrectRule = htmlCorrectRule;
	}
}
