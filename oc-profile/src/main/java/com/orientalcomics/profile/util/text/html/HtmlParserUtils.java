package com.orientalcomics.profile.util.text.html;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.SimpleNodeIterator;

/**
 * html解析器，该解析器只解析html文本中的标签节点(TagNode)，对内容节点(TextNode)忽略。<br/>
 * 在解析过程中可以自定义忽略的节点属性：例如如果忽略href属性，则在解析<a href="www.renren.com">的时候该内容不会被解析。<br/>
 * 解析的最终结果被封装成MyTagAttr对象存放在一个List的集合中。（允许存在相同元素）
 * 
 * @author chao.qi@renren-inc.com
 */
public final class HtmlParserUtils {
	/** 解析器 */
	private Parser parser;
	/**忽略的属性名称*/
	private String[] ignoreAttrNames;

	public HtmlParserUtils() throws ParserException {
		this("utf-8", null);
	}
	
	public HtmlParserUtils(String[] ignoreAttrNames) throws ParserException {
		this("utf-8", ignoreAttrNames);
	}

	public HtmlParserUtils(final String charset,String[] ignoreAttrNames) throws ParserException {
		parser = new Parser();
		parser.setEncoding(charset);
		this.ignoreAttrNames = ignoreAttrNames;
	}

	/**
	 * 解析html内容
	 * 2012-10-18 上午11:49:34
	 * @autor chao.qi@renren-inc.com
	 * @param htmlContent
	 * 			解析内容<br/>
	 * @return
	 * @throws ParserException
	 */
	@SuppressWarnings("rawtypes")
	public List<MyTagAttr> parse(final String htmlContent)
			throws ParserException {
		List<MyTagAttr> attrs = null;
		if (StringUtils.isBlank(htmlContent)) {
			return attrs;
		}
		if (null == attrs) {
			attrs = new ArrayList<MyTagAttr>();
		}
		
		parser.setInputHTML(htmlContent);// 设置解析内容
		NodeList nodeList = parser.extractAllNodesThatMatch(new MyNodeFilter());
		
		// 遍历解析结果,得到节点的属性值
		SimpleNodeIterator it = nodeList.elements();
		Node _node = null;
		Tag _tag = null;
		List preTagAttrs = null;
		MyTagAttr tmpAttr = null;
		
		while (it.hasMoreNodes()) {
			_node = it.nextNode();// 得到当前遍历的节点
			if (_node instanceof TagNode) {
				// 将节点转化为标签类型，得到该标签的所有属性
				_tag = (Tag) _node;
				preTagAttrs = _tag.getAttributesEx();

				// 遍历每个属性key="value"，将属性转化为MyTagAttr对象
				for (int i = 0; i < preTagAttrs.size(); i++) {
					tmpAttr = parserString(preTagAttrs.get(i).toString());
					if (null != tmpAttr) {
						attrs.add(tmpAttr);
					}
				}
			}
		}
		return attrs;
	}

	/**
	 * 解析单个元素属性的字符串，该字符串的类型应该是：key="value"<br/>
	 * 2012-10-18 上午11:50:41
	 * @autor chao.qi@renren-inc.com
	 * @param str
	 * 			节点属性的字符串<br/>
	 * @return
	 * 			MyTagAttr : 属性对象，对于key="value"的字符串，则返回new MyTagAttr("key","value")
	 */
	private MyTagAttr parserString(final String str) {
		if (StringUtils.isBlank(str) || !str.contains("=")) {
			return null;
		}
		
		String[] key_value = str.split("=");
		if (null != key_value && key_value.length == 2) {
			//判断标签是否被忽略
			if(isTagIgnore(key_value[0])){
				return null;
			}
			//去除属性值中的"和'
			String value = key_value[1].replace("\"", "").replace("\'", "");
			return new MyTagAttr(key_value[0], value);
		}
		return null;
	}
	
	/**
	 * 判断属性是否被忽略，如过忽略则在解析的过程中会跳过该属性<br/>
	 * 2012-10-18 上午11:51:02
	 * @autor chao.qi@renren-inc.com
	 * @param attrName
	 * 			属性名称<br/>
	 * @return 
	 * 			true：忽略，不解析； <br/>
	 * 			false：不忽略，需要解析
	 */
	private boolean isTagIgnore(final String attrName){
		//如果标签为空，忽略该标签
		if(StringUtils.isBlank(attrName)){
			return true;
		}
		
		if (null == ignoreAttrNames || ignoreAttrNames.length <= 0) {
			return false;
		}
		
		//比较标签名称和忽略的标签是否匹配，如果匹配则忽略该标签
		for(String _name : ignoreAttrNames){
			if(attrName.toString().toLowerCase().equals(_name.toLowerCase())){
				return true;
			}
		}
		return false;
	}

	/**
	 * 定义解析节点过滤器，在此过滤器中只解析html中的标签节点，对结束标签会忽略<br/>
	 * @author chao.qi@renren-inc.com
	 */
	private class MyNodeFilter implements NodeFilter {
		private static final long serialVersionUID = 1L;

		@Override
		public boolean accept(Node node) {
			// 只有标签类的节点才会被解析,结束标签会被忽略
			if (node instanceof TagNode) {
				Tag _tag = (Tag) node;
				if (_tag.isEndTag()) {
					return false;
				}
				return true;
			}
			return false;
		}
	}

	/**
	 * 定义标签的属性对象，每个属性中包括一个key~value的键值对<br/>
	 * eg : 
	 *		<a href="www.renren.com">对应的元素属性对象为new MyTagAttr("href","www.renren.com")<br/>
	 * @author chao.qi@renren-inc.com
	 */
	public class MyTagAttr {
		/**属性名称*/
		private String key;
		/**属性值*/
		private String value;
		
		public MyTagAttr(){}
		
		public MyTagAttr(String key, String value) {
			this.key = key;
			this.value = value;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}
}
