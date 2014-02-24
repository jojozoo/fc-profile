package com.orientalcomics.profile.util;

import java.util.Map;

import net.paoding.rose.util.PlaceHolderUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanWrapperImpl;

/**
 * 
 * 项目名称：renren-profile  
 * 类名称：PlaceHolder  
 * 类描述： 用于识别识别${xxx}串或识别${xxx?}串，并进行替换的工具类  
 * 创建人：wen.he1  
 * 创建时间：2012-3-29 下午05:45:08  
 * 
 * @version
 */
public class PlaceHolder {


    // 以下这些final都是约定好的，不可随意改变。写成final只是为了不到处写字符串而已。
    
    public static final String DOLLAR = "$";
    
    public static final String PLACEHOLDER_PREFIX = "${";

    public static final char PLACEHOLDER_INNER_PREFIX = '{';

    public static final String PLACEHOLDER_INNER_PREFIX_STRING = "" + PLACEHOLDER_INNER_PREFIX;

    public static final char PLACEHOLDER_SUFFIX_CHAR = '}';

    public static final String PLACEHOLDER_SUFFIX = "" + PLACEHOLDER_SUFFIX_CHAR;

    private static final Log logger = LogFactory.getLog(PlaceHolderUtils.class);

    public static  String resolve(String text, Map<String, Object> map) {
    	
        if (StringUtils.isEmpty(text)) {
            return text;
        }
        
        int startIndex = text.indexOf(PLACEHOLDER_PREFIX);
        if (startIndex == -1) {
            return text;
        }
        
        StringBuilder buf = new StringBuilder(text);
        while (startIndex != -1) {
        	
            int endIndex = buf.indexOf(PLACEHOLDER_SUFFIX, startIndex + PLACEHOLDER_PREFIX.length());
            if (endIndex != -1) {
            	
                String placeholder = null;
                String defaultValue = null;
                for (int i = startIndex + PLACEHOLDER_PREFIX.length(); i < endIndex; i++) {
                	
                    if (buf.charAt(i) == '?') {
                        placeholder = buf.substring(startIndex + PLACEHOLDER_PREFIX.length(), i);
                        defaultValue = buf.substring(i + 1, endIndex);
                        break;
                    }
                    
                }
                
                if (placeholder == null) 
                    placeholder = buf.substring(startIndex + PLACEHOLDER_PREFIX.length(), endIndex);
                    
                int nextIndex = endIndex + PLACEHOLDER_SUFFIX.length();
                try {
                	
                    int dot = placeholder.indexOf('.');
                    String attributeName = dot == -1 ? placeholder : placeholder.substring(0, dot);
                    String propertyPath = dot == -1 ? "" : placeholder.substring(dot + 1);
                    Object propVal = map.get(attributeName);
                    if (propVal != null) {
                    	
                        if (propertyPath.length() > 0) 
                            propVal = new BeanWrapperImpl(propVal).getPropertyValue(propertyPath);
                        
                    } else {
                    	
                        if ("flash".equals(attributeName)) 
                        	 propVal = map.get(propertyPath);
                         else 
                             propVal = map.get(placeholder);
                        
                    }
                    //
                    if (propVal == null) 
                        propVal = defaultValue;
                    
                    if (propVal == null) {
                    	
                        if (logger.isWarnEnabled()) 
                            logger.warn("Could not resolve placeholder '" + placeholder + "' in ["
                                    + text + "].");
                        
                    } else {
                    	
                        String toString = propVal.toString();
                        buf.replace(startIndex, endIndex + PLACEHOLDER_SUFFIX.length(), toString);
                        nextIndex = startIndex + toString.length();
                        
                    }
                    
                } catch (Throwable ex) {
                	
                    logger.warn("Could not resolve placeholder '" + placeholder + "' in [" + text
                            + "] : " + ex);
                    
                }
                
                startIndex = buf.indexOf(PLACEHOLDER_PREFIX, nextIndex);
                
            } else {
            	
                startIndex = -1;
                
            }
        }

        return buf.toString();
    }


}
