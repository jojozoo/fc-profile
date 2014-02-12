// PROJECT : http://code.google.com/p/owaspantisamy
package com.orientalcomics.profile.util.text.html;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.batik.css.parser.ParseException;
import org.apache.batik.css.parser.Parser;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpContentTooLargeException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.w3c.css.sac.InputSource;

import com.orientalcomics.profile.exception.ProfileException;


/**
 * Encapsulates the parsing and validation of a CSS stylesheet or inline
 * declaration. To make use of this class, instantiate the scanner with the
 * desired policy and call either <code>scanInlineSheet()</code> or
 * <code>scanStyleSheet</code> as appropriate.
 * 
 * @see #scanInlineStyle(String, String)
 * @see #scanStyleSheet(String)
 * 
 * @author Jason Li
 */
class CssScanner {

    private static final int DEFAULT_TIMEOUT = 1000;

    private static final String CDATA = "^\\s*<!\\[CDATA\\[(.*)\\]\\]>\\s*$";
    
    /**
     * The parser to be used in any scanning
     */
    private final Parser parser = new Parser();

    /**
     * The policy file to be used in any scanning
     */
    private final HtmlCorrectRule policy;

    /**
     * The message bundled to pull error messages from.
     */
    private final ResourceBundle messages;
    
    /**
     * Constructs a scanner based on the given policy.
     * 
     * @param policy
     *                the policy to follow when scanning
     */
    public CssScanner(HtmlCorrectRule policy, ResourceBundle messages) {
    	this.policy = policy;
    	this.messages = messages;
    }

    /**
     * Scans the contents of a full stylesheet (ex. a file based stylesheet
     * or the complete stylesheet contents as declared within &lt;style&gt;
     * tags)
     * 
     * @param taintedCss
     *                a <code>String</code> containing the contents of the
     *                CSS stylesheet to validate
     * @param sizeLimit
     *                the limit on the total size in bytes of any imported
     *                stylesheets
     * @return a <code>CleanResuts</code> object containing the results of
     *         the scan
     * @throws ProfileException
     *                 if an error occurs during scanning
     */
    public CleanResults scanStyleSheet(String taintedCss, int sizeLimit)
	    throws ProfileException {

	Date startOfScan = new Date();
	ArrayList<String> errorMessages = new ArrayList<String>();

	/* Check to see if the text starts with (\s)*<![CDATA[
	 * and end with ]]>(\s)*.
	 */

	Pattern p = Pattern.compile(CDATA, Pattern.DOTALL);
	Matcher m = p.matcher(taintedCss);
	
	boolean isCdata = m.matches();
	
	if ( isCdata ) {
		taintedCss = m.group(1);
	}
	
	// Create a queue of all style sheets that need to be validated to
	// account for any sheets that may be imported by the current CSS
	LinkedList<URI> stylesheets = new LinkedList<URI>();

	CssHandler handler = new CssHandler(policy, stylesheets, errorMessages, messages);

	// parse the stylesheet
	parser.setDocumentHandler(handler);

	try {
	    // parse the style declaration
	    // note this does not count against the size limit because it
	    // should already have been counted by the caller since it was
	    // embedded in the HTML
	    parser
		    .parseStyleSheet(new InputSource(new StringReader(
			    taintedCss)));
	} catch (IOException ioe) {
	    throw new ProfileException(ioe);
	    
	/*
	 * ParseExceptions, from batik, is unfortunately a RuntimeException.
	 */
	} catch (ParseException pe) {
		throw new ProfileException(pe);
	}

	parseImportedStylesheets(stylesheets, handler, errorMessages, sizeLimit);

	String cleaned = handler.getCleanStylesheet();
	
	if ( isCdata && ! "true".equals(policy.getDirective(HtmlCorrectRule.USE_XHTML)) ) {
		cleaned = "<![CDATA[[" + cleaned + "]]>";
	}
	
	return new CleanResults(startOfScan, new Date(), 
			cleaned, 
			null, errorMessages);
    }

    /**
     * Scans the contents of an inline style declaration (ex. in the style
     * attribute of an HTML tag) and validates the style sheet according to
     * this <code>CssScanner</code>'s policy file.
     * 
     * @param taintedCss
     *                a <code>String</code> containing the contents of the
     *                CSS stylesheet to validate
     * @param tagName
     *                the name of the tag for which this inline style was
     *                declared
     * 
     * @param sizeLimit
     *                the limit on the total size in bites of any imported
     *                stylesheets
     * @return a <code>CleanResuts</code> object containing the results of
     *         the scan
     * @throws ProfileException
     *                 if an error occurs during scanning
     */
    public CleanResults scanInlineStyle(String taintedCss, String tagName,
	    int sizeLimit) throws ProfileException {

	Date startOfScan = new Date();

	ArrayList<String> errorMessages = new ArrayList<String>();

	// Create a queue of all style sheets that need to be validated to
	// account for any sheets that may be imported by the current CSS
	LinkedList<URI> stylesheets = new LinkedList<URI>();

	CssHandler handler = new CssHandler(policy, stylesheets, errorMessages,tagName, messages);

	parser.setDocumentHandler(handler);

	try {
	    // parse the inline style declaration
	    // note this does not count against the size limit because it
	    // should already have been counted by the caller since it was
	    // embedded in the HTML
	    parser.parseStyleDeclaration(taintedCss);
	} catch (IOException ioe) {
	    throw new ProfileException(ioe);
	}

	parseImportedStylesheets(stylesheets, handler, errorMessages, sizeLimit);

	return new CleanResults(startOfScan, new Date(), handler
		.getCleanStylesheet(), null, errorMessages);
    }

    /**
     * Parses through a <code>LinkedList</code> of imported stylesheet
     * URIs, this method parses through those stylesheets and validates them
     * 
     * @param stylesheets
     *                the <code>LinkedList</code> of stylesheet URIs to
     *                parse
     * @param handler
     *                the <code>CssHandler</code> to use for parsing
     * @param errorMessages
     *                the list of error messages to append to
     * @param sizeLimit
     *                the limit on the total size in bites of any imported
     *                stylesheets
     * @throws ProfileException
     *                 if an error occurs during scanning
     */
    private void parseImportedStylesheets(LinkedList<URI> stylesheets,
	    CssHandler handler, ArrayList<String> errorMessages, int sizeLimit)
	    throws ProfileException {

	int importedStylesheets = 0;

	// if stylesheets were imported by the inline style declaration,
	// continue parsing the nested styles. Note this only happens
	// if CSS importing was enabled in the policy file
	if (!stylesheets.isEmpty()) {
	    HttpClient httpClient = new HttpClient();

	    // Ensure that we have appropriate timeout values so we don't
	    // get DoSed waiting for returns
	    HttpConnectionManagerParams params = httpClient
		    .getHttpConnectionManager().getParams();

	    int timeout = DEFAULT_TIMEOUT;

	    try {
		timeout = Integer.parseInt(policy
			.getDirective(HtmlCorrectRule.CONNECTION_TIMEOUT));
	    } catch (NumberFormatException nfe) {
	    }

	    params.setConnectionTimeout(timeout);
	    params.setSoTimeout(timeout);
	    httpClient.getHttpConnectionManager().setParams(params);

	    int allowedImports = HtmlCorrectRule.DEFAULT_MAX_STYLESHEET_IMPORTS;
	    try {
		allowedImports = Integer.parseInt(policy
			.getDirective("maxStyleSheetImports"));
	    } catch (NumberFormatException nfe) {
	    }

	    while (!stylesheets.isEmpty()) {

		URI stylesheetUri = (URI) stylesheets.removeFirst();

		if (++importedStylesheets > allowedImports) {
		    errorMessages.add(ErrorMessageUtil.getMessage(
		    	messages,
			    ErrorMessageUtil.ERROR_CSS_IMPORT_EXCEEDED,
			    new Object[] {
				    HTMLEntityEncoder
					    .htmlEntityEncode(stylesheetUri
						    .toString()),
				    String.valueOf(allowedImports) }));
		    continue;
		}

		GetMethod stylesheetRequest = new GetMethod(stylesheetUri
			.toString());

		byte[] stylesheet = null;
		try {
		    // pull down stylesheet, observing size limit
		    httpClient.executeMethod(stylesheetRequest);
		    stylesheet = stylesheetRequest.getResponseBody(sizeLimit);
		} catch (HttpContentTooLargeException hctle) {
		    errorMessages
			    .add(ErrorMessageUtil
				    .getMessage(
				    	messages,
					    ErrorMessageUtil.ERROR_CSS_IMPORT_INPUT_SIZE,
					    new Object[] {
						    HTMLEntityEncoder
							    .htmlEntityEncode(stylesheetUri
								    .toString()),
						    String.valueOf(policy
							    .getMaxInputSize()) }));
		} catch (IOException ioe) {
		    errorMessages.add(ErrorMessageUtil
			    .getMessage(
			    	messages,
				    ErrorMessageUtil.ERROR_CSS_IMPORT_FAILURE,
				    new Object[] { HTMLEntityEncoder
					    .htmlEntityEncode(stylesheetUri
						    .toString()) }));
		} finally {
		    stylesheetRequest.releaseConnection();
		}

		if (stylesheet != null) {
		    // decrease the size limit based on the
		    sizeLimit -= stylesheet.length;

		    try {
			InputSource nextStyleSheet = new InputSource(
				new InputStreamReader(new ByteArrayInputStream(
					stylesheet)));
			parser.parseStyleSheet(nextStyleSheet);

		    } catch (IOException ioe) {
			throw new ProfileException(ioe);
		    }

		}
	    }
	}
    }
}
