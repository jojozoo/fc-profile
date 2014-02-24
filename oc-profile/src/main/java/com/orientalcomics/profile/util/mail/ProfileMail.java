package com.orientalcomics.profile.util.mail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.orientalcomics.profile.biz.model.User;
import com.orientalcomics.profile.util.logging.ILogger;
import com.orientalcomics.profile.util.logging.ProfileLogger;

/**
 * 
 * 项目名称：renren-profile 类名称：JavaMail 类描述： 创建人：Administrator 创建时间：2012-3-21
 * 上午11:08:29
 * 
 * @version
 */
public class ProfileMail {

    private ILogger logger        = ProfileLogger.getLogger(this.getClass());
    private String  emailHostName = "smtp.renren-inc.com";

    public ProfileMail(String emailHostName) {
        this.emailHostName = emailHostName;
    }

    /***
     * 发送邮件的方式是一对多
     * 
     * @param from
     *            要发送的邮件地址
     * @param to
     *            [] 收到邮件的地址
     * @param emailTitle
     *            发送邮件的主题
     * @param eamilContent
     *            发送邮件的内容
     * 
     * @return
     * @throws EmailException
     */
    public void sendEmail(EmailUser from, Collection<EmailUser> tos, String emailTitle, String emailContent) throws EmailException {

        if (CollectionUtils.isEmpty(tos)) {
            throw new NullArgumentException("tos");
        }
        if (from == null) {
            throw new NullArgumentException("from");
        }

        // email 设置主机以及字符编码
        HtmlEmail email = new HtmlEmail();
        email.setHostName(emailHostName);
        email.setCharset("GB2312");

        try {

            for (EmailUser to : tos) {
                if (StringUtils.isNotEmpty(to.getName())) {
                    email.addTo(to.getEmail(), to.getName());
                } else {
                    email.addTo(to.getEmail());
                }
            }
            if (StringUtils.isNotEmpty(from.getName())) {
                email.setFrom(from.getEmail(), from.getName());
            } else {
                email.setFrom(from.getEmail());
            }
            email.setHtmlMsg(emailContent);
            email.setSubject(emailTitle);
            email.send();
        } catch (EmailException e) {
            logger.error("发送邮件失败！", e);
            throw new EmailException("ProfileMail|sendMail|from|" + from + "|tos|" + StringUtils.join(tos, ",") + "|" + emailTitle + "|" + emailContent, e);
        }
    }

    /**
     * 用户user转换成EmailUser
     * 
     * @param user
     * @return
     */
    public static EmailUser convertEmailUser(User user) {

        if (user == null)
            return null;

        return new EmailUser(user.getName(), user.getEmail());
    }

    /**
     * 多个User转成EmailUser
     * 
     * @param users
     * @return
     */
    public static Collection<EmailUser> convertEmailUserList(List<User> users) {

        return Collections2.transform(users, new Function<User, EmailUser>() {

            @Override
            public EmailUser apply(User user) {
                return convertEmailUser(user);
            }

        });

    }

    /***
     * 发送邮件是一对一的发送
     * 
     * @param from
     *            发送邮件的服务器地址
     * @param to
     *            要发送给对方的邮件地址
     * @param emailTitle
     *            邮件的主题
     * @param emailContent
     *            邮件的内容
     * @return
     */
    public void sendSingleEmail(EmailUser from, EmailUser to, String emailTitle, String emailContent) throws EmailException {
        sendEmail(from, Arrays.asList(to), emailTitle, emailContent);
    }

    public static void main(String args[]) throws EmailException {

        ProfileMail mail = new ProfileMail("smtp.renren-inc.com");
        List<EmailUser> toMails = new ArrayList<EmailUser>();
        toMails.add(new EmailUser("何文", "wen.he1@renren-inc.com"));

        mail.sendEmail(new EmailUser("何文1", "wen.he1@renren-inc.com"), toMails, "测试邮件", "http://www.126.com");
    }
}
