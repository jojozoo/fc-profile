package com.orientalcomics.profile.web.controllers.admin; 

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.orientalcomics.profile.biz.dao.ShadowDAO;
import com.orientalcomics.profile.biz.dao.UserProfileDAO;
import com.orientalcomics.profile.biz.logic.UserRoleService;
import com.orientalcomics.profile.biz.logic.UserService;
import com.orientalcomics.profile.biz.model.Shadow;
import com.orientalcomics.profile.biz.model.User;
import com.orientalcomics.profile.biz.model.UserProfile;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.rest.Get;
import net.paoding.rose.web.annotation.rest.Post;

/** 
 * @author 张浩 E-mail:zhanghao@foundercomics.com 
 * @version 创建时间：2014年2月14日 下午5:08:03 
 * 类说明 用户创建
 */

public class ShadowController {
	
	
	@Autowired
	private ShadowDAO shadowDAO;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserProfileDAO userProfileDAO;
	
	@Autowired
	private UserRoleService userRoleService;
	
	/**
	 * list
	 * @param inv
	 * @return
	 */
	@Get("")
	public String index(Invocation inv){
		
		List<Shadow> shadowList = shadowDAO.queryAll();
		
		inv.addModel("shadowList",shadowList);
		
		return "shadow.vm";
	}
	
	
	/**
	 * 创建用户
	 * @param inv
	 * @param loginName
	 * @param loginPassword
	 * @param email
	 * @return
	 */
	@Post("")
	public String add(Invocation inv,
			@Param("loginName") String loginName ,//登录名
			@Param("loginPassword") String loginPassword,//密码
			@Param("email") String email//邮箱
			){
		
		String trimedName = StringUtils.trimToNull(loginName);
		if(trimedName == null){
			inv.addModel("msg", "登录名为空");
			return "shadow.vm";
		}
		
		String trimedPassword = StringUtils.trimToNull(loginPassword);
		if(trimedPassword == null){
			inv.addModel("msg", "密码为空");
			return "shadow.vm";
		}
		
		
		String trimedEmail = StringUtils.trimToNull(email);
		if(trimedEmail == null){
			inv.addModel("msg", "Email为空");
			return "shadow.vm";
		}
		
		Shadow shadow = shadowDAO.queryByName(trimedName);
		
		if(shadow != null){
			inv.addModel("msg", "登录名被占用");
			return "shadow.vm";
		}
		
		shadow = shadowDAO.queryByEmail(trimedEmail);
		
		if(shadow != null){
			inv.addModel("msg", "Email被占用");
			return "shadow.vm";
		}
		
		//添加用户
		shadow = new Shadow();
		shadow.setLoginName(trimedName);
		shadow.setLoginPassword(trimedPassword);
		shadow.setEmail(trimedEmail);
		Integer userId = shadowDAO.save(shadow);
		
		
		if	(userId != null && userId != 0){
			//创建成功
			User user = new User();
			user.setName(trimedName);
			user.setId(userId);
			user.setEmail(trimedEmail);
			
			Integer id = userService.save(user);
			if(id != null && id != 0){
				//user save成功
				 UserProfile profile = new UserProfile();
	             profile.setUserId(userId);

	             userProfileDAO.save(profile);
			}else{
				inv.addModel("msg", "Profile创建失败");
				return "shadow.vm";
			}
		}else{
			inv.addModel("msg", "用户创建失败");
			return "shadow.vm";
		}
		
		
		return "shadow.vm";
	}
}
 