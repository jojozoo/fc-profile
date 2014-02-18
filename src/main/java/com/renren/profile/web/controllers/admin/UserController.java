package com.renren.profile.web.controllers.admin;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.rest.Get;
import net.paoding.rose.web.annotation.rest.Post;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.renren.profile.biz.dao.RoleDAO;
import com.renren.profile.biz.dao.UserProfileDAO;
import com.renren.profile.biz.dao.UserRoleDAO;
import com.renren.profile.biz.logic.UserRoleService;
import com.renren.profile.biz.logic.UserService;
import com.renren.profile.biz.model.User;
import com.renren.profile.biz.model.UserProfile;
import com.renren.profile.web.annotations.AjaxJson;
import com.renren.profile.web.annotations.ProfileHtmlEscape;
import com.renren.profile.web.base.FormValidator;
import com.renren.profile.web.base.HtmlPage;
import com.renren.profile.web.config.ProfileConfigs.PageSizeConfigView;
import com.renren.profile.web.controllers.internal.LoginRequiredController;
import com.renren.profile.web.interceptors.message.PageMessages;

/**
 * 员工管理
 * 
 * @author hao.zhang
 * 
 */
public class UserController extends LoginRequiredController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserProfileDAO userProfileDAO;
	@Autowired
	private RoleDAO roleDAO;

	@Autowired
	private UserRoleDAO userRoleDAO;

	@Autowired
	private UserRoleService userRoleService;

	/**
	 * 获取员工列表
	 * 
	 * @param inv
	 * @return
	 */
	@Get( { "", "/list" })
	public String list(Invocation inv, @Param("curpage") int curPage) {

		int total = userService.countAll();
		int pageSize =getPageSize(PageSizeConfigView.ADMIN_USER);
		curPage = checkAndReturnPage(curPage, total, pageSize);

		inv.addModel("total", total);
		inv.addModel("pagesize", pageSize);
		inv.addModel("curpage", curPage);
		
		List<User> userList = userService.queryAll(curPage * pageSize, pageSize);
		Collection<Integer> userIds = Collections2.transform(userList, new Function<User,Integer>(){

			@Override
			public Integer apply(User user) {
				return user == null ? null : user.getId();
			}});
		Map<Integer, UserProfile> userPorfileList = userProfileDAO.query(userIds);
		if (userList != null) {
			inv.addModel("userList", userList);
		}
		if (userPorfileList != null) {
			inv.addModel("userProfileList", userPorfileList);
		}
		return "user_list";
	}

	/**
	 * 删除员工
	 * 
	 * @param inv
	 * @param pageMsg
	 * @param userId
	 * @return
	 */
	@Post("/delete/{userId:\\d+}")
	public String deleteUer(Invocation inv, PageMessages pageMsg,
			@Param("userId") int userId) {
		try {
			User user = userService.query(userId);
			if (user != null) {
				user.setStatus(1);
				userService.update(user);
			}
		} catch (Exception e) {
			LOG.error("删除员工失败  ", e.getMessage());
		}
		return "r:/admin/user";
	}

	/**
	 * 添加员工
	 * 
	 * @param inv
	 * @return
	 */

	@Get("/add")
	public String in_add(Invocation inv) {
		renderRolesOfUser(inv, null);
		return "user_add";
	}

	@Post( { "/add" })
	@AjaxJson
	public void ajax_add(Invocation inv, HtmlPage page,
			@ProfileHtmlEscape @Param("name") String name,// 
			@ProfileHtmlEscape @Param("number") String number,//
			@ProfileHtmlEscape @Param("email") String email,//
			@ProfileHtmlEscape @Param("jobtitle") String jobTitle,//
			@Param("department") int departmentId,//
			@Param("qq") long qq,//
			@ProfileHtmlEscape @Param("hobby") String hobby,//
			@ProfileHtmlEscape @Param("bossemail") String bossEmail,//
			@Param("roles") Set<Integer> roles//
	) {//

		$: { // 检测信息，不为空就执行插入数据操作
			try {

				FormValidator fv = page.formValidator();
				fv.notBlank(name, "name", "名字不能为空");
				fv.notBlank(number, "number", "工号不能为空");
				fv.notBlank(email, "email", "邮箱不能为空").rangeLength(email, 1, 50,
						"email", "Email只能由{1}到{2}个字符");
				fv.notBlank(jobTitle, "jobtitle", "职称不能为空");
				fv.notBlank(bossEmail, "bossemail", "bossemail不能为空");

				User boss = userService.queryByEmail(bossEmail);

				if (boss == null) {
					fv.error("bossemail", "上司邮箱错误");
					break $;
				}

				if (fv.isFailed()) {
					break $;
				}

				User user = new User();
				user.setName(name);
				user.setDepartmentId(departmentId);
				user.setJobTitle(jobTitle);
				user.setEmail(email);
				user.setManagerId(boss.getId());

				UserProfile profile = new UserProfile();
				profile.setQq(qq);
				profile.setHobby(hobby);

				int userId = userService.save(user);
				profile.setUserId(userId);
				userProfileDAO.save(profile);

				// 权限
				userRoleService.updateRolesOfUser(userId, roles);
				renderRolesOfUser(inv, null);
				//重定向
				page.redirect("/admin/user");
			} catch (Exception e) {
				LOG.error(e, "addUser");
				break $;
			}
			
		}
	}

	/**
	 * 编辑员工信息
	 * 
	 * @param inv
	 * @return
	 * @return
	 */
	@Get("/edit/{userId:\\d+}")
	public String post_edit(Invocation inv, PageMessages pageMsg,
			@Param("userId") int userId) {
		try {
			User user = userService.query(userId);
			if (user == null) {
				return "e:404";
			}
			inv.addModel("user", user);
			// boss信息
			User boss = userService.query(user.getManagerId());
			if (boss != null) {
				// 有可能为空
				inv.addModel("boss", boss);
			}

			UserProfile profile = userProfileDAO.query(userId);
			if (profile != null) {
				inv.addModel("userProfile", profile);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			pageMsg.addError(e.getMessage());
		}
		renderRolesOfUser(inv, userId);
		return "user_edit";
	}

	@Post("/edit/{userId:\\d+}")
	@AjaxJson
	public void post_edit(Invocation inv, HtmlPage page,
			@Param("userId") int userId, @Param("name") String name,//
			@Param("number") String number,//
			@Param("email") String email,//
			@Param("jobtitle") String jobTitle,//
			@Param("department") int departmentId,//
			@Param("qq") long qq,//
			@Param("hobby") String hobby,//
			@Param("bossemail") String bossEmail,//
			@Param("roles") Set<Integer> roles//
	) {//
		$: try {
			User user = userService.query(userId);
			if (user == null) {
				page.expired();
				break $;
			}
			FormValidator fv = page.formValidator();
			fv.notBlank(name, "name", "名字不能为空");
			fv.notBlank(number, "number", "工号不能为空");
			fv.notBlank(email, "email", "邮箱不能为空").rangeLength(email, 1, 50,
					"email", "Email只能由{1}到{2}个字符");
			fv.notBlank(jobTitle, "jobtitle", "职称不能为空");
			fv.notBlank(bossEmail, "bossemail", "bossemail不能为空");

			UserProfile profile = userProfileDAO.query(userId);
			if (profile == null) {
				page.expired();
				break $;
			}

			// 检测上司邮箱是否正确,不正确直接返回
			User boss = userService.queryByEmail(bossEmail);
			if (boss == null) {
				fv.error("bossemail", "上司邮箱错误");
			}

			if (fv.isFailed()) {
				break $;
			}

			user.setDepartmentId(departmentId);
			user.setName(name);
			user.setJobTitle(jobTitle);
			user.setEmail(email);
			user.setNumber(number);
			user.setManagerId(boss.getId());

			profile.setQq(qq);
			profile.setHobby(hobby);

			userService.update(user);
			userProfileDAO.update(profile);

			// 角色
			userRoleService.updateRolesOfUser(userId, roles);

			page.redirect("/admin/user");
		} catch (Exception e) {
			LOG.error(e, "userId", userId);
			page.error("服务端发生错误");
		}
	}

	private void renderRolesOfUser(Invocation inv, Integer userId) {
		inv.addModel("all_roles", roleDAO.queryAll());
		if (userId != null) {
			inv.addModel("role_set", userRoleDAO.queryRoleIdSetByUser(userId));
		}
	}

}
