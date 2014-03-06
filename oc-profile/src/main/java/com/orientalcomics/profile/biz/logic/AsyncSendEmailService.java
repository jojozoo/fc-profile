package com.orientalcomics.profile.biz.logic;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orientalcomics.profile.biz.model.DailyReport;
import com.orientalcomics.profile.biz.model.RewardItem;
import com.orientalcomics.profile.biz.model.Shadow;
import com.orientalcomics.profile.biz.model.User;
import com.orientalcomics.profile.biz.model.WeeklyReport;
/**
 * 
 * <p> 项目名称：renren-profile
 *
 * <p> 类名称：AsyncSendEmailService     
 *
 * <p> 类描述：  将发邮件变成异步的发送，发邮件的线程池的大小是5个
 *
 * <p> 创建人：wen.he1@renren-inc.com  
 *
 * <p> 创建时间：2012-4-20 上午10:46:31  
 * 
 * <p> @version    1.0
 */

@Service
public class AsyncSendEmailService {

	@Autowired
	private SendEmailService sendEmailService;

	private static final int NTHREADS = 5;

	private static final Executor exec = Executors.newFixedThreadPool(NTHREADS);

	private static Logger LOG = LoggerFactory.getLogger(AsyncSendEmailService.class);
	
	public void sendApplyFlowerEmail(final User fromUser, final User leadUser, final User toUser, final String reason) {

		exec.execute(new Runnable() {

			@Override
			public void run() {
				sendEmailService.sendApplyFlowerEmail(fromUser, leadUser, toUser, reason);
			}

		});

	}
	
	public void sendNoSummbitedWeeklyUser() {

		exec.execute(new Runnable() {

			@Override
			public void run() {
				sendEmailService.sendNoSummbitedWeeklyUser();
			}

		});

	}

	public void sendApplyRewardMoneyEmail(final User fromUser, final int flowerNum, final int money) {

		exec.execute(new Runnable() {

			@Override
			public void run() {
				sendEmailService.sendApplyRewardMoneyEmail(fromUser, flowerNum, money);
			}

		});

	}

	public void sendAwardFlowerUserEmail(final User fromUser, final User toUser) {

		exec.execute(new Runnable() {

			@Override
			public void run() {
				sendEmailService.sendAwardFlowerUserEmail(fromUser, toUser);
			}
		});

	}

	public void sendDelFriendAccessForFriendEmail(final User user, final User inviUser) {

		exec.execute(new Runnable() {

			@Override
			public void run() {
				sendEmailService.sendDelFriendAccessForFriendEmail(user, inviUser);
			}

		});
	}

	public void sendDelFriendAccessForSelfEmail(final User user, final User inviUser) {

		exec.execute(new Runnable() {

			@Override
			public void run() {
				sendEmailService.sendDelFriendAccessForSelfEmail(user, inviUser);
			}

		});
	}

	public void sendInviteFriendAccessEmail(final User user, final User inviUser) {

		exec.execute(new Runnable() {

			@Override
			public void run() {
				sendEmailService.sendInviteFriendAccessEmail(user, inviUser);
			}

		});
	}

	public void sendObtainFlowerEmail(final User fromUser, final User toUser, final String reason) {

		exec.execute(new Runnable() {

			@Override
			public void run() {
				sendEmailService.sendObtainFlowerEmail(fromUser, toUser, reason);
			}

		});

	}

	public void sendPerfDeadlineEmail() {

		exec.execute(new Runnable() {

			@Override
			public void run() {
				sendEmailService.sendPerfDeadlineEmail();
			}

		});

	}

	public void sendPerfStartEmail() {
		
		exec.execute(new Runnable() {

			@Override
			public void run() {
				sendEmailService.sendPerfStartEmail();
			}

		});
		
	}

	public void sendRewardEmail(final User fromUser, final int flowerNum, final RewardItem item) {
		
		exec.execute(new Runnable() {

			@Override
			public void run() {
				sendEmailService.sendRewardEmail(fromUser, flowerNum, item);
			}

		});
		
	}

	public void sendRewardMoneySuccessEmail(final User fromUser, final int flowerNum, final int money) {
		
		exec.execute(new Runnable() {

			@Override
			public void run() {
				sendEmailService.sendRewardMoneySuccessEmail(fromUser, flowerNum, money);
			}

		});
	}

	public void sendSubmmitedPayWeeklyReportEmail(final WeeklyReport report, final User user) {
		
		if (LOG.isDebugEnabled()) {
			LOG.debug("start send Pay weeklyrepot ");
		}
		exec.execute(new Runnable() {

			@Override
			public void run() {
				sendEmailService.sendSubmmitedPayWeeklyReportEmail(report, user);
			}

		});
		
	}

	public void sendSubmmitedWeeklyReportEmail(final WeeklyReport report, final User user) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("start send weeklyrepot ");
		}
		exec.execute(new Runnable() {

			@Override
			public void run() {
				if (LOG.isDebugEnabled()) {
					LOG.debug("run send weeklyrepot ");
				}
				sendEmailService.sendSubmmitedWeeklyReportEmail(report, user);
			}

		});
		
	}

	public void sendTipLeaderWeelyNoAccessEmail(final User user) {
		
		exec.execute(new Runnable() {

			@Override
			public void run() {
				sendEmailService.sendTipLeaderWeelyNoAccessEmail(user);
			}

		});
		
	}

	public void sendTipWeeklyReportEmail() {
		
		exec.execute(new Runnable() {

			@Override
			public void run() {
				sendEmailService.sendTipWeeklyReportEmail();
			}

		});
		
	}

	public void sendWeelyAccessEmail(final User leader, final User user, final String content) {
		
		exec.execute(new Runnable() {

			@Override
			public void run() {
				sendEmailService.sendWeelyAccessEmail(leader, user, content);
			}

		});
		
	}

	public void sendWeelyNoSummbitEmail() {
		
		exec.execute(new Runnable() {

			@Override
			public void run() {
				sendEmailService.sendWeelyNoSummbitEmail();
			}

		});
		
	}
	public static void main(String[] args) {
		
	}

	//发送日报的晚报
	public void sendSubmmitedDoneDailyReport(final DailyReport report, final User user) {
		exec.execute(new Runnable() {
			@Override
			public void run() {
				sendEmailService.sendDailyDone(report,user);
			}

		});
	}

	//发送日报的早报
	public void sendSubmmitedPlanDailyReport(final DailyReport report,final  User user) {
		exec.execute(new Runnable() {
			@Override
			public void run() {
				sendEmailService.sendDailyPlan(report,user);
			}

		});
	}
	
	//发送日报的早报
		public void sendUserCreateReport(final Shadow shadow) {
			exec.execute(new Runnable() {
				@Override
				public void run() {
					sendEmailService.sendUserShadowEmail(shadowshadow);
				}

			});
		}
}
