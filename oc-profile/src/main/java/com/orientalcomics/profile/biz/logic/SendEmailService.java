package com.orientalcomics.profile.biz.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orientalcomics.profile.OcProfileConstants;
import com.orientalcomics.profile.biz.dao.EmailTextDAO;
import com.orientalcomics.profile.biz.model.Department;
import com.orientalcomics.profile.biz.model.EmailText;
import com.orientalcomics.profile.biz.model.PerfTime;
import com.orientalcomics.profile.biz.model.RewardItem;
import com.orientalcomics.profile.biz.model.User;
import com.orientalcomics.profile.biz.model.WeeklyReport;
import com.orientalcomics.profile.constants.email.type.PlaceHolderType;
import com.orientalcomics.profile.constants.email.type.SendEmailType;
import com.orientalcomics.profile.util.PlaceHolder;
import com.orientalcomics.profile.util.common.Collections0;
import com.orientalcomics.profile.util.mail.EmailUser;
import com.orientalcomics.profile.util.mail.ProfileMail;
import com.orientalcomics.profile.util.time.DateTimeUtil;
import com.orientalcomics.profile.web.dto.UserExtendInfoDTO;

/**
 * 
 * 项目名称：renren-profile 类名称：SendEmailService 类描述： 发送邮件的服务
 * 创建人：wen.he1@renren-inc.com 创建时间：2012-3-26 下午05:02:48
 * 
 * @version
 */
@Service
public class SendEmailService implements OcProfileConstants {

    @Autowired
    private EmailTextDAO      emailTextDAO;

    @Autowired
    private NotifyService     notifyService;

    @Autowired
    private PerfTimeService   perfService;

    @Autowired
    private UserService       userService;

    @Autowired
    private DepartmentService departmentService;
    
    @Autowired
    private StatisticsService statisticsService;

    private Logger LOG = LoggerFactory.getLogger(SendEmailService.class);
    
    
    public void sendNoSummbitedWeeklyUser() {
    	
		List<User> userList = statisticsService.getNoSummbitedWeeklyUserList();
		if (Collections0.isEmpty(userList)) {
			LOG.info("上周没有用户提交周报！");
			return;
		}
		
		List<UserExtendInfoDTO> extUserList = userService.getUserInfoByUsers(userList);
		
		List<User> users = new ArrayList<User>();
		User user1 = new User();
		user1.setName("周欣");
		user1.setEmail("xin.zhou1@renren-inc.com");
		
		User user2 = new User();
		user2.setName("李伟");
		user2.setEmail("kevin.li@renren-inc.com");
		
//		User user3 = new User();
//		user3.setName("sns");
//		user3.setEmail("renren.sns@renren-inc.com");
		
		users.add(user1);
		users.add(user2);
//		users.add(user3);
		
		StringBuilder emailContent = new StringBuilder();
		String tdStr1 = "<td width=230 nowrap valign=top style='width:105.0pt;border-top:solid #4BACC6 1.0pt;border-left:none;border-bottom:solid #4BACC6 1.0pt;border-right:none;padding:0cm 5.4pt 0cm 5.4pt;height:12.75pt'><p class=MsoNormal align=left style='text-align:left'><b><span style='font-size:10.0pt;font-family:宋体;color:#31849B'>";
		String tdStr2 = "</span></b><span lang=EN-US><o:p></o:p></span></p></td>";
		emailContent.append("<body lang=ZH-CN link=blue vlink=purple style='text-justify-trim:punctuation'>")
			.append("<div class=WordSection1>")
			.append("<table class=MsoNormalTable border=0 cellspacing=0 cellpadding=0 width=700 style='width:525.0pt;border-collapse:collapse'>")
			.append(" <tr style='height:12.75pt'>")
			.append(tdStr1).append("员工编号").append(tdStr2)
			.append(tdStr1).append("部门").append(tdStr2)
			.append(tdStr1).append("姓名").append(tdStr2).append(" </tr>");
		
		String contentTd1 = " <td width=230 nowrap valign=bottom style='width:105.0pt;background:#D2EAF1;padding:0cm 5.4pt 0cm 5.4pt;height:12.75pt'><p class=MsoNormal><span lang=EN-US style='font-size:10.0pt;font-family:"+"Arial"+",'sans-serif'"+"'>";
		String contentTd2 = "</span><span lang=EN-US style='font-size:10.0pt;font-family:"+"Arial"+",'sans-serif'"+"'><o:p></o:p></span></p></td>";
		for (UserExtendInfoDTO extUser : extUserList) {
			emailContent.append(" <tr style='height:12.75pt'>");
			emailContent.append(contentTd1+extUser.getUser().getNumber()+contentTd2);
			emailContent.append(contentTd1+extUser.getDeparmentName()+contentTd2);
			emailContent.append(contentTd1+extUser.getUser().getName()+contentTd2).append("</tr>");
		}
		
		emailContent.append("</table>");
		
		emailContent.append("<p class=MsoNormal><span lang=EN-US style='color:black'><o:p>&nbsp;</o:p></span></p><p class=MsoNormal><span style='font-family:宋体;color:red'>注意：上面的名单部分人已经离职，请离职人员的经理发邮件告知我，谢谢！</span><span lang=EN-US style='color:black'><o:p></o:p></span></p>");

		LOG.info("开始发送没有提交周报的名单了！");
        sendServerEmail(users, "who系统未提交周报通知", emailContent.toString());
		LOG.info("发送没有提交周报的名单已经结束！");

    }
    
    /**
     * 
     * 绩效考评开始
     * 
     */
    public void sendPerfStartEmail() {
        EmailText emailText = emailTextDAO.query(String.valueOf(SendEmailType.PERF_START.getId()));
        if (emailText == null) {

            LOG.info("查询emailText表根据email类型" + SendEmailType.PERF_START.getName() + "失败！");
            return;

        }

        LOG.info("发邮件的绩效考核开始啦！");

        PerfTime perfTime = perfService.getCurrent();
        String emailTitle = StringUtils.trimToEmpty(emailText.getEmailTitle());
        String emailContent = StringUtils.trimToEmpty(emailText.getEmailContent());

        Map<String, Object> placeMap = new HashMap<String, Object>(2);
        placeMap.put(PlaceHolderType.XX_PLACE.getName(), perfTime.getPerfTitle());
        emailTitle = PlaceHolder.resolve(emailTitle, placeMap);
        placeMap.put(PlaceHolderType.YY_PLACE.getName(), DateTimeUtil.getSimpleDateFormat(perfTime.dueDate()));
        emailContent = PlaceHolder.resolve(emailContent, placeMap);

        List<User> indexUsers = userService.queryAllWithIndexableFields();

        sendServerEmail(indexUsers, emailTitle, emailContent);

        LOG.info("发邮件的绩效考核已经结束！");

    }

    /**
     * 绩效终止前三天，给考核没有结束的用户发送邮件如：
     * <p>
     * 
     * &nbsp&nbsp&nbsp 1、没有写自评的用户发邮件
     * <p>
     * 
     * &nbsp&nbsp&nbsp 2、别人邀请你自己还没有评价的的列表
     * <p>
     * 
     * &nbsp&nbsp&nbsp 3、自己要别人评价，好友还未评价的用户
     * <p>
     * 
     * &nbsp&nbsp&nbsp 4、没有给汇报人评价的用户
     * <p>
     * 
     */
    public void sendPerfDeadlineEmail() {

        EmailText emailText = emailTextDAO.query(String.valueOf(SendEmailType.PERF_DEADLINE.getId()));
        if (emailText == null) {

            LOG.info("查询emailText表根据email类型" + SendEmailType.PERF_DEADLINE.getName() + "失败！");
            return;

        }

        LOG.debug("发邮件的关闭绩效考核开始啦！");

        PerfTime perfTime = perfService.getCurrent();
        String emailTitle = StringUtils.trimToEmpty(emailText.getEmailTitle());
        String emailContent = StringUtils.trimToEmpty(emailText.getEmailContent());

        Map<String, Object> holderMap = new HashMap<String, Object>(1);
        holderMap.put(PlaceHolderType.XX_PLACE.getName(), perfTime.getPerfTitle());
        emailTitle = PlaceHolder.resolve(emailTitle, holderMap);
        emailContent = PlaceHolder.resolve(emailContent, holderMap);

        // 没有写自评的用户发邮件
        List<User> noSelfPerfUser = notifyService.getNoSelfPerfUsersForCurrentPerf();
        if (CollectionUtils.isNotEmpty(noSelfPerfUser))
            sendServerEmail(noSelfPerfUser, emailTitle, emailContent);

        // 别人邀请你自己还没有评价的的列表
        List<User> noAccessInviteUserList = notifyService.getInvitedSelfNotCompleteUser();
        if (CollectionUtils.isNotEmpty(noAccessInviteUserList))
            sendServerEmail(noAccessInviteUserList, emailTitle, emailContent);

        // 自己要别人评价，好友还未评价的用户
        List<User> noAccessSelfList = notifyService.getNotPeerPerfsNotCompleteUser();
        if (CollectionUtils.isNotEmpty(noAccessSelfList))
            sendServerEmail(noAccessSelfList, emailTitle, emailContent);

        // 没有给汇报人评价的用户
        List<User> noReportUserList = notifyService.getMyReportUserForNoAccessPerf();
        if (CollectionUtils.isNotEmpty(noReportUserList))
            sendServerEmail(noReportUserList, emailTitle, emailContent);

        LOG.debug("发邮件的关闭绩效考核已经结束！");

    }

    /**
     * 给周报给自己和主管发送邮件
     * 
     * @param report
     *            周报的内容
     * @param user
     *            自己的信息
     */
    public void sendSubmmitedWeeklyReportEmail(WeeklyReport report, User user) {

        EmailText emailText = emailTextDAO.query(String.valueOf(SendEmailType.WEEKLY_REPORT_SUBMMITED.getId()));
        if (emailText == null) {

            LOG.info("查询emailText表根据email类型" + SendEmailType.WEEKLY_REPORT_SUBMMITED.getName() + "失败！");
            return;

        }

        submmitedWeeklyReportEmail(report, user, emailText);

    }

    /**
     * 给补交的周报给子自己和主管发送邮件
     * 
     * @param report
     *            补交周报的内容
     * @param user
     *            要发送邮件信息
     */
    public void sendSubmmitedPayWeeklyReportEmail(WeeklyReport report, User user) {

        EmailText emailText = emailTextDAO.query(String.valueOf(SendEmailType.WEEKLY_REPORT_PAY.getId()));
        if (emailText == null) {

            LOG.info("查询emailText表根据email类型" + SendEmailType.WEEKLY_REPORT_PAY.getName() + "失败！");
            return;

        }

        submmitedWeeklyReportEmail(report, user, emailText);

    }

    /**
     * 
     * 每周六00:00给没有写周报的用户发送提示邮件
     * 
     */
    public void sendTipWeeklyReportEmail() {

        EmailText emailText = emailTextDAO.query(String.valueOf(SendEmailType.WEEKLY_REPORT_TIP.getId()));
        if (emailText == null) {

            LOG.info("查询emailText表根据email类型" + SendEmailType.WEEKLY_REPORT_TIP.getName() + "失败！");
            return;

        }

        LOG.info("提示周报还没有写的用户发送邮件开始啦！");

        List<User> noWeeklyReportUserList = notifyService.getNoWeeklyReprUsersForCurrentWeek();

        Date monday = DateTimeUtil.getMondayOfWeek(DateTimeUtil.getCurrDate());// 周一
        Date sunday = DateTimeUtil.addDatesToDate(monday, 6);
        String emailTitle = StringUtils.trimToEmpty(emailText.getEmailTitle());
        String emailContent = StringUtils.trimToEmpty(emailText.getEmailContent());

        Map<String, Object> placeMap = new HashMap<String, Object>();
        placeMap.put(PlaceHolderType.XX_PLACE.getName(), DateTimeUtil.getSimpleDateFormat(monday) + "-" + DateTimeUtil.getSimpleDateFormat(sunday));
        emailContent = PlaceHolder.resolve(emailContent, placeMap);

        // 给没有提交周报发邮件
        for(User user : noWeeklyReportUserList){
        	sendServerEmail(Arrays.asList(user), emailTitle, emailContent);
        }
        
        // 给没有提交周报的主管发邮件
        for(User user : noWeeklyReportUserList){
        	
        	User leaderUser = userService.query(user.getManagerId());
        	
        	if(leaderUser != null){
        		
                EmailText text = emailTextDAO.query(String.valueOf(SendEmailType.TIP_LEADER_NO_SUMMBIT_WEEKLY_REPORT.getId()));
        		String content = StringUtils.trimToEmpty(text.getEmailContent());
        		String title   = StringUtils.trimToEmpty(text.getEmailTitle());
        		
        		placeMap.put(PlaceHolderType.XX_PLACE.getName(), user.getName());
        		placeMap.put(PlaceHolderType.YY_PLACE.getName(), DateTimeUtil.getSimpleDateFormat(monday) + "-" + DateTimeUtil.getSimpleDateFormat(sunday));
        		
        		content = PlaceHolder.resolve(content, placeMap);
        	    
        		sendServerEmail(Arrays.asList(leaderUser), title, content);
        		
        	}
        	
        }

    }

    /**
     * 给user发邮件，表明领导已经点评你的周报
     * 
     * @param leader
     *            上级用户
     * @param user
     *            自己
     * @param content
     *            点评周报内容
     */
    public void sendWeelyAccessEmail(User leader, User user, String content) {
        EmailText emailText = emailTextDAO.query(String.valueOf(SendEmailType.LEADER_ACCESS_WEEKLY_REPORT.getId()));
        if (emailText == null) {

            LOG.info("查询emailText表根据email类型" + SendEmailType.WEEKLY_REPORT_TIP.getName() + "失败！");
            return;

        }

        LOG.info("领导点评用户的周报开始发送啦");

        String title = StringUtils.trimToEmpty(emailText.getEmailTitle());
        Map<String, Object> placeMap = new HashMap<String, Object>(4);
        placeMap.put(PlaceHolderType.XX_PLACE.getName(), leader.getName());
        title = PlaceHolder.resolve(title, placeMap);

        String emailContent = StringUtils.trimToEmpty(emailText.getEmailContent());
        placeMap.put(PlaceHolderType.XX_PLACE.getName(), user.getName());
        placeMap.put(PlaceHolderType.YY_PLACE.getName(), leader.getName());
        placeMap.put(PlaceHolderType.ZZ_PLACE.getName(), content);
        emailContent = PlaceHolder.resolve(emailContent, placeMap);

        sendServerEmail(Arrays.asList(user), title, emailContent);
    }

    /**
     * 提醒领导还没有对下属进行点评周报
     * 
     * @param user
     *            自己
     */
    public void sendTipLeaderWeelyNoAccessEmail(User user) {
        EmailText emailText = emailTextDAO.query(String.valueOf(SendEmailType.TIP_LEADER_ACCESS_WEEKLY_REPORT.getId()));
        if (emailText == null) {

            LOG.info("查询emailText表根据email类型" + SendEmailType.WEEKLY_REPORT_TIP.getName() + "失败！");
            return;

        }

        LOG.info("提醒领导点评下属的周报开始发送啦");

        String title = StringUtils.trimToEmpty(emailText.getEmailTitle());
        Map<String, Object> placeMap = new HashMap<String, Object>(3);

        String emailContent = StringUtils.trimToEmpty(emailText.getEmailContent());
        placeMap.put(PlaceHolderType.XX_PLACE.getName(), user.getName());
        emailContent = PlaceHolder.resolve(emailContent, placeMap);

        sendServerEmail(Arrays.asList(user), title, emailContent);

        LOG.info("提醒领导点评下属的周报结束啦");
    }

    /**
     * 提醒没有写周报的用户在每周五下午一点
     * 
     * @param user
     *            自己
     */
    public void sendWeelyNoSummbitEmail() {
        EmailText emailText = emailTextDAO.query(String.valueOf(SendEmailType.TIP_NO_SUMMBIT_WEEKLY_REPORT.getId()));
        if (emailText == null) {

            LOG.info("查询emailText表根据email类型" + SendEmailType.WEEKLY_REPORT_TIP.getName() + "失败！");
            return;

        }

        LOG.info("提醒周报没有写开始发送啦");

        List<User> noWeeklyReportUserList = notifyService.getNoWeeklyReprUsersForCurrentWeek();

        Date monday = DateTimeUtil.getMondayOfWeek(DateTimeUtil.getCurrDate());// 周一
        Date sunday = DateTimeUtil.addDatesToDate(monday, 6);

        String title = StringUtils.trimToEmpty(emailText.getEmailTitle());
        Map<String, Object> placeMap = new HashMap<String, Object>(3);
        placeMap.put(PlaceHolderType.XX_PLACE.getName(), DateTimeUtil.getSimpleDateFormat(monday) + "-" + DateTimeUtil.getSimpleDateFormat(sunday));
        title = PlaceHolder.resolve(title, placeMap);

        String emailContent = StringUtils.trimToEmpty(emailText.getEmailContent());
        placeMap.put(PlaceHolderType.XX_PLACE.getName(), "尊敬的用户");
        emailContent = PlaceHolder.resolve(emailContent, placeMap);
        
        for(User user : noWeeklyReportUserList){
        	sendServerEmail(Arrays.asList(user), title, emailContent);
        }
        LOG.info("提醒周报没有写开始结束啦");
    }

    /**
     * 发邮件小红花兑换勋章
     * 
     * @param fromUser
     *            兑换人民币的用户
     * @param flowerNum
     *            兑换小红花的数目
     */
    public void sendRewardEmail(User fromUser, int flowerNum, RewardItem item) {

        EmailText emailText = emailTextDAO.query(String.valueOf(SendEmailType.OBTATIN_VIRTUAL_REWARD.getId()));
        if (emailText == null) {

            LOG.info("查询emailText表根据email类型" + SendEmailType.OBTATIN_VIRTUAL_REWARD.getName() + "失败！");
            return;

        }

        LOG.info("给邀请好友发送邮件开始啦！");

        String inviteEmailTitle = StringUtils.trimToEmpty(emailText.getEmailTitle());
        Map<String, Object> placeMap = new HashMap<String, Object>(4);
        placeMap.put(PlaceHolderType.XX_PLACE.getName(), item.getName());
        inviteEmailTitle = PlaceHolder.resolve(inviteEmailTitle, placeMap);

        String emailContent = StringUtils.trimToEmpty(emailText.getEmailContent());
        placeMap.put(PlaceHolderType.XX_PLACE.getName(), fromUser.getName());
        placeMap.put(PlaceHolderType.YY_PLACE.getName(), flowerNum);
        placeMap.put(PlaceHolderType.ZZ_PLACE.getName(), item.getName());
        emailContent = PlaceHolder.resolve(emailContent, placeMap);

        sendServerEmail(Arrays.asList(fromUser), inviteEmailTitle, emailContent);

    }

    /**
     * 发邮件小红花兑换人民币申请成功
     * 
     * @param fromUser
     *            兑换人民币的用户
     * @param flowerNum
     *            兑换小红花的数目
     * @param money
     *            兑换的金额
     */
    public void sendRewardMoneySuccessEmail(User fromUser, int flowerNum, int money) {

        EmailText emailText = emailTextDAO.query(String.valueOf(SendEmailType.OBTAIN_MONEY_REWARD.getId()));
        if (emailText == null) {

            LOG.info("查询emailText表根据email类型" + SendEmailType.OBTAIN_MONEY_REWARD.getName() + "失败！");
            return;

        }

        LOG.info("给邀请好友发送邮件开始啦！");

        String inviteEmailTitle = StringUtils.trimToEmpty(emailText.getEmailTitle());
        Map<String, Object> placeMap = new HashMap<String, Object>(4);
        placeMap.put(PlaceHolderType.XX_PLACE.getName(), money);
        inviteEmailTitle = PlaceHolder.resolve(inviteEmailTitle, placeMap);

        String emailContent = StringUtils.trimToEmpty(emailText.getEmailContent());
        placeMap.put(PlaceHolderType.XX_PLACE.getName(), fromUser.getName());
        placeMap.put(PlaceHolderType.YY_PLACE.getName(), flowerNum);
        placeMap.put(PlaceHolderType.ZZ_PLACE.getName(), money);
        emailContent = PlaceHolder.resolve(emailContent, placeMap);

        sendServerEmail(Arrays.asList(fromUser), inviteEmailTitle, emailContent);

    }

    /**
     * 发邮件小红花兑换人民币申请
     * 
     * @param fromUser
     *            兑换人民币的用户
     * @param flowerNum
     *            兑换小红花的数目
     * @param money
     *            兑换的金额
     */
    public void sendApplyRewardMoneyEmail(User fromUser, int flowerNum, int money) {

        EmailText emailText = emailTextDAO.query(String.valueOf(SendEmailType.REWARD_CONFIRM_FLOWER.getId()));
        if (emailText == null) {

            LOG.info("查询emailText表根据email类型" + SendEmailType.REWARD_CONFIRM_FLOWER.getName() + "失败！");
            return;

        }

        LOG.info("给邀请好友发送邮件开始啦！");

        String inviteEmailTitle = StringUtils.trimToEmpty(emailText.getEmailTitle());
        Map<String, Object> placeMap = new HashMap<String, Object>(4);
        placeMap.put(PlaceHolderType.XX_PLACE.getName(), money);
        inviteEmailTitle = PlaceHolder.resolve(inviteEmailTitle, placeMap);

        String emailContent = StringUtils.trimToEmpty(emailText.getEmailContent());
        placeMap.put(PlaceHolderType.XX_PLACE.getName(), fromUser.getName());
        placeMap.put(PlaceHolderType.YY_PLACE.getName(), flowerNum);
        placeMap.put(PlaceHolderType.ZZ_PLACE.getName(), money);
        emailContent = PlaceHolder.resolve(emailContent, placeMap);

        sendServerEmail(Arrays.asList(fromUser), inviteEmailTitle, emailContent);

    }

    /**
     * 发邮件给得到小红花的人
     * 
     * @param fromUser
     *            赠送小红花的用户
     * @param toUser
     *            收到小红花的用户
     * @param reason
     *            送小红花的原因
     */
    public void sendObtainFlowerEmail(User fromUser, User toUser, String reason) {

        EmailText emailText = emailTextDAO.query(String.valueOf(SendEmailType.LEADER_CONFIRM_FLOWER_FOR_SENDER.getId()));
        if (emailText == null) {

            LOG.info("查询emailText表根据email类型" + SendEmailType.LEADER_CONFIRM_FLOWER_FOR_SENDER.getName() + "失败！");
            return;

        }

        LOG.info("给邀请好友发送邮件开始啦！");

        String inviteEmailTitle = StringUtils.trimToEmpty(emailText.getEmailTitle());
        Map<String, Object> placeMap = new HashMap<String, Object>(4);

        String emailContent = StringUtils.trimToEmpty(emailText.getEmailContent());
        placeMap.put(PlaceHolderType.XX_PLACE.getName(), toUser.getName());
        placeMap.put(PlaceHolderType.YY_PLACE.getName(), fromUser.getName());
        placeMap.put(PlaceHolderType.ZZ_PLACE.getName(), reason);
        emailContent = PlaceHolder.resolve(emailContent, placeMap);

        sendServerEmail(Arrays.asList(toUser), inviteEmailTitle, emailContent);

    }

    /**
     * 发邮件发给赠送小红花的人
     * 
     * @param fromUser
     *            赠送小红花的用户
     * @param toUser
     *            收到小红花的用户
     */
    public void sendAwardFlowerUserEmail(User fromUser, User toUser) {

        EmailText emailText = emailTextDAO.query(String.valueOf(SendEmailType.LEADER_AGREE_FLOWER_FOR_OBTAIN.getId()));
        if (emailText == null) {

            LOG.info("查询emailText表根据email类型" + SendEmailType.LEADER_AGREE_FLOWER_FOR_OBTAIN.getName() + "失败！");
            return;

        }

        LOG.info("给邀请好友发送邮件开始啦！");

        String inviteEmailTitle = StringUtils.trimToEmpty(emailText.getEmailTitle());
        Map<String, Object> placeMap = new HashMap<String, Object>(4);
        placeMap.put(PlaceHolderType.XX_PLACE.getName(), toUser.getName());
        inviteEmailTitle = PlaceHolder.resolve(inviteEmailTitle, placeMap);
        
        placeMap.put(PlaceHolderType.XX_PLACE.getName(), fromUser.getName());
        inviteEmailTitle = PlaceHolder.resolve(inviteEmailTitle, placeMap);

        String emailContent = StringUtils.trimToEmpty(emailText.getEmailContent());
        placeMap.put(PlaceHolderType.YY_PLACE.getName(), toUser.getName());
        emailContent = PlaceHolder.resolve(emailContent, placeMap);

        sendServerEmail(Arrays.asList(fromUser), inviteEmailTitle, emailContent);

    }

    /**
     * 主管确认的邮件
     * 
     */
    public void sendApplyFlowerEmail(User fromUser, User leadUser, User toUser, String reason) {

        EmailText emailText = emailTextDAO.query(String.valueOf(SendEmailType.LEADER_CONFIRM_FLOWER.getId()));
        if (emailText == null) {

            LOG.info("查询emailText表根据email类型" + SendEmailType.LEADER_CONFIRM_FLOWER.getName() + "失败！");
            return;

        }

        LOG.info("给邀请好友发送邮件开始啦！");

        String inviteEmailTitle = StringUtils.trimToEmpty(emailText.getEmailTitle());
        Map<String, Object> placeMap = new HashMap<String, Object>(5);
        placeMap.put(PlaceHolderType.XX_PLACE.getName(), toUser.getName());
        inviteEmailTitle = PlaceHolder.resolve(inviteEmailTitle, placeMap);

        String emailContent = StringUtils.trimToEmpty(emailText.getEmailContent());
        Department deparment = departmentService.queryById(fromUser.getDepartmentId());
        if (deparment != null)
            placeMap.put(PlaceHolderType.XX_PLACE.getName(), deparment.getDepartmentName());
        else
            placeMap.put(PlaceHolderType.XX_PLACE.getName(), "");

        placeMap.put(PlaceHolderType.YY_PLACE.getName(), fromUser.getName());
        placeMap.put(PlaceHolderType.ZZ_PLACE.getName(), toUser.getName());
        placeMap.put(PlaceHolderType.MM_PLACE.getName(), reason);
//        String hash = TextHashUtils.MD5.digestHex(MD5_PREFIX + "_" + fromUser.getId() + "_" + toUser.getId());
        StringBuilder url = new StringBuilder();
      //  url.append(RenrenProfileConstants.PROFILE_MAIN_URL).append("/reward/index");
//        placeMap.put(PlaceHolderType.NN_PLACE.getName(), url);
        url.append(OcProfileConstants.PROFILE_MAIN_URL).append("/reward/agree?from=").append(fromUser.getId()).append("&to=").append(toUser.getId());
        placeMap.put(PlaceHolderType.NN_PLACE.getName(), url);
        StringBuilder url2 = new StringBuilder();
        url2.append(OcProfileConstants.PROFILE_MAIN_URL).append("/reward/disagree?from=").append(fromUser.getId()).append("&to=").append(toUser.getId());
        placeMap.put(PlaceHolderType.TT_PLACE.getName(), url2);

        emailContent = PlaceHolder.resolve(emailContent, placeMap);

        sendNoServerEmail(Arrays.asList(leadUser), inviteEmailTitle, emailContent, fromUser);

    }

    /**
     * 给邀请好友发送邮件
     * 
     * @param user
     * @param inviUser
     */
    public void sendInviteFriendAccessEmail(User user, User inviUser) {

        EmailText emailText = emailTextDAO.query(String.valueOf(SendEmailType.INVITE_FRIEND_ACCESS.getId()));
        if (emailText == null) {

            LOG.info("查询emailText表根据email类型" + SendEmailType.INVITE_FRIEND_ACCESS.getName() + "失败！");
            return;

        }

        LOG.info("给邀请好友发送邮件开始啦！");

        PerfTime perfTime = perfService.getCurrent();

        // 给添加邀请好友的邮件主题
        String inviteEmailTitle = StringUtils.trimToEmpty(emailText.getEmailTitle());
        Map<String, Object> placeMap = new HashMap<String, Object>(4);
        placeMap.put(PlaceHolderType.XX_PLACE.getName(), user.getName());
        placeMap.put(PlaceHolderType.YY_PLACE.getName(), perfTime.getPerfTitle());
        inviteEmailTitle = PlaceHolder.resolve(inviteEmailTitle, placeMap);

        // 给添加邀请好友的邮件内容
        String inviteEmailContent = StringUtils.trimToEmpty(emailText.getEmailContent());
        placeMap.put(PlaceHolderType.XX_PLACE.getName(), inviUser.getName());
        placeMap.put(PlaceHolderType.YY_PLACE.getName(), user.getName());
        placeMap.put(PlaceHolderType.ZZ_PLACE.getName(), perfTime.getPerfTitle());
        placeMap.put(PlaceHolderType.MM_PLACE.getName(), String.valueOf(user.getId()));
        inviteEmailContent = PlaceHolder.resolve(inviteEmailContent, placeMap);

        sendNoServerEmail(Arrays.asList(inviUser), inviteEmailTitle, inviteEmailContent, user);

    }

    /**
     * 邀请人发送邮件给自己一封邮件
     * 
     * @param user
     *            要邀请人的用户信息
     * @param inviUser
     *            要被邀请人的用户信息
     */
    public void sendDelFriendAccessForSelfEmail(User user, User inviUser) {

        EmailText emailText = emailTextDAO.query(String.valueOf(SendEmailType.DELETE_FRIEND_ACCESS_SELF.getId()));
        if (emailText == null) {

            LOG.info("查询emailText表根据email类型" + SendEmailType.DELETE_FRIEND_ACCESS_SELF.getName() + "失败！");
            return;

        }

        PerfTime perfTime = perfService.getCurrent();

        // 给邀请人发邮件的主题
        String inviteEmailTitle = StringUtils.trimToEmpty(emailText.getEmailTitle());
        Map<String, Object> placeMap = new HashMap<String, Object>(3);
        placeMap.put(PlaceHolderType.XX_PLACE.getName(), inviUser.getName());
        inviteEmailTitle = PlaceHolder.resolve(inviteEmailTitle, placeMap);

        // 给邀请好友发邮件的内容
        String inviteEmailContent = StringUtils.trimToEmpty(emailText.getEmailContent());
        placeMap.put(PlaceHolderType.XX_PLACE.getName(), user.getName());
        placeMap.put(PlaceHolderType.YY_PLACE.getName(), inviUser.getName());
        placeMap.put(PlaceHolderType.ZZ_PLACE.getName(), perfTime.getPerfTitle());
        inviteEmailContent = PlaceHolder.resolve(inviteEmailContent, placeMap);

        sendNoServerEmail(Arrays.asList(user), inviteEmailTitle, inviteEmailContent, user);

    }

    /**
     * 邀请人发送邮件给被邀请人一封邮件
     * 
     * @param user
     *            要邀请人的用户信息
     * @param inviUser
     *            要被邀请人的用户信息
     */
    public void sendDelFriendAccessForFriendEmail(User user, User inviUser) {

        EmailText emailText = emailTextDAO.query(String.valueOf(SendEmailType.DELETE_FRIEND_ACCESS_INVITE.getId()));
        if (emailText == null) {

            LOG.info("查询emailText表根据email类型" + SendEmailType.DELETE_FRIEND_ACCESS_INVITE.getName() + "失败！");
            return;

        }

        PerfTime perfTime = perfService.getCurrent();

        // 给邀请人发邮件的主题
        String inviteEmailTitle = StringUtils.trimToEmpty(emailText.getEmailTitle());
        Map<String, Object> placeMap = new HashMap<String, Object>();
        placeMap.put(PlaceHolderType.XX_PLACE.getName(), user.getName());
        inviteEmailTitle = PlaceHolder.resolve(inviteEmailTitle, placeMap);

        // 给邀请好友发邮件的内容
        String inviteEmailContent = StringUtils.trimToEmpty(emailText.getEmailContent());
        placeMap.put(PlaceHolderType.XX_PLACE.getName(), inviUser.getName());
        placeMap.put(PlaceHolderType.YY_PLACE.getName(), user.getName());
        placeMap.put(PlaceHolderType.ZZ_PLACE.getName(), perfTime.getPerfTitle());
        inviteEmailContent = PlaceHolder.resolve(inviteEmailContent, placeMap);

        sendNoServerEmail(Arrays.asList(inviUser), inviteEmailTitle, inviteEmailContent, user);

    }

    /**
     * 服务器发送的邮件
     * 
     * @param userList
     *            发送给收件人的用户信息
     * @param emailTitle
     *            邮件的标题
     * @param emailContent
     *            邮件的内容
     */
    private static void sendServerEmail(List<User> userList, String emailTitle, String emailContent) {

        User user = new User();
        user.setName(SEND_EMAIL_HOST_NAME);
        user.setEmail(SEND_EMAIL_HOST);

        sendNoServerEmail(userList, emailTitle, emailContent, user);

    }

    /**
     * 指定用户发送邮件
     * 
     * @param userList
     *            发送给收件人的用户信息
     * @param emailTitle
     *            邮件的标题
     * @param emailContent
     *            邮件的内容
     * @param fromUser
     *            要发送邮件的用户信息
     */
    private static void sendNoServerEmail(List<User> userList, String emailTitle, String emailContent, User fromUser) {

        ProfileMail mail = new ProfileMail(EMAIL_HOST);
        Collection<EmailUser> emaiNoSelfList = ProfileMail.convertEmailUserList(userList);

        try {

            mail.sendEmail(new EmailUser(fromUser.getName(), fromUser.getEmail()), emaiNoSelfList, emailTitle, emailContent);

        } catch (EmailException e) {

           // LOG.error("发送邮箱失败!", e);
        }

    }

    private void submmitedWeeklyReportEmail(WeeklyReport report, User user, EmailText emailText) {

        LOG.debug("发邮件的提交周报开始啦！");

        // 周报的主题本周的周一和周日的日期精度是年月日
        String emailTitle = StringUtils.trimToEmpty(emailText.getEmailTitle());
        Map<String, Object> placeMap = new HashMap<String, Object>(5);
        placeMap.put(PlaceHolderType.XX_PLACE.getName(), user.getName());
        placeMap.put(
                PlaceHolderType.YY_PLACE.getName(),
                DateTimeUtil.getSimpleDateFormat(report.getWeekDate()) + "~"
                        + DateTimeUtil.getSimpleDateFormat(DateTimeUtil.addDatesToDate(report.getWeekDate(), 6)));
        emailTitle = PlaceHolder.resolve(emailTitle, placeMap);

        // 周报的内容
        String emailContent = StringUtils.trimToEmpty(emailText.getEmailContent());
        placeMap.put(PlaceHolderType.MM_PLACE.getName(), String.valueOf(user.getId()));
        placeMap.put(PlaceHolderType.ZZ_PLACE.getName(), report.getContentDone());
        placeMap.put(PlaceHolderType.NN_PLACE.getName(), report.getContentPlan());
        placeMap.put(PlaceHolderType.TT_PLACE.getName(), report.getQa());

        emailContent = PlaceHolder.resolve(emailContent, placeMap);

        // 主管的信息和用户的信息
        User toEmailMangerUser = userService.query(user.getManagerId());
        List<User> toMails = new ArrayList<User>();
        if (toEmailMangerUser != null) {
            toMails.add(toEmailMangerUser);
        }
        toMails.add(user);

        // 添加用户指定的Emails
        String emailTos = report.getEmailTos();
        String[] emailToArray = StringUtils.split(emailTos, ";");
        if (!ArrayUtils.isEmpty(emailToArray)) {
            for (String email : emailToArray) {
                toMails.add(new User(email, null));
            }
        }

        sendNoServerEmail(toMails, emailTitle, emailContent, user);

        if (LOG.isDebugEnabled()) {
            LOG.debug("发邮件的提交周报结束啦！");
        }
    }

//    /**
//     * 积分申请邮件，发送给主管
//     * 
//     */
//    public void sendApplyScoreEmail(User fromUser, User toUser, ScoreInfo scoreInfo) {
//
//        EmailText emailText = emailTextDAO.query(String.valueOf(SendEmailType.LEADER_CONFIRM_SCORE.getId()));
//        if (emailText == null) {
//
//            LOG.info("查询emailText表根据email类型" + SendEmailType.LEADER_CONFIRM_FLOWER.getName() + "失败！");
//            return;
//
//        }
//
//        LOG.info("积分申请发送邮件开始啦！");
//
//        String inviteEmailTitle = StringUtils.trimToEmpty(emailText.getEmailTitle());
//        Map<String, Object> placeMap = new HashMap<String, Object>(5);
//        placeMap.put(PlaceHolderType.XX_PLACE.getName(), fromUser.getName());
//        placeMap.put(PlaceHolderType.YY_PLACE.getName(), scoreInfo.getFromInfo());
//        inviteEmailTitle = PlaceHolder.resolve(inviteEmailTitle, placeMap);
//
//        String emailContent = StringUtils.trimToEmpty(emailText.getEmailContent());
//        StringBuilder agreeUrl = new StringBuilder();
//        agreeUrl.append(OcProfileConstants.PROFILE_MAIN_URL).append("/score/apply/confirmScore?flag=2&info_id=" + scoreInfo.getId());
//        StringBuilder refuseUrl = new StringBuilder();
//        refuseUrl.append(OcProfileConstants.PROFILE_MAIN_URL).append("/score/apply/confirmScore?flag=3&info_id=" + scoreInfo.getId());
//        
//        placeMap.put(PlaceHolderType.ZZ_PLACE.getName(), scoreInfo.getApplyReason());
//        placeMap.put(PlaceHolderType.MM_PLACE.getName(), agreeUrl);
//        placeMap.put(PlaceHolderType.NN_PLACE.getName(), refuseUrl);
//        
//
//        emailContent = PlaceHolder.resolve(emailContent, placeMap);
//
//        sendNoServerEmail(Arrays.asList(toUser), inviteEmailTitle, emailContent, fromUser);
//
//    }
    
//    /**
//     * 积分申请确认邮件，发送给申请人
//     * 
//     */
//    public void sendConfirmScoreEmail(User leadUser, User toUser, ScoreInfo scoreInfo, int flag) {
//
//        EmailText emailText = emailTextDAO.query(String.valueOf(SendEmailType.LEADER_CONFIRM_SCORE.getId()));
//        if (emailText == null) {
//
//            LOG.info("查询emailText表根据email类型" + SendEmailType.LEADER_CONFIRM_FLOWER.getName() + "失败！");
//            return;
//
//        }
//
//        LOG.info("积分申请发送邮件开始啦！");
//
//        String inviteEmailTitle = StringUtils.trimToEmpty(emailText.getEmailTitle());
//        Map<String, Object> placeMap = new HashMap<String, Object>(5);
//        placeMap.put(PlaceHolderType.XX_PLACE.getName(), leadUser.getName());
//        placeMap.put(PlaceHolderType.YY_PLACE.getName(), flag == 2 ? "同意" : "拒绝");
//        placeMap.put(PlaceHolderType.ZZ_PLACE.getName(), scoreInfo.getFromInfo());
//        inviteEmailTitle = PlaceHolder.resolve(inviteEmailTitle, placeMap);
//
//        String emailContent = StringUtils.trimToEmpty(emailText.getEmailContent());
//        emailContent = PlaceHolder.resolve(emailContent, placeMap);
//
//        sendNoServerEmail(Arrays.asList(toUser), inviteEmailTitle, emailContent, leadUser);
//
//    }
    
    public static void main(String[] args) {
//        String[] strs = { "何文", "2012-03-26~2012-04-01", "<p>测试</p>", "<p>测试</p>" };
//        String content = "xx已提交yy的周报，地址：<a href =\"http://profile.renren-inc.com/weeklyreport/my\">http://profile.renren-inc.com/weeklyreport/my</a>。<br><strong>周报全文如下：</strong><br><strong> 本周要做的:</strong><br>zz<strong> 下周周要做的:</strong><br>nn";
//
//        System.out.println(StringUtils.replaceEach(content, new String[] { "xx", "yy", "zz", "nn" }, strs));
//
//        Map<String, Object> holderMap = new HashMap<String, Object>();
//        holderMap.put("xx", "2012年Q1");
//        System.out.println(PlaceHolder.resolve("${xx}的考评即将截止", holderMap));
    	
    	

		List<User> users = new ArrayList<User>();
		
		User user2 = new User();
		user2.setName("何文");
		user2.setEmail("wen.he1@renren-inc.com");
		
		users.add(user2);
		
		StringBuilder emailContent = new StringBuilder();
		String tdStr1 = "<td width=230 nowrap valign=top style='width:105.0pt;border-top:solid #4BACC6 1.0pt;border-left:none;border-bottom:solid #4BACC6 1.0pt;border-right:none;padding:0cm 5.4pt 0cm 5.4pt;height:12.75pt'><p class=MsoNormal align=left style='text-align:left'><b><span style='font-size:10.0pt;font-family:宋体;color:#31849B'>";
		String tdStr2 = "</span></b><span lang=EN-US><o:p></o:p></span></p></td>";
		emailContent.append("<body lang=ZH-CN link=blue vlink=purple style='text-justify-trim:punctuation'>")
			.append("<div class=WordSection1>")
			.append("<table class=MsoNormalTable border=0 cellspacing=0 cellpadding=0 width=700 style='width:525.0pt;border-collapse:collapse'>")
			.append(" <tr style='height:12.75pt'>")
			.append(tdStr1).append("员工编号").append(tdStr2)
			.append(tdStr1).append("部门").append(tdStr2)
			.append(tdStr1).append("姓名").append(tdStr2).append(" </tr>");

		String contentTd1 = " <td width=230 nowrap valign=bottom style='width:105.0pt;background:#D2EAF1;padding:0cm 5.4pt 0cm 5.4pt;height:12.75pt'><p class=MsoNormal><span lang=EN-US style='font-size:10.0pt;font-family:"+"Arial"+",'sans-serif'"+"'>";
		String contentTd2 = "</span><span lang=EN-US style='font-size:10.0pt;font-family:"+"Arial"+",'sans-serif'"+"'><o:p></o:p></span></p></td>";
		
		emailContent.append(" <tr style='height:12.75pt'>");
		emailContent.append(contentTd1+"CIAC008008"+contentTd2);
		emailContent.append(contentTd1+"三反五反范文芳/weqfwqefasf未访问"+contentTd2);
		emailContent.append(contentTd1+"何文"+contentTd2).append("</tr>");
		
		emailContent.append(" <tr style='height:12.75pt'>");
		emailContent.append(contentTd1+"CIAC008008"+contentTd2);
		emailContent.append(contentTd1+"三反五反范文芳/weqfwqefasf未访问"+contentTd2);
		emailContent.append(contentTd1+"何文2"+contentTd2).append("</tr>");
		
		emailContent.append(" <tr style='height:12.75pt'>");
		emailContent.append(contentTd1+"CIAC008008"+contentTd2);
		emailContent.append(contentTd1+"三反五反范文芳/weqfwqefasf未访问"+contentTd2);
		emailContent.append(contentTd1+"何文3"+contentTd2).append("</tr>");
		emailContent.append("</table>");
		
		emailContent.append("<p class=MsoNormal><span lang=EN-US style='color:black'><o:p>&nbsp;</o:p></span></p><p class=MsoNormal><span style='font-family:宋体;color:red'>注意：上面的名单部分人已经离职，请离职人员的经理发邮件告知我，谢谢！</span><span lang=EN-US style='color:black'><o:p></o:p></span></p>");

        sendServerEmail(users, "who系统未提交周报通知", emailContent.toString());
    
    }

}
