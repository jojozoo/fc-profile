package com.orientalcomics.profile.web.controllers.admin;

import java.util.List;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.rest.Get;
import net.paoding.rose.web.annotation.rest.Post;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.orientalcomics.profile.biz.dao.RoleDAO;
import com.orientalcomics.profile.biz.dao.UserRoleDAO;
import com.orientalcomics.profile.biz.logic.DepartmentService;
import com.orientalcomics.profile.biz.logic.ProfileConfigs.PageSizeConfigView;
import com.orientalcomics.profile.biz.logic.UserService;
import com.orientalcomics.profile.biz.model.Role;
import com.orientalcomics.profile.biz.model.User;
import com.orientalcomics.profile.biz.model.UserRole;
import com.orientalcomics.profile.core.base.FormValidator;
import com.orientalcomics.profile.core.base.HtmlPage;
import com.orientalcomics.profile.util.common.JsonUtils;
import com.orientalcomics.profile.util.common.JsonUtils.JsonBuilder;
import com.orientalcomics.profile.util.time.TimeFetchUtils;
import com.orientalcomics.profile.util.time.TimeFormatUtils;
import com.orientalcomics.profile.util.time.TimeUtils;
import com.orientalcomics.profile.web.annotations.Ajaxable;
import com.orientalcomics.profile.web.annotations.ProfileHtmlEscape;
import com.orientalcomics.profile.web.annotations.ProfileRootSecurity;
import com.orientalcomics.profile.web.constants.AjaxType;
import com.orientalcomics.profile.web.controllers.internal.LoginRequiredController;

@ProfileRootSecurity
public class RolesController extends LoginRequiredController {
    @Autowired
    RoleDAO           roleDAO;

    @Autowired
    UserRoleDAO       userRoleDAO;

    @Autowired
    UserService       userService;

    @Autowired
    DepartmentService departmentService;

    @Get({ "", "list" })
    public String get_list(Invocation inv) {
        List<Role> roles = roleDAO.queryAll();
        inv.addModel("roles", roles);
        return "role_list";
    }

    @Get("create")
    public String get_create(Invocation inv) {
        return "role_create";
    }

    @Post("create")
    @Ajaxable(AjaxType.JSON)
    public synchronized void post_create(HtmlPage page, @ProfileHtmlEscape @Param("name") String name) {
        $: try {
            name = StringUtils.trimToNull(name);
            FormValidator fv = page.formValidator();
            fv.require(name, "name", "角色名称不能为空").maxLength(name,10,"name","角色名称最长为10个字符");
            if (fv.isFailed()) {
                break $;
            }

            Role role = new Role();
            role.setName(name);
            role.setEditorId(1);
            role.setEditorName("系统 ");
            role.setEditTime(TimeUtils.FetchTime.now());
            Integer roleId = roleDAO.save(role);
            LOG.info("create_user_role", "id", roleId, "name", name, "editor", role.getEditorId());
            if (roleId == null || roleId <= 0) {
                page.error("创建失败");
            } else {
                page.redirect("/admin/roles/edit/" + roleId);
            }
        } catch (Exception e) {
            LOG.error(e, "Unkown", name);
            page.error("服务端发生错误");
        }
    }

    @Get("edit/{id:\\d+}")
    public String get_edit(Invocation inv, @Param("id") int id, @Param("curpage") int curPage) {
        Role role = roleDAO.query(id);
        if (role == null) {
            return "e:404";
        }

        inv.addModel("role", role);
        renderUsers(inv, id, curPage);
        return "role_edit";
    }

    @Post("editname")
    @Ajaxable(value = AjaxType.JSON, must = true)
    public synchronized String post_edit(Invocation inv, HtmlPage page, @Param("id") int id, @ProfileHtmlEscape @Param("name") String name) {
        User editor = currentUser();
        JsonBuilder jb = JsonUtils.builder();
        $: try {
            name = StringUtils.trimToNull(name);
            if (name == null) {
                page.error("角色名称不能为空");
                break $;
            }
            Role role = roleDAO.query(id);
            if (role == null) {
                page.error("此角色不存在或者已经被删除");
                break $;
            }
            inv.addModel("role", role);

            role.setName(name);
            role.setEditorId(editor.getId());
            role.setEditorName(editor.getName());
            role.setEditTime(TimeUtils.FetchTime.now());
            roleDAO.update(role);

            LOG.info("update_user_role_name", "id", id, "name", name, "editor", editor.getId());
            if (isAjaxJson(inv)) {
                jb.put("id", id);
                jb.put("name", name);
                jb.put("time", TimeFormatUtils.dateTime(role.getEditTime()));
                page.data(jb.build());
                return null;
            } else {
                return "r:/admin/roles";
            }
        } catch (Exception e) {
            LOG.error(e, id, name, "editor", editor.getId());
            page.error("服务端发生错误");
        }
        return null;
    }

    @Post("users/add")
    @Ajaxable(value = AjaxType.JSON, must = true)
    public synchronized void post_addUser(Invocation inv, HtmlPage page, @Param("id") int id, @Param("email") String email) {
        User editor = currentUser();
        JsonBuilder jb = JsonUtils.builder();
        $: try {
            User user = userService.queryByEmail(email);
            if (user == null) {
                page.error("此用户不存在或者已经被删除");
                break $;
            }

            Role role = roleDAO.query(id);
            if (role == null) {
                page.error("此角色不存在或者已经被删除");
                break $;
            }

            if (userRoleDAO.existUserAndRole(user.getId(), id)) {
                page.error("指定用户已经被赋予了指定角色");
                break $;
            }

            UserRole userRole = new UserRole();
            userRole.setRoleId(id);
            userRole.setUserId(user.getId());
            userRole.setEditorId(editor.getId());
            userRole.setEditorName(editor.getName());
            userRole.setEditTime(TimeFetchUtils.TIMESTAMP.now());

            Integer userRoleId = userRoleDAO.save(userRole);
            if (userRoleId == null) {
                if (userRoleDAO.existUserAndRole(user.getId(), id)) {
                    page.error("指定用户已经被赋予了指定角色");
                    break $;
                } else {
                    LOG.error("add_user", "db_save_failed", "roleId", id, "userId", user.getId(), "editor", editor.getId());
                    page.error("服务端错误");
                    break $;
                }
            }

            LOG.info("add_user", "roleId", id, "userId", user.getId(), "editor", editor.getId());
            departmentService.wrap(user);
            jb.put("id", id);
            jb.put("user", userService.toJSON(user));
            jb.put("time", TimeFormatUtils.dateTime(role.getEditTime()));
            page.data(jb.build());
        } catch (Exception e) {
            LOG.error(e, id, email, "editor", editor.getId());
            page.error("服务端发生错误");
        }
    }

    @Post("users/delete")
    @Ajaxable(value = AjaxType.JSON, must = true)
    public synchronized void post_DeleteUser(Invocation inv, HtmlPage page, @Param("id") int id, @Param("user") int userId) {
        User editor = currentUser();
        $: try {
            User user = userService.query(userId);
            if (user == null) {
                break $;
            }

            Role role = roleDAO.query(id);
            if (role == null) {
                page.error("此角色不存在或者已经被删除");
                break $;
            }

            if (!userRoleDAO.existUserAndRole(user.getId(), id)) {
                break $;
            }

            UserRole userRole = new UserRole();
            userRole.setRoleId(id);
            userRole.setUserId(user.getId());
            userRole.setEditorId(editor.getId());
            userRole.setEditorName(editor.getName());
            userRole.setEditTime(TimeFetchUtils.TIMESTAMP.now());

            userRoleDAO.deleteByUserAndRole(userId, id);
            LOG.info("delete_user", "roleId", id, "userId", userId, "editor", editor.getId());
            departmentService.wrap(user);
        } catch (Exception e) {
            LOG.error(e, id, userId, "editor", editor.getId());
            page.error("服务端发生错误");
        }
    }

    @Post("delete/{id:\\d+}")
    public String post_delete(HtmlPage page, @Param("id") int id) {
        User editor = currentUser();
        $: try {
            roleDAO.delete(id);
            LOG.info("delete_user_role", "id", id, "editor", editor.getId());
            return "r:/admin/roles";
        } catch (Exception e) {
            LOG.error(e, id);
            page.error("服务端发生错误");
        }
        return "role_edit";
    }

    private void renderUsers(Invocation inv, int roleId, int curPage) {
        if (roleId > 0) {
            int total = userRoleDAO.countByRole(roleId);
            int pageSize = getPageSize(PageSizeConfigView.ROLE_USER);
            curPage = checkAndReturnPage(curPage, total, pageSize);

            List<Integer> userIds = userRoleDAO.queryUserIdListByRole(roleId,pageSize*curPage,pageSize);
            List<User> users = userService.queryAllList(userIds);
            departmentService.wrap(users);
            inv.addModel("total", total);
            inv.addModel("curpage", curPage);
            inv.addModel("pagesize", pageSize);
            inv.addModel("user_list", users);
        }
    }
}
