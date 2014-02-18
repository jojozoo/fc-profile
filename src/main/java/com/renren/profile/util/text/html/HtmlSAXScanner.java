// PROJECT : http://code.google.com/p/owaspantisamy
package com.renren.profile.util.text.html;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.xerces.xni.parser.XMLDocumentFilter;
import org.cyberneko.html.parsers.SAXParser;
import org.xml.sax.InputSource;

import com.renren.profile.util.sys.ProfileException;

class HtmlSAXScanner extends AbstractHtmlScanner {

    public HtmlSAXScanner(HtmlCorrectRule policy) {
        super(policy);
    }

    public CleanResults getResults() {
        return null;
    }

    public CleanResults scan(String html) throws ProfileException {

        if (html == null) {
            throw new ProfileException(new NullPointerException("Null input"));
        }

        int maxInputSize = policy.getMaxInputSize();

        if (maxInputSize < html.length()) {
            throw new ProfileException("输入文件过大,最多输入" + maxInputSize);
        }

        MagicSAXFilter filter = new MagicSAXFilter(policy, messages);
        XMLDocumentFilter[] filters = { filter };

        try {
            SAXParser parser = new SAXParser();
            parser.setFeature("http://xml.org/sax/features/namespaces", false);
            parser.setFeature("http://cyberneko.org/html/features/balance-tags", true);
            parser.setFeature("http://cyberneko.org/html/features/balance-tags/ignore-outside-content", false);
            parser.setFeature("http://cyberneko.org/html/features/balance-tags/document-fragment", true);
            parser.setFeature("http://cyberneko.org/html/features/scanner/cdata-sections", true);

            parser.setProperty("http://cyberneko.org/html/properties/filters", filters);
            parser.setProperty("http://cyberneko.org/html/properties/names/elems", "lower");
            parser.setProperty("http://cyberneko.org/html/properties/default-encoding", Constants.DEFAULT_ENCODING_ALGORITHM);

            Date start = new Date();

            SAXSource source = new SAXSource(parser, new InputSource(new StringReader(html)));
            StringWriter out = new StringWriter();
            StreamResult result = new StreamResult(out);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            Transformer transformer = transformerFactory.newTransformer();

            if ("true".equals(policy.getDirective("omitXmlDeclaration"))) {
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            } else {
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            }

            if ("true".equals(policy.getDirective("formatOutput"))) {
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            } else {
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "0");
                transformer.setOutputProperty(OutputKeys.INDENT, "no");
            }

            if ("true".equals(policy.getDirective("useXHTML"))) {
                transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            } else {
                transformer.setOutputProperty(OutputKeys.METHOD, "html");
            }

            if ("true".equals(policy.getDirective("omitDoctypeDeclaration"))) {
                parser.setFeature("http://cyberneko.org/html/features/balance-tags/document-fragment", true);
            } else if ("true".equals(policy.getDirective("useXHTML"))) {
                transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "-//W3C//DTD XHTML 1.0 Transitional//EN");
                transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");
            } else {
                transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "-//W3C//DTD HTML 4.01 Transitional//EN");
                transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "http://www.w3.org/TR/html4/loose.dtd");
            }

            transformer.setOutputProperty("{http://xml.apache.org/xslt}entities", "org/apache/xml/serializer/HTMLEntities");
            transformer.setOutputProperty(OutputKeys.ENCODING, Constants.DEFAULT_ENCODING_ALGORITHM);

            transformer.transform(source, result);

            Date end = new Date();

            errorMessages = filter.getErrorMessages();
            return new CleanResults(start, end, out.getBuffer().toString(), null, errorMessages);

        } catch (Exception e) {
            throw new ProfileException(e);
        }

    }

}
