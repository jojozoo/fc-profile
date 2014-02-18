package com.renren.profile.util;

import java.util.List;

import com.renren.profile.biz.model.BusinessTag;

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
}
