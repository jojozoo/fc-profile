// PROJECT : http://code.google.com/p/owaspantisamy
package com.orientalcomics.profile.util.text.html;

import java.util.ArrayList;
import java.util.Date;

import org.w3c.dom.DocumentFragment;

/**
 * This class contains the results of a scan.
 * 
 * The list of error messages (<code>errorMessages</code>) will let the user
 * know what, if any HTML errors existed, and what, if any, security or
 * validation-related errors existed, and what was done about them.
 * 
 * @author Arshan Dabirsiaghi
 * 
 */

class CleanResults {

	private ArrayList<String> errorMessages = new ArrayList<String>();
	private String cleanHTML;
	private Date startOfScan;
	private Date endOfScan;

	private DocumentFragment cleanXMLDocumentFragment;

	/*
	 * For extension.
	 */
	public CleanResults() {

	}

	public CleanResults(Date startOfScan, Date endOfScan, String cleanHTML,
			DocumentFragment XMLDocumentFragment, ArrayList<String> errorMessages) {
		this.startOfScan = startOfScan;
		this.endOfScan = endOfScan;
		this.cleanXMLDocumentFragment = XMLDocumentFragment;
		this.cleanHTML = cleanHTML;
		this.errorMessages = errorMessages;
	}

	/**
	 * This is called at the beginning of the scan to initialize the start time
	 * and create a new CleanResults object.
	 * 
	 * @param date
	 *            The begin time of the scan.
	 */
	public CleanResults(Date date) {
		this.startOfScan = date;
	}

	public DocumentFragment getCleanXMLDocumentFragment() {
		return cleanXMLDocumentFragment;
	}

	public void setCleanHTML(String cleanHTML) {
		this.cleanHTML = cleanHTML;
	}

	/**
	 * Return the filtered HTML as a String.
	 * 
	 * @return A String object which contains the serialized, safe HTML.
	 */
	public String getCleanHTML() {
		return cleanHTML;
	}

	/**
	 * Return a list of error messages.
	 * 
	 * @return An ArrayList object which contain the error messages after a
	 *         scan.
	 */
	public ArrayList<String> getErrorMessages() {
		return errorMessages;
	}

	/**
	 * Return the time when scan finished.
	 * 
	 * @return A Date object indicating the moment the scan finished.
	 */
	public Date getEndOfScan() {

		return endOfScan;
	}

	/**
	 * Return the time when scan started.
	 * 
	 * @return A Date object indicating the moment the scan started.
	 */
	public Date getStartOfScan() {
		return startOfScan;
	}

	/**
	 * Return the time elapsed during the scan.
	 * 
	 * @return A double primitive indicating the amount of time elapsed between
	 *         the beginning and end of the scan in seconds.
	 */
	public double getScanTime() {
		return (endOfScan.getTime() - startOfScan.getTime()) / 1000D;
	}

	/**
	 * Add an error message to the aggregate list of error messages during
	 * filtering.
	 * 
	 * @param msg
	 *            An error message to append to the list of aggregate error
	 *            messages during filtering.
	 */
	public void addErrorMessage(String msg) {
		errorMessages.add(msg);
	}

	/**
	 * Return the number of errors encountered during filtering.
	 */
	public int getNumberOfErrors() {
		return errorMessages.size();
	}

}
