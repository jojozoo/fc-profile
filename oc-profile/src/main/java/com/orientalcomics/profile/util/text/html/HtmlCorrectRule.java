package com.orientalcomics.profile.util.text.html;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.orientalcomics.profile.exception.ProfileException;
import com.orientalcomics.profile.util.common.NumberUtils0;

/**
 * Policy.java
 *
 * This file holds the model for our policy engine.
 *
 * @author Arshan Dabirsiaghi
 *
 */

class HtmlCorrectRule {

	public static final Pattern ANYTHING_REGEXP = Pattern.compile(".*");

	private static final String DEFAULT_POLICY_URI = "resources/html-corrector.xml";
	private static final String DEFAULT_ONINVALID = "removeAttribute";

	public static final int DEFAULT_MAX_INPUT_SIZE = 100000;
	public static final int DEFAULT_MAX_STYLESHEET_IMPORTS = 1;

	public static final String OMIT_XML_DECLARATION = "omitXmlDeclaration";
	public static final String OMIT_DOCTYPE_DECLARATION = "omitDoctypeDeclaration";
	public static final String MAX_INPUT_SIZE = "maxInputSize";
	public static final String USE_XHTML = "useXHTML";
	public static final String FORMAT_OUTPUT = "formatOutput";
	public static final String EMBED_STYLESHEETS = "embedStyleSheets";
	public static final String CONNECTION_TIMEOUT = "connectionTimeout";
	public static final String ANCHORS_NOFOLLOW = "nofollowAnchors";
	public static final String VALIDATE_PARAM_AS_EMBED = "validateParamAsEmbed";
	public static final String PRESERVE_SPACE = "preserveSpace";
	public static final String PRESERVE_COMMENTS = "preserveComments";
    public static final String ACTION_WHEN_AGAIN_PARENT_TAGS = "actionWhenAgainParentTags";// remove or filter
    
    public static final String DEFAULT_ACTION_WHEN_AGAIN_PARENT_TAGS = "filter";
	
	public static final String ENCODE_TAGS = "onUnknownTag";
	
	public static final String ACTION_VALIDATE	= "validate";
	public static final String ACTION_FILTER	= "filter";
	public static final String ACTION_TRUNCATE	= "truncate";

	private static char REGEXP_BEGIN = '^';
	private static char REGEXP_END = '$';

	private HashMap<String, RegexPattern> commonRegularExpressions	= new HashMap<String, RegexPattern>();
	private HashMap<String, Attribute> commonAttributes			= new HashMap<String, Attribute>();
	private HashMap<String, Tag> tagRules					= new HashMap<String, Tag>();
	private HashMap<String, Property> cssRules					= new HashMap<String, Property>();
	private HashMap<String, String> directives					= new HashMap<String, String>();
	private HashMap<String, Attribute> globalAttributes			= new HashMap<String, Attribute>();
	private Set<String>		encodeTags					= new HashSet<String>();

	private ArrayList<String> tagNames;

	/** The path to the base policy file, used to resolve relative paths when reading included files */
	private static URL baseUrl					= null;

	public boolean isTagInListToEncode(String s) {
		return encodeTags.contains(s);
	}

	/**
	 * Retrieves a Tag from the Policy.
	 * @param tagName The name of the Tag to look up.
	 * @return The Tag associated with the name specified, or null if none is found.
	 */
	public Tag getTagByName(String tagName) {

		return (Tag) tagRules.get(tagName.toLowerCase());

	}

	/**
	 * Retrieves a CSS Property from the Policy.
	 * @param propertyName The name of the CSS Property to look up.
	 * @return The CSS Property associated with the name specified, or null if none is found.
	 */
	public Property getPropertyByName(String propertyName) {

		return (Property) cssRules.get(propertyName.toLowerCase());

	}

	/**
	 * This retrieves a Policy based on a default location ("resources/antisamy.xml")
	 * @return A populated Policy object based on the XML policy file located in the default location.
	 * @throws ProfileException If the file is not found or there is a problem parsing the file.
	 */
	public static HtmlCorrectRule getInstance() throws ProfileException {
		return getInstance(DEFAULT_POLICY_URI);
	}

	/**
	 * This retrieves a Policy based on the file name passed in
	 * @param filename The path to the XML policy file.
	 * @return A populated Policy object based on the XML policy file located in the location passed in.
	 * @throws ProfileException If the file is not found or there is a problem parsing the file.
	 */
	public static HtmlCorrectRule getInstance(String filename) throws ProfileException {
		File file = new File(filename);
		return getInstance(file);
	}

	/**
	 * This retrieves a Policy based on the File object passed in
	 * @param file A File object which contains the XML policy information.
	 * @return A populated Policy object based on the XML policy file pointed to by the File parameter.
	 * @throws ProfileException If the file is not found or there is a problem parsing the file.
	 */
	public static HtmlCorrectRule getInstance(File file) throws ProfileException {
		try {
			URI uri = file.toURI();
			return getInstance(uri.toURL());
		} catch (IOException e) {
			throw new ProfileException(e);
		}
	}


	/**
	 * This retrieves a Policy based on the URL object passed in.
	 *
	 * NOTE: This is the only factory method that will work with <include> tags
	 * in AntiSamy policy files.
	 * 
	 * @param url A URL object which contains the XML policy information.
	 * @return A populated Policy object based on the XML policy file pointed to by the File parameter.
	 * @throws ProfileException If the file is not found or there is a problem parsing the file.
	 */
	public static HtmlCorrectRule getInstance(URL url) throws ProfileException {

		if (baseUrl == null) setBaseURL(url);
		return new HtmlCorrectRule(url);			
	}

	/**
	 * This retrieves a Policy based on the InputStream object passed in
	 * @param inputStream An InputStream which contains thhe XML policy information.
	 * @return A populated Policy object based on the XML policy file pointed to by the inputStream parameter.
	 * @throws ProfileException If there is a problem parsing the input stream.
	 * @deprecated This method does not properly load included policy files. Use getInstance(URL) instead.
	 */
	public static HtmlCorrectRule getInstance(InputStream inputStream) throws ProfileException {

		return new HtmlCorrectRule(inputStream);

	}

	/**
	 * Load the policy from a URL.
	 *
	 * @param filename Load a policy from the filename specified.
	 * @throws ProfileException
	 */
	private HtmlCorrectRule(URL url) throws ProfileException {
		try {

			InputSource source = resolveEntity(null, url.toExternalForm());
			if (source == null) {
				source = new InputSource(url.toExternalForm());
				source.setByteStream(url.openStream());
			} else {
				source.setSystemId(url.toExternalForm());
			}
			DocumentBuilderFactory dbf = new DocumentBuilderFactoryImpl();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document dom = null;

			/**
			 * Load and parse the file.
			 */
			dom = db.parse(source);


			/**
			 * Get the policy information out of it!
			 */
			Element topLevelElement = dom.getDocumentElement();


			/**
			 * Are there any included policies? These are parsed here first so that
			 * rules in _this_ policy file will override included rules.
			 *
			 * NOTE that by this being here we only support one level of includes.
			 * To support recursion, move this into the parsePolicy method.
			 */
			NodeList includes = topLevelElement.getElementsByTagName("include");
			for (int i = 0; i < includes.getLength(); i++) {
				Element include = (Element) includes.item(i);

				String href = XMLUtil.getAttributeValue(include, "href");

				Element includedPolicy = getPolicy(href);

				parsePolicy(includedPolicy);
			}

			/**
			 * Parse the top level element itself
			 */
			parsePolicy(topLevelElement);


		} catch (SAXException e) {
			throw new ProfileException(e);
		} catch (ParserConfigurationException e) {
			throw new ProfileException(e);
		} catch (IOException e) {
			throw new ProfileException(e);
		}
	}

	/**
	 * Load the policy from an XML file.
	 * @param filename Load a policy from the filename specified.
	 * @throws ProfileException
	 * @deprecated This constructor does not properly load included policy files. Use Policy(URL) instead.
	 */
	private HtmlCorrectRule (InputStream is) throws ProfileException {

		try {

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document dom = null;

			/**
			 * Load and parse the file.
			 */
			dom = db.parse(is);


			/**
			 * Get the policy information out of it!
			 */

			Element topLevelElement = dom.getDocumentElement();

			/**
			 * Parse the top level element itself
			 */
			parsePolicy(topLevelElement);

		} catch (SAXException e) {
			throw new ProfileException(e);
		} catch (ParserConfigurationException e) {
			throw new ProfileException(e);
		} catch (IOException e) {
			throw new ProfileException(e);
		}
	}

	private void parsePolicy(Element topLevelElement)
			throws ProfileException {

		if (topLevelElement == null) return;

		/**
		 * First thing to read is the <common-regexps> section.
		 */
		Element commonRegularExpressionListNode = (Element) topLevelElement.getElementsByTagName("common-regexps").item(0);
		parseCommonRegExps(commonRegularExpressionListNode);

		/**
		 * Next we read in the directives.
		 */
		Element directiveListNode = (Element)topLevelElement.getElementsByTagName("directives").item(0);
		parseDirectives(directiveListNode);


		/**
		 * Next we read in the common attributes.
		 */
		Element commonAttributeListNode = (Element)topLevelElement.getElementsByTagName("common-attributes").item(0);
		parseCommonAttributes(commonAttributeListNode);

		/**
		 * Next we need the global tag attributes (id, style, etc.)
		 */
		Element globalAttributeListNode = (Element)topLevelElement.getElementsByTagName("global-tag-attributes").item(0);
		parseGlobalAttributes(globalAttributeListNode);

		/**
		 * Next we read in the tags that should be encoded when they're encountered like <g>.
		 */
		NodeList tagsToEncodeList = topLevelElement.getElementsByTagName("tags-to-encode");
		if (tagsToEncodeList != null && tagsToEncodeList.getLength() != 0) {
			parseTagsToEncode((Element) tagsToEncodeList.item(0));
		}
		
		/**
		 * Next, we read in the tag restrictions.
		 */
		Element tagListNode = (Element)topLevelElement.getElementsByTagName("tag-rules").item(0);
		parseTagRules(tagListNode);

		/**
		 * Finally, we read in the CSS rules.
		 */
		Element cssListNode = (Element)topLevelElement.getElementsByTagName("css-rules").item(0);

		parseCSSRules(cssListNode);

	}


	/**
	 * Returns the top level element of a loaded policy Document
	 */
	private Element getPolicy(String href)
			throws IOException, SAXException, ParserConfigurationException {
		
		InputSource source = null;

		 // Can't resolve public id, but might be able to resolve relative
        // system id, since we have a base URI.
        if (href != null && baseUrl != null) {
            URL url;

            try {
                url = new URL(baseUrl, href);
                source = new InputSource(url.openStream());
                source.setSystemId(href);

			} catch (MalformedURLException except) {
                try {
                    String absURL = URIUtils.resolveAsString(href, baseUrl.toString());
                    url = new URL(absURL);
                    source = new InputSource(url.openStream());
                    source.setSystemId(href);

				} catch (MalformedURLException ex2) {
                    // nothing to do
                }

            } catch (java.io.FileNotFoundException fnfe) {
                try {
                    String absURL = URIUtils.resolveAsString(href, baseUrl.toString());
                    url = new URL(absURL);
                    source = new InputSource(url.openStream());
                    source.setSystemId(href);

				} catch (MalformedURLException ex2) {
                    // nothing to do
                }
            }
        }

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document dom = null;

		/**
		 * Load and parse the file.
		 */
		if (source != null) {
			dom = db.parse(source);


			/**
			 * Get the policy information out of it!
			 */
			Element topLevelElement = dom.getDocumentElement();

			return topLevelElement;
		}

		return null;
	}


	/**
	 * Go through <directives> section of the policy file.
	 * @param directiveListNode Top level of <directives>
	 * @return A HashMap of directives for validation behavior.
	 */
	private void parseDirectives(Element root) {

		if (root == null) return;

		NodeList directiveNodes = root.getElementsByTagName("directive");

		for(int i=0;i<directiveNodes.getLength();i++) {

			Element ele = (Element)directiveNodes.item(i);

			String name = XMLUtil.getAttributeValue(ele,"name");
			String value = XMLUtil.getAttributeValue(ele,"value");

			directives.put(name,value);

		}
	}

	/**
	 * Go through <tags-to-encode> section of the policy file.
	 * @param root Top level of <tags-to-encode>
	 * @return A HashMap of String tags that are to be encoded when they're encountered.
	 * @throws ProfileException
	 */
	private void parseTagsToEncode(Element root) throws ProfileException {

		if (root == null) return;

		NodeList tagsToEncodeNodes = root.getElementsByTagName("tag");

		if ( tagsToEncodeNodes != null ) {

			for(int i=0;i<tagsToEncodeNodes.getLength();i++) {

				Element ele = (Element)tagsToEncodeNodes.item(i);
				if ( ele.getFirstChild() != null && ele.getFirstChild().getNodeType() == Node.TEXT_NODE ) {
					encodeTags.add(ele.getFirstChild().getNodeValue());
				}

			}
		}
	}

	/**
	 * Go through <global-tag-attributes> section of the policy file.
	 * @param globalAttributeListNode Top level of <global-tag-attributes>
	 * @return A HashMap of global Attributes that need validation for every tag.
	 * @throws ProfileException
	 */
	private void parseGlobalAttributes(Element root) throws ProfileException {

		if (root == null) return;

		NodeList globalAttributeNodes = root.getElementsByTagName("attribute");

		/*
		 * Loop through the list of regular expressions and add them to the collection.
		 */
		for(int i=0;i<globalAttributeNodes.getLength();i++) {
			Element ele = (Element)globalAttributeNodes.item(i);

			String name = XMLUtil.getAttributeValue(ele,"name");

			Attribute toAdd = getCommonAttributeByName(name);

			if ( toAdd != null ) {
				globalAttributes.put(name.toLowerCase(),toAdd);
			} else {
				throw new ProfileException("Global attribute '"+name+"' was not defined in <common-attributes>");
			}
		}
	}

	/**
	 * Go through the <common-regexps> section of the policy file.
	 * @param root Top level of <common-regexps>
	 * @return An ArrayList of AntiSamyPattern objects.
	 */
	private void parseCommonRegExps(Element root) {

		if (root == null) return;

		NodeList commonRegExpPatternNodes = root.getElementsByTagName("regexp");

		/*
		 * Loop through the list of regular expressions and add them to the collection.
		 */
		for(int i=0;i<commonRegExpPatternNodes.getLength();i++) {
			Element ele = (Element)commonRegExpPatternNodes.item(i);

			String name = XMLUtil.getAttributeValue(ele,"name");
			Pattern pattern = Pattern.compile(XMLUtil.getAttributeValue(ele,"value"));

			commonRegularExpressions.put(name,new RegexPattern(name,pattern));

		}
	}


	/**
	 * Go through the <common-attributes> section of the policy file.
	 * @param root Top level of <common-attributes>
	 * @return An ArrayList of Attribute objects.
	 */
	private void parseCommonAttributes(Element root) {

		if (root == null) return;

		NodeList commonAttributesNodes = root.getElementsByTagName("attribute");

		/*
		 * Loop through the list of attributes and add them to the collection.
		 */
		for(int i=0;i<commonAttributesNodes.getLength();i++) {

			Element ele = (Element)commonAttributesNodes.item(i);

			String onInvalid = XMLUtil.getAttributeValue(ele,"onInvalid");
			String name = XMLUtil.getAttributeValue(ele,"name");

			Attribute attribute = new Attribute(XMLUtil.getAttributeValue(ele,"name"));
			attribute.setDescription(XMLUtil.getAttributeValue(ele,"description"));

			if ( onInvalid != null && onInvalid.length() > 0 ) {
				attribute.setOnInvalid(onInvalid);
			} else {
				attribute.setOnInvalid(DEFAULT_ONINVALID);
			}

			Element regExpListNode = (Element)ele.getElementsByTagName("regexp-list").item(0);


			if ( regExpListNode != null ) {
				NodeList regExpList = regExpListNode.getElementsByTagName("regexp");

				/*
				 * First go through the allowed regular expressions.
				 */
				for(int j=0;j<regExpList.getLength();j++) {
					Element regExpNode = (Element)regExpList.item(j);

					String regExpName = XMLUtil.getAttributeValue(regExpNode,"name");
					String value = XMLUtil.getAttributeValue(regExpNode,"value");

					if (  regExpName != null && regExpName.length() > 0 ) {
						/*
						 * Get the common regular expression.
						 */
						attribute.addAllowedRegExp(getRegularExpression(regExpName).getPattern());
					} else {
						attribute.addAllowedRegExp(Pattern.compile(REGEXP_BEGIN+value+REGEXP_END)) ;
					}
				}
			}

			Element literalListNode = (Element)ele.getElementsByTagName("literal-list").item(0);

			if ( literalListNode != null ) {

				NodeList literalList = literalListNode.getElementsByTagName("literal");
				/*
				 * Then go through the allowed constants.
				 */
				for(int j=0;j<literalList.getLength();j++) {
					Element literalNode = (Element)literalList.item(j);

					String value = XMLUtil.getAttributeValue(literalNode,"value");

					if ( value != null && value.length() > 0 ) {
						attribute.addAllowedValue(value);
					} else if ( literalNode.getNodeValue() != null ) {
						attribute.addAllowedValue(literalNode.getNodeValue());
					}

				}

			}

			commonAttributes.put(name.toLowerCase(),attribute);

		}
	}


	/**
	 * Private method for parsing the <tag-rules> from the XML file.
	 * @param root The root element for <tag-rules>
	 * @return A List<Tag> containing the rules.
	 * @throws ProfileException
	 */
	private void parseTagRules(Element root) throws ProfileException {

		if (root == null) return;

		NodeList tagList = root.getElementsByTagName("tag");

		/*
		 * Go through tags.
		 */
		for(int i=0;i<tagList.getLength();i++) {

			Element tagNode = (Element)tagList.item(i);

			String name = XMLUtil.getAttributeValue(tagNode,"name");
			String action = XMLUtil.getAttributeValue(tagNode, "action");

			/*
			 * Create tag.
			 */

			Tag tag = new Tag(name);

			if ( tagNames == null ) {
				tagNames = new ArrayList<String>();
			}

			tagNames.add(name);

			tag.setAction(action);

			/*
			 * 添加父Tag规则
			 */

			NodeList parentTagNameList = tagNode.getElementsByTagName("parent-tag");

            if ( parentTagNameList != null ) {
                for(int j=0;j<parentTagNameList.getLength();j++) {
                    Element parentTagNode = (Element)parentTagNameList.item(j);
                    
                    String parentTagName = XMLUtil.getAttributeValue(parentTagNode,"value");
                    int distance = NumberUtils0.toInt(XMLUtil.getAttributeValue(parentTagNode,"distance"),-1);
                    if(parentTagName != null && parentTagName.length() > 0){
                        ParentTag parentTag = new ParentTag();
                        parentTag.setValue(parentTagName.toLowerCase());
                        parentTag.setDistance(distance);
                        tag.addParentTag(parentTag);
                    }
                }
            }
			
			/*
			 * Add its attribute rules.
			 */
            NodeList attributeList = tagNode.getElementsByTagName("attribute");
			for(int j=0;j<attributeList.getLength();j++) {

				Element attributeNode = (Element)attributeList.item(j);

				if ( ! attributeNode.hasChildNodes() ) {
					Attribute attribute = getCommonAttributeByName(XMLUtil.getAttributeValue(attributeNode,"name"));

					/*
					 * All they provided was the name, so they must want a common
					 * attribute.
					 */
					if ( attribute != null ) {

						/*
						 * If they provide onInvalid/description values here they will
						 * override the common values.
						 */

						String onInvalid = XMLUtil.getAttributeValue(attributeNode,"onInvalid");
						String description = XMLUtil.getAttributeValue(attributeNode,"description");

						if ( onInvalid != null && onInvalid.length() != 0 ) {
							attribute.setOnInvalid(onInvalid);
						}
						if ( description != null && description.length() != 0 ) {
							attribute.setDescription(description);
						}

						tag.addAttribute((Attribute) attribute.clone());

					} else {

						throw new ProfileException("Attribute '"+XMLUtil.getAttributeValue(attributeNode,"name")+"' was referenced as a common attribute in definition of '"+tag.getName()+"', but does not exist in <common-attributes>");

					}

				} else {
					/*
					 * Custom attribute for this tag.
					 */
					Attribute attribute = new Attribute(XMLUtil.getAttributeValue(attributeNode,"name"));
					attribute.setOnInvalid(XMLUtil.getAttributeValue(attributeNode,"onInvalid"));
					attribute.setDescription(XMLUtil.getAttributeValue(attributeNode,"description"));

					/*
					 * Get the list of regexps for the attribute.
					 */
					Element regExpListNode = (Element)attributeNode.getElementsByTagName("regexp-list").item(0);

					if ( regExpListNode != null ) {
						NodeList regExpList = regExpListNode.getElementsByTagName("regexp");

						for(int k=0;k<regExpList.getLength();k++) {

							Element regExpNode = (Element)regExpList.item(k);

							String regExpName = XMLUtil.getAttributeValue(regExpNode,"name");
							String value = XMLUtil.getAttributeValue(regExpNode,"value");

							/*
							 * Look up common regular expression specified
							 * by the "name" field. They can put a common
							 * name in the "name" field or provide a custom
							 * value in the "value" field. They must choose
							 * one or the other, not both.
							 */
							if ( regExpName != null && regExpName.length() > 0 ) {

								RegexPattern pattern = getRegularExpression(regExpName);

								if ( pattern != null ) {

									attribute.addAllowedRegExp(pattern.getPattern());
								} else {

									throw new ProfileException("Regular expression '"+regExpName+"' was referenced as a common regexp in definition of '"+tag.getName()+"', but does not exist in <common-regexp>");
								}

							} else if ( value != null && value.length() > 0 ) {
								attribute.addAllowedRegExp(Pattern.compile(REGEXP_BEGIN+value+REGEXP_END));
							}
						}
					}

					/*
					 * Get the list of constant values for the attribute.
					 */
					Element literalListNode = (Element)attributeNode.getElementsByTagName("literal-list").item(0);

					if ( literalListNode != null ) {
						NodeList literalList = literalListNode.getElementsByTagName("literal");

						for(int k=0;k<literalList.getLength();k++) {
							Element literalNode = (Element)literalList.item(k);
							String value = XMLUtil.getAttributeValue(literalNode,"value");

							/*
							 * Any constant value will do.
							 */

							if ( value != null && value.length() > 0 ) {
								attribute.addAllowedValue(value);
							} else if ( literalNode.getNodeValue() != null ) {
								attribute.addAllowedValue(literalNode.getNodeValue());
							}

						}
					}
					/*
					 * Add fully built attribute.
					 */
					tag.addAttribute(attribute);
				}

			}

			tagRules.put(name.toLowerCase(),tag);
		}
	}

	/**
	 * Go through the <css-rules> section of the policy file.
	 * @param root Top level of <css-rules>
	 * @return An ArrayList of Property objects.
	 * @throws ProfileException
	 */
	private void parseCSSRules(Element root) throws ProfileException {

		if (root == null) return;

		NodeList propertyNodes = root.getElementsByTagName("property");

		/*
		 * Loop through the list of attributes and add them to the collection.
		 */
		for(int i=0;i<propertyNodes.getLength();i++) {
			Element ele = (Element)propertyNodes.item(i);

			String name = XMLUtil.getAttributeValue(ele,"name");
			String description = XMLUtil.getAttributeValue(ele,"description");

			Property property = new Property(name);
			property.setDescription(description);

			String onInvalid = XMLUtil.getAttributeValue(ele,"onInvalid");

			if ( onInvalid != null && onInvalid.length() > 0 ) {
				property.setOnInvalid(onInvalid);
			} else {
				property.setOnInvalid(DEFAULT_ONINVALID);
			}

			Element regExpListNode = (Element)ele.getElementsByTagName("regexp-list").item(0);


			if ( regExpListNode != null ) {
				NodeList regExpList = regExpListNode.getElementsByTagName("regexp");

				/*
				 * First go through the allowed regular expressions.
				 */
				for(int j=0;j<regExpList.getLength();j++) {
					Element regExpNode = (Element)regExpList.item(j);

					String regExpName = XMLUtil.getAttributeValue(regExpNode,"name");
					String value = XMLUtil.getAttributeValue(regExpNode,"value");

					RegexPattern pattern = getRegularExpression(regExpName);

					if ( pattern != null ) {

						property.addAllowedRegExp(pattern.getPattern());
					} else if ( value != null ){
						property.addAllowedRegExp(Pattern.compile(REGEXP_BEGIN+value+REGEXP_END));

					} else {

						throw new ProfileException("Regular expression '"+regExpName+"' was referenced as a common regexp in definition of '"+property.getName()+"', but does not exist in <common-regexp>");
					}

				}
			}

			Element literalListNode = (Element)ele.getElementsByTagName("literal-list").item(0);

			if ( literalListNode != null ) {

				NodeList literalList = literalListNode.getElementsByTagName("literal");
				/*
				 * Then go through the allowed constants.
				 */
				for(int j=0;j<literalList.getLength();j++) {
					Element literalNode = (Element)literalList.item(j);
					property.addAllowedValue(XMLUtil.getAttributeValue(literalNode,"value")) ;
				}

			}

			Element shorthandListNode = (Element)ele.getElementsByTagName("shorthand-list").item(0);
			if ( shorthandListNode != null ) {

				NodeList shorthandList = shorthandListNode.getElementsByTagName("shorthand");
				/*
				 * Then go through the allowed constants.
				 */
				for(int j=0;j<shorthandList.getLength();j++) {
					Element shorthandNode = (Element)shorthandList.item(j);
					property.addShorthandRef(XMLUtil.getAttributeValue(shorthandNode,"name")) ;
				}

			}

			cssRules.put(name.toLowerCase(),property);

		}
	}



	/**
	 * A simple method for returning on of the <common-regexp> entries by
	 * name.
	 *
	 * @param name The name of the common regexp we want to look up.
	 * @return An AntiSamyPattern associated with the lookup name specified.
	 */
	public RegexPattern getRegularExpression(String name) {

		return (RegexPattern) commonRegularExpressions.get(name);

	}

	/**
	 * A simple method for returning on of the <global-attribute> entries by
	 * name.
	 * @param name The name of the global-attribute we want to look up.
	 * @return An Attribute associated with the global-attribute lookup name specified.
	 */
	public Attribute getGlobalAttributeByName(String name) {

		return (Attribute) globalAttributes.get(name.toLowerCase());

	}

	/**
	 * A simple method for returning on of the <common-attribute> entries by
	 * name.
	 * @param name The name of the common-attribute we want to look up.
	 * @return An Attribute associated with the common-attribute lookup name specified.
	 */
	private Attribute getCommonAttributeByName(String attributeName) {

		return (Attribute) commonAttributes.get(attributeName.toLowerCase());

	}


	/**
	 * Return all the tags accepted by the Policy object.
	 * @return A String array of all the tag names accepted by the current Policy.
	 */
	public String[] getTags() {
		return (String[])tagNames.toArray(new String[1]);
	}

	/**
	 * Return a directive value based on a lookup name.
	 * @return A String object containing the directive associated with the lookup name, or null if none is found.
	 */
	public String getDirective(String name) {
		return (String) directives.get(name);
	}

	/**
	 * Set a directive for a value based on a name.
	 * @param name A directive to set a value for.
	 * @param value The new value for the directive.
	 */
	public void setDirective(String name, String value) {
		directives.put(name, value);
	}

	/**
	 * Returns the maximum input size. If this value is not specified by
	 * the policy, the <code>DEFAULT_MAX_INPUT_SIZE</code> is used.
	 * @return the maximium input size.
	 */
	public int getMaxInputSize() {
		int maxInputSize = HtmlCorrectRule.DEFAULT_MAX_INPUT_SIZE;

		try {
			maxInputSize = Integer.parseInt(getDirective("maxInputSize"));
		} catch (NumberFormatException nfe) {}

		return maxInputSize;
	}

	/**
	 * Set the base directory to use to resolve relative file paths when including other policy files.
	 *
	 * @param newValue
	 */
	public static void setBaseURL(URL newValue) {
		baseUrl = newValue;
	}

	/**
	 * Resolves public & system ids to files stored within the JAR.
	 */
	public InputSource resolveEntity(final String publicId,
			final String systemId) throws IOException, SAXException {
		InputSource source = null;

		// Can't resolve public id, but might be able to resolve relative
		// system id, since we have a base URI.
		if (systemId != null && baseUrl != null) {
			URL url;

			try {
				url = new URL(baseUrl, systemId);
				source = new InputSource(url.openStream());
				source.setSystemId(systemId);
				return source;
			} catch (MalformedURLException except) {
				try {
					String absURL = URIUtils.resolveAsString(systemId, baseUrl.toString());
					url = new URL(absURL);
					source = new InputSource(url.openStream());
					source.setSystemId(systemId);
					return source;
				} catch (MalformedURLException ex2) {
					// nothing to do
				}
			} catch (java.io.FileNotFoundException fnfe) {
				try {
					String absURL = URIUtils.resolveAsString(systemId, baseUrl.toString());
					url = new URL(absURL);
					source = new InputSource(url.openStream());
					source.setSystemId(systemId);
					return source;
				} catch (MalformedURLException ex2) {
					// nothing to do
				}
			}
			return null;
		}

		// No resolving.
		return null;
	}
}
