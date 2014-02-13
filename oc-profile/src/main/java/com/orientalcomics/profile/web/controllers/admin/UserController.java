package com.orientalcomics.profile.web.controllers.admin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.relation.Role;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.rest.Get;
import net.paoding.rose.web.annotation.rest.Post;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.orientalcomics.profile.biz.dao.RoleDAO;
import com.orientalcomics.profile.biz.dao.UserProfileDAO;
import com.orientalcomics.profile.biz.dao.UserRoleDAO;
import com.orientalcomics.profile.biz.logic.BusinessTagService;
import com.orientalcomics.profile.biz.logic.DepartmentService;
import com.orientalcomics.profile.biz.logic.ProfileConfigs.PageSizeConfigView;
import com.orientalcomics.profile.biz.logic.ProfileSecurityService;
import com.orientalcomics.profile.biz.logic.UserRoleService;
import com.orientalcomics.profile.biz.logic.UserService;
import com.orientalcomics.profile.biz.model.User;
import com.orientalcomics.profile.biz.model.UserProfile;
import com.orientalcomics.profile.constants.ProfileAction;
import com.orientalcomics.profile.core.base.FormValidator;
import com.orientalcomics.profile.core.base.HtmlPage;
import com.orientalcomics.profile.util.common.Collections0;
import com.orientalcomics.profile.web.annotations.Ajaxable;
import com.orientalcomics.profile.web.annotations.ProfileHtmlEscape;
import com.orientalcomics.profile.web.annotations.ProfileSecurity;
import com.orientalcomics.profile.web.constants.AjaxType;
import com.orientalcomics.profile.web.controllers.internal.LoginRequiredController;
import com.orientalcomics.profile.web.interceptors.message.PageMessages;

/**
 * 员工管理
 * 
 * @author hao.zhang
 */
@ProfileSecurity(ProfileAction.USER_MANAGE)
public class UserController extends LoginRequiredController {

    @Autowired
    private UserService            userService;

    @Autowired
    private UserProfileDAO         userProfileDAO;
    
    @Autowired
    private RoleDAO                roleDAO;

    @Autowired
    private UserRoleDAO            userRoleDAO;

    @Autowired
    private UserRoleService        userRoleService;

    @Autowired
    private ProfileSecurityService profileSecurityService;

    @Autowired
    DepartmentService              departmentService;
//
//    @Autowired
//    SearchService searchService;

    @Autowired
    BusinessTagService businessTagService;

    /**
     * 获取员工列表
     * 
     * @param inv
     * @return
     */
    @Get({ "", "/list" })
    public String list(Invocation inv, PageMessages pageMessages, @Param("ul_keyword") String keyword, @Param("curpage") int curPage) {
        List<User> userList;
        String escapedKeyword = StringUtils.trimToNull(keyword);
        int pageSize = getPageSize(PageSizeConfigView.ADMIN_USER);
        int total = userService.countValidUsers();
        curPage = checkAndReturnPage(curPage, total, pageSize);
        userList = userService.queryValidUsers(curPage * pageSize, pageSize);
        
        inv.addModel("total", total);
        inv.addModel("pagesize", pageSize);
        inv.addModel("curpage", curPage);
        
//        if (escapedKeyword == null) {// 未输入关键词
//        } else {
//            SearchRequest request = new SearchRequest();
//            request.pageno = Math.max(0, curPage);
//            request.pagesize = pageSize;
//
//            SearchResult<User> searchResult = null;
//            try {
//                searchResult = searchService.searchUser(escapedKeyword, request);
//            } catch (Exception e) {
//                pageMessages.addError("搜索失败");
//            }
//            if (searchResult == null) {
//                searchResult = new SearchResult<User>();
//            }
//            userList = searchResult.data;
//            inv.addModel("total", searchResult.total);
//            inv.addModel("curpage", searchResult.pageno);
//            inv.addModel("pagesize", searchResult.pagesize);
//        }

        Collection<Integer> userIds = userList == null ? null : Collections2.transform(userList, new Function<User, Integer>() {

            @Override
            public Integer apply(User user) {
                return user == null ? null : user.getId();
            }
        });
        Map<Integer, UserProfile> userPorfileList = userIds == null ? null : userProfileDAO.query(userIds);
        if (userList != null) {
            inv.addModel("userList", userList);
        }
        if (userPorfileList != null) {
            inv.addModel("userProfileList", userPorfileList);
        }
        departmentService.wrap(userList);
        return "user_list";
    }

    /**
     * 搜索员工
     *
     * @param inv
     * @return
     */
    @Get("/search/{id:\\d+}")
    public String list(Invocation inv, @Param("id") int id) {
    	
    	User user = userService.query(id);
        if (user != null) {
        	
        	List<User> userList = Lists.newArrayList();
        	userList.add(user);
        	
            inv.addModel("userList", userList);
            
            departmentService.wrap(userList);
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
                            @Param("userId") int userId, @Param("curpage") int curpage) {
        try {
            User user = userService.query(userId);
            if (user != null) {
                user.setStatus(1);
                userService.update(user);
            }
        } catch (Exception e) {
            LOG.error("删除员工失败  ", e.getMessage());
        }
        return "r:/admin/user?curpage=" + curpage;
    }

    /**
     * 添加员工
     * 
     * @param inv
     * @return
     */

    @Get("/add")
    public String in_add(Invocation inv) {

        boolean isRoot = profileSecurityService.isRoot(currentUserId());
        if (isRoot) {
            inv.addModel("isRootUser", true);
        }
        renderRolesOfUser(inv, null);
        return "user_add";
    }

    @Post({ "/add" })
    @Ajaxable(AjaxType.JSON)
    public void ajax_add(Invocation inv, HtmlPage page,
            @ProfileHtmlEscape @Param("name") String name,//
            @ProfileHtmlEscape @Param("number") String number,//
            @ProfileHtmlEscape @Param("email") String email,//
            @ProfileHtmlEscape @Param("jobtitle") String jobTitle,//
            @Param("department") int departmentId,//
            @Param("qq") long qq,//
            @Param("level") String level,//
            @ProfileHtmlEscape @Param("hobby") String hobby,//
            @ProfileHtmlEscape @Param("bossemail") String bossEmail,//
            @Param("roles") Set<Integer> roles//
    ) {//

        $:
        { // 检测信息，不为空就执行插入数据操作
            try {

                FormValidator fv = page.formValidator();
                fv.notBlank(name, "name", "名字不能为空");
                //fv.notBlank(number, "number", "工号不能为空");
                fv.notBlank(email, "email", "邮箱不能为空").rangeLength(email, 1, 50,
                        "email", "Email只能由{1}到{2}个字符");
                fv.notBlank(jobTitle, "jobtitle", "岗位不能为空");
                fv.notBlank(level, "level", "级别不能为空");
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
                user.setLevel(level);
                user.setNumber(number);

                UserProfile profile = new UserProfile();
                profile.setQq(qq);
                profile.setHobby(hobby);

                int userId = userService.save(user);
                if(userId <=0){
                    page.error("插入数据库失败");
                    break $;
                }
                profile.setUserId(userId);
                userProfileDAO.save(profile);

                // 权限
                userRoleService.updateRolesOfUser(userId, roles);
                renderRolesOfUser(inv, null);
                // 重定向
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
        }
        /*
         * 是否是超管，可以赋予权限
         */
        boolean isRoot = profileSecurityService.isRoot(currentUserId());
        if (isRoot) {
            inv.addModel("isRootUser", true);
        }
        renderRolesOfUser(inv, userId);
        return "user_edit";
    }

    @Post("/edit/{userId:\\d+}")
    @Ajaxable(AjaxType.JSON)
    public void post_edit(Invocation inv, HtmlPage page,
            @Param("userId") int userId, @Param("name") String name,//
            @Param("number") String number,//
            @Param("email") String email,//
            @Param("jobtitle") String jobTitle,//
            @Param("level") String level,
            @Param("department") int departmentId,//
            @Param("qq") long qq,//
            @Param("hobby") String hobby,//
            @Param("bossemail") String bossEmail,//
            @Param("roles") Set<Integer> roles//
    ) {//
        $:
        try {
            User user = userService.query(userId);
            if (user == null) {
                page.expired();
                break $;
            }
            FormValidator fv = page.formValidator();
            fv.notBlank(name, "name", "名字不能为空");
           // fv.notBlank(number, "number", "工号不能为空");
            fv.notBlank(email, "email", "邮箱不能为空").rangeLength(email, 1, 50,
                    "email", "Email只能由{1}到{2}个字符");
            fv.notBlank(jobTitle, "jobtitle", "岗位不能为空");
            fv.notBlank(level, "level", "级别不能为空");
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

            if(user.getId() == boss.getId()){
                fv.error("bossemail", "上司不能是自己哇");
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
            user.setLevel(level);

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


		List<Role> roleList = roleDAO.queryAll();
		if (Collections0.isEmpty(roleList))
			return;
		else
			inv.addModel("unchecked", roleList);
		
		if (userId != null) {
			Set<Integer> roleIds = userRoleDAO.queryRoleIdSetByUser(userId);
			if (roleIds == null || roleIds.isEmpty()) {
				inv.addModel("unchecked", roleList);
				return;
    	}
    	
    	List<Role> checked   =  new ArrayList<Role>(4);
    	List<Role> unchecked =  new ArrayList<Role>(4);
    	int i = 0;
    	for(Role role : roleList) {
    		
    	   i = 0;
    	   for(Integer id : roleIds) {
    		   if(id.intValue() == role.getId()){
    		   	   checked.add(role);
    		   	   break;
    		   }
    		   i++;
    	   }
    	   if(i == roleIds.size())
    		  unchecked.add(role);
    	   
    	}
			inv.addModel("unchecked", unchecked);
			inv.addModel("checked", checked);
		}
	
    }


}
