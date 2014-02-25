package com.orientalcomics.profile.util.mail;

import java.util.ArrayList;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.orientalcomics.profile.OcProfileConstants;

public class EmailUtils implements OcProfileConstants {
    public static String[] correctEmails(String[] inputs, boolean silent) {
        return correctEmails(inputs, null, silent);
    }

    public static String[] correctEmails(String[] inputs, String emailDomain, boolean silent) {
        if (ArrayUtils.isEmpty(inputs)) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        ArrayList<String> result = new ArrayList<String>(inputs.length);
        for (String input : inputs) {
            String res = correctEmail(input, emailDomain, silent);
            if (res != null) {
                result.add(res);
            }
        }
        return result.toArray(new String[result.size()]);
    }

    public static String correctEmail(String input, boolean silent) {
        return correctEmail(input, null, silent);
    }

    public static String correctEmail(String input, String emailDomain, boolean silent) {
        if (StringUtils.isEmpty(input)) {
            return null;
        }
        String[] cols = StringUtils.split(input, "@", 2);
        String main, domain;
        main = cols[0];
        domain = cols.length > 1 ? cols[1] : null;
        if (domain != null) {
            if (emailDomain != null) {// 要求验证domain
                if (!StringUtils.equalsIgnoreCase(domain, emailDomain)) {
                    if (!silent) {
                        throw new IllegalArgumentException("email's domain must be '" + emailDomain + "'|" + input);
                    } else {
                        return null;
                    }
                }
            }
        } else {
            domain = emailDomain == null ? EMAIL_DOMAIN : emailDomain;
        }
        if (!main.matches(EMAIL_MAIN_PATTERN)) {
            if (!silent) {
                throw new IllegalArgumentException("email's main must be regex'" + EMAIL_MAIN_PATTERN + "'|" + input);
            } else {
                return null;
            }
        }
        return main + "@" + domain;
    }
    public static void main(String[] args) {
		String emailto = EmailUtils.correctEmail("zhanghao@foundercomics.com","foundercomics.com",true);
		System.out.println(""+emailto);
		
	}
}
