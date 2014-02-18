package com.renren.profile.web.controllers.perf;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.Path;
import net.paoding.rose.web.annotation.rest.Get;
import net.paoding.rose.web.annotation.rest.Post;

import org.springframework.beans.factory.annotation.Autowired;

import com.renren.profile.biz.dao.PerfTimeDAO;
import com.renren.profile.biz.dao.UserPerfDAO;
import com.renren.profile.biz.dao.UserPerfProjectDAO;
import com.renren.profile.biz.logic.UserPerfService;
import com.renren.profile.biz.logic.UserService;
import com.renren.profile.biz.model.PerfTime;
import com.renren.profile.biz.model.User;
import com.renren.profile.biz.model.UserPerf;
import com.renren.profile.biz.model.UserPerfProject;
import com.renren.profile.constants.ProfilePerfProjectWeight;
import com.renren.profile.web.annotations.AjaxJson;
import com.renren.profile.web.annotations.ProfileHtmlEscape;
import com.renren.profile.web.base.FormValidator;
import com.renren.profile.web.base.HtmlPage;
import com.renren.profile.web.controllers.internal.LoginRequiredController;

/**
 * @author 张浩 E-mail:hao.zhang@renren-inc.com
 * @date 2012-3-12 上午09:58:39
 * 
 *       个人自评页面
 */

@Path("self")
public class SelfPerfController extends LoginRequiredController {

    @Autowired
    private UserService            userService;

    @Autowired
    private PerfTimeDAO        perfTimeDAO;

    @Autowired
    private UserPerfDAO        userPerfDAO;

    @Autowired
    private UserPerfProjectDAO userPerfProjectDAO;

    @Autowired
    private UserPerfService    userPerfService;

    /**
     * 入口页
     * 
     * @param inv
     * @return
     */
    @Get("")
    public String Index(Invocation inv) {

        try {
            /*
             * 只有绩效考核开始的时候才能进入
             */
            PerfTime perfTime = perfTimeDAO.queryNewestPerfItem();
            if (perfTime == null) {
                return "@绩效考核还未开始!";
            }
            // 是否包含升职，放在JSP中处理
            inv.addModel("perfTime", perfTime);

            User user = userService.query(currentUserId());
            inv.addModel("user", user);

            UserPerf userSelfPerf = userPerfService.getUserSelfPerf(
                    currentUserId(), perfTime.getId());
            if (userSelfPerf != null) {
                inv.addModel("userSelfPerf", userSelfPerf);
                List<UserPerfProject> userProjects = userSelfPerf.projects();
                if (userProjects != null)
                	if(userProjects != Collections.EMPTY_LIST){
                		inv.addModel("userProjects", userProjects);
                	}
            }

        } catch (Exception e) {
            LOG.error(e, "绩效考核入口出错");
        }

        return "perf_self";
    }

    /**
     * 保存信息
     */
    @Post("save/{status:save|submit}")
    @AjaxJson
    public void save(Invocation inv, HtmlPage page,
            @Param("perftimeid") int perfTimeId,// 绩效时间ID
            @ProfileHtmlEscape @Param("advantage") String advantage,// 优点
            @ProfileHtmlEscape @Param("disadvantage") String disadvantage,// 缺点
            @ProfileHtmlEscape @Param("projecttitles") String[] projectTitles,// 项目标题
            @ProfileHtmlEscape @Param("projectcontents") String[] projectContents,// 项目内容
            @Param("projectweights") int[] projectWeights,// 员工在项目中的权重
            @ProfileHtmlEscape @Param("projectroles") String[] projectRoles,// 项目中的角色
            @Param("projectids") int[] projectIds,// 项目的ID，为0代表新纪录
            @Param("ispromotion") String isPromotion,// 是否提交了升职申请
            @Param("promotionreason") String promotionReason,// 升职理由
            @Param("status") String status// 本次提交状态，save|submit
    ) {

        $: {
            FormValidator fv = page.formValidator();
            fv.notEmpty(advantage, "advantage", "优点不能为空!");
            fv.notEmpty(disadvantage, "disadvantage", "缺点不能为空!");
            fv.min(perfTimeId, 0, "perftimeid", "绩效时间ID错误");

            try {
                PerfTime perfTime = perfTimeDAO.query(perfTimeId);
                if (perfTime == null) {
                    page.error("内部错误");
                    break $;
                }
                // 如果申请升职，升职不能为空
                boolean isPromotionBoolean = false;
                if (perfTime.getIsPromotion()) {
                    if ("true".equals(isPromotion)) {
                        isPromotionBoolean = true;
                        fv.notEmpty(promotionReason, "promotionreason", "升职理由不能为空");
                    }
                }

                // 检测项目记录长度
                if (projectTitles.length != projectContents.length || projectTitles.length != projectRoles.length ||
                        projectContents.length != projectRoles.length) {
                    page.error("请求错误,项目记录长度不匹配");
                }
                // 检测项目描述为空的情况
                for (int i = 0; i < projectTitles.length; i++) {
                    fv.notBlank(projectTitles[i], "projecttitles" + i, "项目标题不能为空");
                    fv.notBlank(projectContents[i], "projectcontents" + i, "项目内容不能为空");
                    fv.notBlank(projectRoles[i], "projectroles" + i, "项目角色不能为空");
                    fv.require(ProfilePerfProjectWeight.findById(projectWeights[i]), "projectweights" + i, "项目权重设置不正确");
                    if (fv.isFailed()) {
                        break $;
                    }
                }

                User user = userService.query(currentUserId());
                int dbAction = UserPerf.SAVE;
                if ("submit".equals(status)) {
                    dbAction = UserPerf.SUBMIT;
                }

                // 这里分为插入数据和更新数据
                // 检测有没有记录，有，先判断状态，是否更新
                UserPerf existsUserPerf = userPerfDAO.queryByUserIdAndPerfTimeID(currentUserId(), perfTimeId);
                if (existsUserPerf != null) {
                    // 已经有过记录，现在是更新
                    if (existsUserPerf.getStatus() == UserPerf.SUBMIT) {
                        page.error("本季度自评已经提交过不能修改");
                    }
                    else {
                        // 开始更新自评信息
                        existsUserPerf.setAdvantage(advantage);
                        existsUserPerf.setDisadvantage(disadvantage);
                        existsUserPerf.setEditTime(new Timestamp(new Date().getTime()));
                        existsUserPerf.setStatus(dbAction);

                        Integer userPerfId = userPerfDAO.update(existsUserPerf);
                        if (userPerfId == null) {
                            page.error("自评更新出错");
                            break $;
                        }
                        // 开始更新项目自评信息
                        for (int i = 0; i < projectTitles.length; i++) {
                            if (projectIds[i] == 0) {
                                // 说明是新增加的项目
                                UserPerfProject insertUserProject = new UserPerfProject();
                                insertUserProject.setUserId(user.getId());
                                insertUserProject.setEditTime(new Timestamp(new Date().getTime()));
                                insertUserProject.setPerfTimeId(perfTimeId);
                                insertUserProject.setUserPerfId(userPerfId);
                                insertUserProject.setProjectContent(projectContents[i]);
                                insertUserProject.setProjectName(projectTitles[i]);
                                insertUserProject.setRole(projectRoles[i]);
                                insertUserProject.setWeight(projectWeights[i]);
                                insertUserProject.setStatus(existsUserPerf.getStatus());

                                Integer projectId = userPerfProjectDAO.save(insertUserProject);
                                if (projectId == null) {
                                    page.error("插入项目数据出错");
                                }

                            } else {// 已有的记录
                                UserPerfProject updateUserProject = userPerfProjectDAO.query(projectIds[i]);
                                if (updateUserProject == null) {
                                    page.error("读取项目记录出错");
                                    break $;
                                }
                                updateUserProject.setEditTime(new Timestamp(new Date().getTime()));
                                updateUserProject.setProjectContent(projectContents[i]);
                                updateUserProject.setProjectName(projectTitles[i]);
                                updateUserProject.setRole(projectRoles[i]);
                                updateUserProject.setWeight(projectWeights[i]);
                                updateUserProject.setStatus(existsUserPerf.getStatus());

                                Integer updatedProjectId = userPerfProjectDAO.update(updateUserProject);
                                if (updatedProjectId == null) {
                                    page.error("插入项目数据出错," + updatedProjectId);
                                    break $;
                                }
                            }
                        }//for
                       page.redirect("/perf");
                    }
                } else {
                    // 插入
                    // 开始插入自评数据
                    UserPerf userPerf = new UserPerf();
                    userPerf.setUserId(currentUserId());
                    userPerf.setPerfTimeId(perfTimeId);
                    userPerf.setUserName(user.getName());
                    userPerf.setAdvantage(advantage);
                    userPerf.setDisadvantage(disadvantage);
                    userPerf.setEditTime(new Timestamp(new Date().getTime()));
                    userPerf.setStatus(dbAction);

                    if (perfTime.getIsPromotion()) {
                        userPerf.setIsPromotion(isPromotionBoolean);
                        if (isPromotionBoolean) {
                            userPerf.setPromotionReason(promotionReason);
                        }
                    }
                    Integer userPerfId = userPerfDAO.save(userPerf);
                    if (userPerfId == null) {
                        UserPerf queryUserPerf = userPerfDAO.queryByUserIdAndPerfTimeID(user.getId(), perfTimeId);
                        if (queryUserPerf == null) {
                            page.error("DB出错");
                            break $;
                        } else {
                            page.error("数据已存在");
                            break $;
                        }
                    }
                    // 开始插入项目自评
                    for (int i = 0; i < projectTitles.length; i++) {
                        UserPerfProject userProject = new UserPerfProject();
                        userProject.setUserId(user.getId());
                        userProject.setEditTime(new Timestamp(new Date().getTime()));
                        userProject.setPerfTimeId(perfTimeId);
                        userProject.setUserPerfId(userPerfId);
                        userProject.setProjectContent(projectContents[i]);
                        userProject.setProjectName(projectTitles[i]);
                        userProject.setRole(projectRoles[i]);
                        userProject.setWeight(projectWeights[i]);
                        userProject.setStatus(userPerf.getStatus());

                        Integer projectId = userPerfProjectDAO.save(userProject);
                        if (projectId == null) {
                            page.error("插入项目数据出错");
                            break $;
                        }
                    }
                    page.info("保存成功");
                }

            } catch (Exception e) {
                page.error("内部错误");
                break $;
            }
        }
    }
}
