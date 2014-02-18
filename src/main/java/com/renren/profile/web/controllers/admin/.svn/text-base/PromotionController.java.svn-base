package com.renren.profile.web.controllers.admin;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.rest.Get;
import net.paoding.rose.web.annotation.rest.Post;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.renren.profile.biz.dao.PerfTimeDAO;
import com.renren.profile.biz.dao.PromotionManageDAO;
import com.renren.profile.biz.dao.UserPerfDAO;
import com.renren.profile.biz.dao.UserProfileDAO;
import com.renren.profile.biz.logic.UserService;
import com.renren.profile.biz.model.PerfTime;
import com.renren.profile.biz.model.PromotionManage;
import com.renren.profile.biz.model.User;
import com.renren.profile.biz.model.UserPerf;
import com.renren.profile.biz.model.UserProfile;
import com.renren.profile.constants.status.PerfTimeStatus;
import com.renren.profile.web.annotations.AjaxJson;
import com.renren.profile.web.annotations.ProfileHtmlEscape;
import com.renren.profile.web.base.FormValidator;
import com.renren.profile.web.base.HtmlPage;
import com.renren.profile.web.config.ProfileConfigs.PageSizeConfigView;
import com.renren.profile.web.controllers.internal.LoginRequiredController;

/**
 * @author 张浩 E-mail:hao.zhang@renren-inc.com
 * @date 2012-3-12 下午05:30:46
 * 
 *       升职管理
 */

public class PromotionController extends LoginRequiredController {

    @Autowired
    private UserPerfDAO        userPerfDAO;

    @Autowired
    private PerfTimeDAO        perfTimeDAO;

    @Autowired
    private UserService            userService;

    @Autowired
    private UserProfileDAO     userProfileDAO;

    @Autowired
    private PromotionManageDAO promotionManageDAO;

    /**
     * 入口函数，默认为列表
     * 
     * @param inv
     * @return
     */
    @Get({ "", "list" })
    public String Index(Invocation inv, @Param("curpage") int curPage,
            @Param("perftimeid") int perfTimeId) {

        $: {
            try {
                PerfTime perfTime = null;

                if (perfTimeId == 0) {
                    // 获取当前Perf
                    perfTime = perfTimeDAO.queryNewestPerfItem();
                } else {
                    // 获取指定的Perf
                    perfTime = perfTimeDAO.query(perfTimeId);
                }
                if (perfTime.getStatus() == 2) {
                    inv.addModel("isEnd", "end");
                }
                inv.addModel("perfTime", perfTime);

                // 获取页码
                int total = userPerfDAO.countPromotion(perfTime.getId());
                int pageSize = getPageSize(PageSizeConfigView.ADMIN_PROMOTION);
                curPage = checkAndReturnPage(curPage, total, pageSize);
                inv.addModel("total", total);
                inv.addModel("pagesize", pageSize);
                inv.addModel("curpage", curPage);

                // 按页码查询申请升职的列表
                List<UserPerf> userPerfs = userPerfDAO.queryPromotionList(
                        perfTime.getId(), curPage * total, pageSize);
                if (userPerfs != null) {
                    // 列表不为空时，继续
                    // 查看员工申请是否已经被反馈
                    Collection<Integer> perfIds = Collections2.transform(
                            userPerfs, new Function<UserPerf, Integer>() {

                                @Override
                                public Integer apply(UserPerf perf) {
                                    return perf == null ? null : perf.getId();
                                }
                            });
                    Map<Integer, Integer> promotionMap = promotionManageDAO
                            .queryMapByCollection(perfIds);
                    if (promotionMap != null) {
                        inv.addModel("promotionMap", promotionMap);
                    }

                    // 查询用户信息
                    Collection<Integer> promotionIds = Collections2.transform(
                            userPerfs, new Function<UserPerf, Integer>() {

                                @Override
                                public Integer apply(UserPerf perf) {
                                    return perf == null ? null : perf
                                            .getUserId();
                                }
                            });

                    inv.addModel("userPerfs", userPerfs);

                    // 查询用户个人信息
                    Map<Integer, User> userMap = userService
                            .queryAllMap(promotionIds);
                    List<User> userList = userService.queryAllList(promotionIds);
                    Collection<Integer> userIds = Collections2.transform(
                            userList, new Function<User, Integer>() {
                                @Override
                                public Integer apply(User user) {
                                    return user == null ? null : user.getId();
                                }
                            });

                    Map<Integer, UserProfile> userPorfileList = userProfileDAO
                            .query(userIds);

                    if (userList != null) {
                        inv.addModel("userMap", userMap);
                    }
                    if (userPorfileList != null) {
                        inv.addModel("userProfileMap", userPorfileList);
                    }
                }

            } catch (Exception e) {
                LOG.error(e, "升职管理出错");
            }
        }
        return "promotion_list";
    }

    /**
     * 查看编辑promotion管理界面
     * 
     * @param inv
     * @return
     */
    @Get({ "edit/{userperfid:\\d+}", "view/{userperfid:\\d+}" })
    public String editPromotion(Invocation inv,
            @Param("userperfid") int userPerfId) {
        try {
            UserPerf userPerf = userPerfDAO.query(userPerfId);
            PromotionManage promotionManage = promotionManageDAO
                    .queryByUserPerfId(userPerfId);

            // 查看绩效评定是否结束,已结束就只能查看不能编辑
            PerfTime perfTime = perfTimeDAO.query(userPerf.getPerfTimeId());
            if (perfTime != null) {
            	inv.addModel("perfTime", perfTime);
                if (perfTime.status() == PerfTimeStatus.END) {
                    inv.addModel("perf_end", "true");
                } else {
                    inv.addModel("perf_end", "false");
                }
            }

            if (userPerf != null) {
                inv.addModel("userPerf", userPerf);
            }
            if (promotionManage != null) {
                inv.addModel("promotionManage", promotionManage);
            }
        } catch (Exception e) {
            LOG.error(e, "查看编辑升职页面出错");
        }
        return "promotion_view";
    }

    /**
     * 保存升职反馈信息
     * 
     * @param inv
     * @param page
     * @param promotionManageId
     * @param isPromotion
     * @param promotionContent
     */
    @Post({ "save/{promotionmanageid:\\d+}" })
    @AjaxJson
    public void savePromotion(Invocation inv, //
    		HtmlPage page,//
            @Param("promotionmanageid") int promotionManageId,//
            @Param("ispromotion") int isPromotion,//
            @Param("perftimeid") int perfTimeId,//
            @Param("selfperfid") int selfPerfId,//
            @ProfileHtmlEscape @Param("promotioncontent") String promotionContent) {

        $: {
            // 检查错误
            FormValidator fv = page.formValidator();
            fv.notEmpty(promotionContent, "promotioncontent", "反馈内容不能为空");
            fv.min(promotionManageId, 0, "promotionmanageid", "反馈ID出错");
            fv.range(isPromotion, PromotionManage.PROMOTION_YES, PromotionManage.PROMOTION_NO, "ispromotion", "是否支持升职出错");
            fv.min(perfTimeId,0,"perftimeid","perfTimeId wrong");
            fv.min(selfPerfId,0,"selfperfid","selfperfid wrong");
            
            if(fv.isFailed()){
            	break $;
            }
            PerfTime perfTime = perfTimeDAO.query(perfTimeId);
            if (perfTime.status() == PerfTimeStatus.END) {
                page.error("绩效已过期");
                break $;
            }
            try {
				PromotionManage promotionManage = null;
				if (promotionManageId == 0) {
					// 新的升职反馈
					promotionManage = new PromotionManage();
					promotionManage.setOperator(currentUserId());
					promotionManage.setPerfTimeId(perfTimeId);
					promotionManage.setIsPromotion(isPromotion);
					promotionManage.setPromotionContent(promotionContent);
					promotionManage.setUserPerfId(selfPerfId);
					Integer savedPromotionManageId = promotionManageDAO
							.save(promotionManage);
					if (savedPromotionManageId == null) {
						page.error("保存失败");
					} else {
						page.redirect("/promotion");
					}
				} else {// 更新的反馈
					promotionManage = promotionManageDAO
							.query(promotionManageId);
					if (promotionManage == null) {
						page.error("DB查询出错");
						break $;
					}
					promotionManage.setIsPromotion(isPromotion);
					promotionManage.setPromotionContent(promotionContent);
					promotionManage.setOperator(currentUserId());
					promotionManageDAO.update(promotionManage);
					page.redirect("/promotion");
				}

			} catch (Exception e) {
                page.error(e.getMessage());
            }
        }

    }

    /**
     * 查看历史绩效升职信息列表
     * 
     * @param inv
     * @param curPage
     * @return
     */
    @Get("{history}")
    public String viewPromotionHistory(Invocation inv,
            @Param("curpage") int curPage) {

        // 分页
        int total = perfTimeDAO.countAllYear();
        int pageSize = getPageSize(PageSizeConfigView.ADMIN_PROMOTION);
        curPage = checkAndReturnPage(curPage, total, pageSize);

        inv.addModel("total", total);
        inv.addModel("pagesize", pageSize);
        inv.addModel("curpage", curPage);

        // 查询数据
        try {
            List<Integer> years = perfTimeDAO.queryYears(curPage
                    * pageSize, pageSize);
            Map<Integer, List<PerfTime>> perfMap = new HashMap<Integer, List<PerfTime>>();
            for (Integer year : years) {
                List<PerfTime> perfList = perfTimeDAO.queryByYear(year);
                perfMap.put(year, perfList);
            }
            inv.addModel("perfMap", perfMap);
            inv.addModel("yearSet", perfMap.keySet());
        } catch (Exception e) {
        }

        return "promotion_history";
    }
}
