package com.orientalcomics.profile.biz.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orientalcomics.profile.biz.dao.UserWhiteListDAO;
import com.orientalcomics.profile.biz.model.UserWhiteList;


@Service
public class UserWhiteListService {
    
	@Autowired
    private UserWhiteListDAO userWhiteListDAO;
	


	/***
	 * <p> 用户查看周报的白名单
	 * 
     * <p>1、总监级白名单（可相互查看周报）：
	 * <p>   李伟、周欣、庄宏斌、王剑、汤成信
		
	 * <p> 2、经理级白名单（可相互查看周报）：
	 * <p> 李晓堂、罗缌洋、张义民、吴昊、潘磊、李敏、
		    洪振亚、张铁安、李合、李金山、刘启荣、闫强、于杨、黄炜元、
		    赖正、徐瑜骏、姜戈
		
	 * <p> s3、总监级的几位同事可以查看经理级的同事的周报。
	 * 
	 * @param userId
	 * @param viewId
	 * @return
	 */
	public boolean getWhiteListUserViewWeeklyReport(int userId, int viewId) {
		
		if (userId == 0 || viewId == 0) {
			return false;
		}
		
		// 查询本用户是否是总监
		UserWhiteList userWhite = userWhiteListDAO.query(userId, 1);
		if (userWhite == null) {
			// 用户不是总监，看是否是经理
			UserWhiteList managerUser = userWhiteListDAO.query(userId, 2);
			if (managerUser == null) {// 用户即不是总监有不是经理就返回false
				return false;
			}else {
				// 用户是经理，看浏览周报的用户是否是经理
				UserWhiteList viewManagerUser = userWhiteListDAO.query(viewId, 2);
				if (viewManagerUser == null) {// 浏览周报的用户不是经理，那么就查询浏览周报的用户是否是总监，如果是返回true，否则返回false
					UserWhiteList viewDirectorUser = userWhiteListDAO.query(viewId, 1);
					return viewDirectorUser == null ? false : true;
				}else { //用户是经理，浏览周报的用户也是经理就返回true
					return true;
				}
						
			}
		}else {
			
			// 本用户是总监，看浏览周报的用户是否是总监，如果是就返回false，否则就返回true
			UserWhiteList viewUser = userWhiteListDAO.query(viewId, 1);
			if (viewUser == null) {
				return userWhiteListDAO.query(viewId, 2) == null ? false : true;
			}else {
				return viewUser == null ? false : true;
			}
		}
		
		
	}
}
