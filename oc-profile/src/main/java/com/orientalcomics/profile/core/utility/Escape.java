package com.orientalcomics.profile.core.utility; 
/** 
 * @author 张浩 E-mail:zhanghao@foundercomics.com 
 * @version 创建时间：2014年2月14日 上午11:20:49 
 * 类说明 string escape
 */
public class Escape {
	public static String banned =".*(5q|baodoo|dipian|5jia1|yeejee|faceben|chinay|faceren).*";
	  public static String filter = ".*faceben.*";

	  static final String[] script_editor = { "onabort", "onblur", "onchange", "onclick", "ondblclick", "onerror", "onfocus", "onkeydown", "onkeypress", "onkeyup", "onload", "onmousedown", "onmousemove", "onmouseout", "onmouseover", "onmouseup", "onreset", "onresize", "onselect", "onsubmit", "onunload", "<script", "<frame", "<applet", "<object", "<form", "<meta", "location" };

	  static final String[] script_space = { "onabort", "onblur", "onchange", "onclick", "ondblclick", "onerror", "onfocus", "onkeydown", "onkeypress", "onkeyup", "onload", "onmousedown", "onmousemove", "onmouseout", "onmouseover", "onmouseup", "onreset", "onresize", "onselect", "onsubmit", "onunload", "script", "frame", "applet", "form", "meta", "location" };

	  public static String stringToHTMLString(String string)
	  {
	    if (string == null) {
	      return null;
	    }
	    StringBuffer sb = new StringBuffer(string.length());

	    int len = string.length();

	    for (int i = 0; i < len; i++) {
	      char c = string.charAt(i);

	      switch (c) { case '<':
	        sb.append("&lt;"); break;
	      case '>':
	        sb.append("&gt;"); break;
	      case '&':
	        sb.append("&amp;"); break;
	      case '#':
	        sb.append("&#35;"); break;
	      case '(':
	        sb.append("&#40;"); break;
	      case ')':
	        sb.append("&#41;"); break;
	      case '"':
	        sb.append("&#34;"); break;
	      case '\'':
	        sb.append("&#39;"); break;
	      case '$':
	      case '%':
	      case '*':
	      case '+':
	      case ',':
	      case '-':
	      case '.':
	      case '/':
	      case '0':
	      case '1':
	      case '2':
	      case '3':
	      case '4':
	      case '5':
	      case '6':
	      case '7':
	      case '8':
	      case '9':
	      case ':':
	      case ';':
	      case '=':
	      default:
	        sb.append(c);
	      }

	    }

	    return sb.toString();
	  }

	  public static String decodeHTML(String InputString)
	  {
	    String OutputString = null;
	    if (InputString == null)
	    {
	      return null;
	    }

	    OutputString = new String(InputString);
	    OutputString = OutputString.replace("&amp;", "&");
	    OutputString = OutputString.replace("&lt;", "<");
	    OutputString = OutputString.replace("&gt;", ">");
	    OutputString = OutputString.replace("&quot;", "\"");
	    OutputString = OutputString.replace("&nbsp;", " ");
	    return OutputString;
	  }

	  public static boolean isValidLink(String string)
	  {
	    if (string == null) {
	      return false;
	    }

	    for (int i = 0; i < string.length(); i++) {
	      char c = string.charAt(i);
	      if ((!Character.isDigit(c)) && (!Character.isLetter(c)))
	      {
	        return false;
	      }
	    }
	    return true;
	  }

	  public static boolean isLetterExist(String string) {
	    if (string == null) {
	      return false;
	    }

	    for (int i = 0; i < string.length(); i++) {
	      char c = string.charAt(i);
	      if (Character.isLetter(c)) {
	        return true;
	      }
	    }
	    return false;
	  }

	  public static String checkEditorHtml(String str)
	  {
	    if (str == null) {
	      return null;
	    }

	    String st = str.toLowerCase();
	    for (int i = 0; i < script_editor.length; i++) {
	      if (st.indexOf(script_editor[i]) > -1) {
	        return script_editor[i];
	      }
	    }
	    return null;
	  }

	  public static String checkSpaceHtml(String str)
	  {
	    if (str == null) {
	      return null;
	    }

	    String st = str.toLowerCase();
	    for (int i = 0; i < script_space.length; i++) {
	      if (st.indexOf(script_space[i]) > -1) {
	        return script_space[i];
	      }
	    }
	    return null;
	  }

	  public static String trim(String str) {
	    if (str == null) {
	      return null;
	    }

	    String st = str.trim();
	    while ((st.length() > 0) && (st.charAt(0) == '　')) {
	      st = st.substring(1);
	    }
	    while ((st.length() > 0) && (st.charAt(st.length() - 1) == '　')) {
	      st = st.substring(0, st.length() - 1);
	    }
	    return st;
	  }

	  public static String purgeWhiteSpace(String st) {
	    if (st == null) {
	      return null;
	    }
	    StringBuffer sb = new StringBuffer(st.length());
	    int len = st.length();

	    for (int i = 0; i < len; i++) {
	      char c = st.charAt(i);
	      if (!Character.isWhitespace(c)) {
	        sb.append(c);
	      }
	    }
	    return sb.toString();
	  }
}
 