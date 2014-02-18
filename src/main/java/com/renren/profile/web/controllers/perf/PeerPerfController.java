package com.renren.profile.web.controllers.perf;

import java.sql.Timestamp;
import java.util.Date;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.Path;
import net.paoding.rose.web.annotation.rest.Get;
import net.paoding.rose.web.annotation.rest.Post;

import org.springframework.beans.factory.annotation.Autowired;

import com.renren.profile.biz.dao.PeerPerfDAO;
import com.renren.profile.biz.dao.PeerPerfProjectDAO;
import com.renren.profile.biz.dao.PerfTimeDAO;
import com.renren.profile.biz.dao.UserPerfDAO;
import com.renren.profile.biz.logic.UserPerfService;
import com.renren.profile.biz.logic.UserService;
import com.renren.profile.biz.model.PeerPerf;
import com.renren.profile.biz.model.PeerPerfProject;
import com.renren.profile.biz.model.PerfTime;
import com.renren.profile.biz.model.User;
import com.renren.profile.biz.model.UserInvitation;
import com.renren.profile.biz.model.UserPerf;
import com.renren.profile.util.sys.status.ReportStatus;
import com.renren.profile.web.annotations.AjaxJson;
import com.renren.profile.web.annotations.ProfileHtmlEscape;
import com.renren.profile.web.base.FormValidator;
import com.renren.profile.web.base.HtmlPage;
import com.renren.profile.web.controllers.internal.LoginRequiredController;

/**
 * @author 张浩 E-mail:hao.zhang@renren-inc.com
 * @date 2012-3-15 上午09:46:18
 * 
 *       互评页面
 */
@Path("peer")
public class PeerPerfController extends LoginRequiredController {

    @Autowired
    private UserPerfDAO        userSelfPerfDAO;

    @Autowired
    private UserService        userService;

    @Autowired
    private UserPerfService    userPerfService;

    @Autowired
    private PerfTimeDAO        perfTimeDAO;

    @Autowired
    private PeerPerfDAO        peerPerfDAO;

    @Autowired
    private PeerPerfProjectDAO peerPerfProjectDAO;

    /**
     * 用户互评入口，方法已包含了对非正常进入页面的校验
     * 
     * @param inv
     * @param userSelfPerfId
     * @return
     */
    @Get("{selfperfuserid:\\d+}")
    public String Index(Invocation inv, @Param("selfperfuserid") int selfPerfUserId) {

        PerfTime perTime = perfTimeDAO.queryNewestPerfItem();
        if (perTime != null) {
            inv.addModel("perfTime", perTime);
        }

        UserPerf userSelfPerf = userSelfPerfDAO.queryByUserIdAndPerfTimeID(selfPerfUserId, perTime.getId());
        // 包装userProjectsPerf
        userPerfService.wrapPerfProjects(userSelfPerf);

        // 检查是否存在该记录，防止非正常进入
        if (userSelfPerf == null) {
            return "e:404";
        }
        if (userSelfPerf.getStatus() != UserPerf.SUBMIT) {
            return "e:404";
        }

        User selfPerfUser = userService.query(userSelfPerf.getUserId());
        if (selfPerfUser != null) {
            inv.addModel("selfPerfUser", selfPerfUser);
        }
        inv.addModel("userSelfPerf", userSelfPerf);

        // 只有经理和邀请人才可以进行互评
        boolean isManager = false;
        User peer = currentUser();

        if (selfPerfUser.getManagerId() == peer.getId()) {
            isManager = true;
        }
        else {
            // 检查是否被邀请，并且点击开始，防止非正常进入
            UserInvitation invitation = userPerfService.testIsUserInvitedForPeerPerf(userSelfPerf.getUserId(), currentUserId());
            if (invitation == null) {
                return "e:404";
            }
            if (invitation.getStatus() != 1) {
                return "e:404";
            }
            inv.addModel("invitation", invitation);
        }

        // 如果已经有互评信息，包装好对项目的评价，填充到页面中
        PeerPerf peerPerf = peerPerfDAO.queryPeerPerfByUserPerfIdAndPeerId(currentUserId(), userSelfPerf.getId());
        userPerfService.wrapPeerPerfProjects(peerPerf);
        if (peerPerf != null) {
            inv.addModel("peerPerf", peerPerf);
        }

        // 向页面填充数据
        inv.addModel("isManager", isManager ? "true" : "false");

        User currentUser = currentUser();
        inv.addModel("currentUser", currentUser);

        return "perf_peer";
    }

    /**
     * 保存互评信息
     * 
     * @param inv
     * @param page
     * @param perfTimeId
     * @param advantage
     * @param disadvantage
     * @param projectContents
     * @param selfPerfProjectIds
     * @param peerPerfProjectIds
     * @param isPromotion
     * @param managerComment
     * @param perfScore
     * @param status
     */
    @Post("save/{status:save|submit}")
    @AjaxJson
    public void savePeerPerf(Invocation inv, HtmlPage page,
            @Param("perftimeid") int perfTimeId,// 绩效时间ID
            @Param("userselfperfid") int userSelfPerfId,// 自评记录ID
            @ProfileHtmlEscape @Param("advantagecomment") String advantageComment,// 优点评价
            @ProfileHtmlEscape @Param("disadvantagecomment") String disadvantageComment,// 缺点评价
            @ProfileHtmlEscape @Param("projectcomments") String[] projectComments,// 项目评价
            @Param("selfperfprojectids") int[] selfPerfProjectIds,// 项目自评的ID
            @Param("peerperfprojectids") int[] peerPerfProjectIds,// 项目互评的ID，为0代表新纪录
            @ProfileHtmlEscape @Param("ispromotion") String isPromotion,// 是否支持Ta升职
            @ProfileHtmlEscape @Param("managercomment") String managerComment,// 经理的总体意见
            @ProfileHtmlEscape @Param("perfscore") String perfScore,// 绩效打分
            @Param("invitationid") int invitationId,// 邀请记录ID
            @Param("status") String status// 本次提交状态，save|submit
    ) {

        $: {
            FormValidator fv = page.formValidator();
            fv.notEmpty(advantageComment, "advantage", "优点评价不能为空!");
            fv.notEmpty(disadvantageComment, "disadvantage", "缺点评价不能为空!");
            fv.min(perfTimeId, 0, "perftimeid", "绩效时间ID错误");
            fv.min(userSelfPerfId, 0, "userselfperfid", "用户自评记录ID出错");
            fv.min(invitationId, 0, "invitationid", "邀请记录ID出错");
            try {
                PerfTime perfTime = perfTimeDAO.query(perfTimeId);
                if (perfTime == null) {
                    page.error("内部错误");
                    break $;
                }
                // 是否支持升职
                boolean isPromotionBoolean = true;
                if ("false".equals(isPromotion)) {
                    isPromotionBoolean = false;
                }

                // 检测项目记录长度
                if (selfPerfProjectIds.length != peerPerfProjectIds.length || peerPerfProjectIds.length != projectComments.length ||
                        selfPerfProjectIds.length != projectComments.length) {
                    page.error("请求错误,项目评价长度不匹配");
                    break $;
                }
                // 检测项目评价为空的情况
                for (int i = 0; i < projectComments.length; i++) {
                    fv.notBlank(projectComments[i], "projectcontents" + i, "项目评价不能为空");
                }
                // 检测当前PeerPerf用户是manager时，对绩效的考核评分
                boolean isManager = false;
                User peer = currentUser();
                UserPerf userSelfPerf = userSelfPerfDAO.query(userSelfPerfId);
                if (userSelfPerf == null) {
                    page.error("自评ID出错");
                    break $;
                }
                User selfPerfUser = userService.query(userSelfPerf.getUserId());
                if (selfPerfUser == null) {
                    page.error("用户信息出错");
                }
                if (selfPerfUser.getManagerId() == peer.getId()) {
                    isManager = true;
                    fv.notBlank(perfScore, "perfScore", "绩效打分不能为空");
                    if (!("S".equals(perfScore) || "A".equals(perfScore) || "B".equals(perfScore) || "C".equals(perfScore))) {
                        page.error("绩效打分不正确");
                        break $;
                    }
                }

                if (fv.isFailed()) {
                    break $;
                }

                // 保存和提交行为
                int dbAction = UserPerf.SAVE;
                if ("submit".equals(status)) {
                    dbAction = UserPerf.SUBMIT;
                }

                // 这里分为插入数据和更新数据
                // 检测有没有记录，有，先判断状态，是否更新
                PeerPerf peerPerf = peerPerfDAO.queryPeerPerfByUserPerfIdAndPeerId(currentUserId(), userSelfPerfId);
                if (peerPerf != null) {
                    // 已经有过记录，现在是更新
                    if (peerPerf.getStatus() == UserPerf.SUBMIT) {
                        page.error("本季度自评已经提交过不能修改");
                    }
                    else {
                        // 开始更新互评信息
                        peerPerf.setAdvantageComments(advantageComment);
                        peerPerf.setDisadvantageComments(disadvantageComment);
                        peerPerf.setEditTime(new Timestamp(new Date().getTime()));
                        peerPerf.setStatus(dbAction);
                        if (perfTime.getIsPromotion()) {
                            peerPerf.setIsPromotion(isPromotionBoolean);
                        }
                        if (isManager) {
                            peerPerf.setContent(managerComment);
                            peerPerf.setPerfScore(perfScore);
                            userSelfPerf.setPerfScore(perfScore);
                            userSelfPerf.setLeaderId(peer.getId());
                            userSelfPerf.setLeaderName(peer.getName());
                        }

                        Integer peerPerfId = peerPerfDAO.update(peerPerf);
                        userSelfPerfDAO.update(userSelfPerf);
                        if (peerPerfId == null) {
                            page.error("互评更新出错");
                            break $;
                        }
                        // 开始更新项目互评信息
                        for (int i = 0; i < peerPerfProjectIds.length; i++) {
                            if (peerPerfProjectIds[i] == 0) {
                                // 说明是新增加的项目互评
                                PeerPerfProject insertPeerPerfProject = new PeerPerfProject();
                                insertPeerPerfProject.setInvitationId(invitationId);
                                insertPeerPerfProject.setEditTime(new Timestamp(new Date().getTime()));
                                insertPeerPerfProject.setPerfTimeId(perfTimeId);
                                insertPeerPerfProject.setProjectPerfId(selfPerfProjectIds[i]);
                                insertPeerPerfProject.setContent(projectComments[i]);
                                insertPeerPerfProject.setPeerId(currentUserId());
                                insertPeerPerfProject.setIsManager(isManager);
                                insertPeerPerfProject.setStatus(peerPerf.getStatus() == 0 ? false : true);
                                insertPeerPerfProject.setPeerPerfId(peerPerfId);

                                Integer projectId = peerPerfProjectDAO.save(insertPeerPerfProject);
                                if (projectId == null) {
                                    page.error("插入项目评价数据出错");
                                }

                            } else {// 已有的项目记录
                                PeerPerfProject updatePeerPerfProject = peerPerfProjectDAO.query(peerPerfProjectIds[i]);
                                if (updatePeerPerfProject == null) {
                                    page.error("读取项目评价记录出错");
                                    break $;
                                }
                                updatePeerPerfProject.setEditTime(new Timestamp(new Date().getTime()));
                                updatePeerPerfProject.setContent(projectComments[i]);
                                updatePeerPerfProject.setStatus(peerPerf.getStatus() == 0 ? false : true);

                                Integer updatedPeerPerfProjectId = peerPerfProjectDAO.update(updatePeerPerfProject);
                                if (updatedPeerPerfProjectId == null) {
                                    page.error("更新项目评价数据出错," + updatedPeerPerfProjectId);
                                    break $;
                                }
                            }
                        }// for
                         // 更新互评邀请状态
                        userPerfService.updateInvitationStatus(invitationId, dbAction == UserPerf.SAVE ? ReportStatus.EDIT : ReportStatus.VIEW);
                        page.redirect("/perf");
                    }
                } else {
                    // 插入
                    // 开始插入互评数据
                    PeerPerf insertPeerPerf = new PeerPerf();
                    insertPeerPerf.setAdvantageComments(advantageComment);
                    if (isManager) {
                        insertPeerPerf.setContent(managerComment);
                        insertPeerPerf.setPerfScore(perfScore);
                        userSelfPerf.setPerfScore(perfScore);
                    }
                    insertPeerPerf.setDisadvantageComments(disadvantageComment);
                    insertPeerPerf.setEditTime(new Timestamp(new Date().getTime()));
                    insertPeerPerf.setInvitationId(invitationId);
                    insertPeerPerf.setIsLeader(isManager);
                    insertPeerPerf.setIsPromotion(isPromotionBoolean);
                    insertPeerPerf.setPeerId(currentUserId());
                    insertPeerPerf.setPerfTimeId(perfTimeId);
                    insertPeerPerf.setStatus(dbAction);
                    insertPeerPerf.setUserPerfId(userSelfPerfId);

                    if (perfTime.getIsPromotion()) {
                        insertPeerPerf.setIsPromotion(isPromotionBoolean);
                    }
                    Integer peerPerfId = peerPerfDAO.save(insertPeerPerf);
                    userSelfPerfDAO.update(userSelfPerf);
                    if (peerPerfId == null) {
                        page.error("DB出错");
                        break $;
                    }
                    // 开始插入项目互评
                    for (int i = 0; i < peerPerfProjectIds.length; i++) {
                        PeerPerfProject peerPerfProject = new PeerPerfProject();
                        peerPerfProject.setContent(projectComments[i]);
                        peerPerfProject.setEditTime(new Timestamp(new Date().getTime()));
                        peerPerfProject.setInvitationId(invitationId);
                        peerPerfProject.setIsManager(isManager);
                        peerPerfProject.setPeerId(currentUserId());
                        peerPerfProject.setPeerPerfId(peerPerfId);
                        peerPerfProject.setPerfTimeId(perfTimeId);
                        peerPerfProject.setProjectPerfId(userSelfPerfId);
                        peerPerfProject.setStatus(insertPeerPerf.getStatus() == 0 ? false : true);

                        Integer projectId = peerPerfProjectDAO.save(peerPerfProject);
                        if (projectId == null) {
                            page.error("插入项目评价数据出错");
                            break $;
                        }
                    }
                    // 更新互评邀请状态
                    userPerfService.updateInvitationStatus(invitationId, dbAction == UserPerf.SAVE ? ReportStatus.EDIT : ReportStatus.VIEW);
                    page.info("保存成功");
                }

            } catch (Exception e) {
                page.error("内部错误");
                break $;
            }
        }

    }
}
