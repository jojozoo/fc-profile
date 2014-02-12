package com.orientalcomics.profile.util.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.orientalcomics.profile.biz.model.BusinessTag;


public class Strings0 {
	
	public static String convertStringByComma(List<BusinessTag> businessTags) {

		StringBuilder businessTageNames = new StringBuilder();
		int i = 0;
		for (BusinessTag businessTag : businessTags) {

			if (i < (businessTags.size() - 1)) {
				businessTageNames.append(businessTag.getTagName()).append(",");
				i++;
			} else
				businessTageNames.append(businessTag.getTagName());

		}

		return businessTageNames.toString();
	}
	
	/**
	 * 截取字符串(或（的结束的字符串如：c++工程师（人人网/技术部/引擎部/新鲜事），截取完后c++工程师
	 * 
	 * @param str
	 * @return
	 */
	public static String subString(String str){
		
		if(StringUtils.isEmpty(str))
			return "";
		
		if(StringUtils.indexOf(str, "(") != -1){
			return StringUtils.substring(str, 0,StringUtils.indexOf(str, "("));
		}else if(StringUtils.indexOf(str, "（") != -1){
			return StringUtils.substring(str, 0,StringUtils.indexOf(str, "（"));
		}else{
			return str;
		}
	}
	
	   /**
     * 两个字符串str1和str2进行比较；注意两个字符串的格式是:"aa,bb,cc"<br>
     * 也就是把两个逗号隔开的字符串进行比较。<br>
     * <p>
     * </P>
     * 实例：<br>
     * &nbsp&nbsp str1="aa,bb,cc,dd";<br>
     * &nbsp&nbsp str2="bb,dd,ac";<br>
     * &nbsp&nbsp 那么返回的结果是：{"aa","cc"}
     * 
     * @param str1
     * @param str2
     * @return
     */
    public static List<String> compareStringByCommaDifference(String str1, String str2) {

        String[] strs1 = StringUtils.split(str1, ",");
        String[] strs2 = StringUtils.split(str2, ",");

        List<String> strList1 = new ArrayList<String>(strs1.length);
        List<String> strList2 = new ArrayList<String>(strs2.length);
        for (String str : strs1)
            strList1.add(str);
        for (String str : strs2)
            strList2.add(str);

        List<String> diffList = new ArrayList<String>();
        for (String str : strList1) {
            if (!strList2.contains(str))
                diffList.add(str);
        }

        return diffList;
    }
}
