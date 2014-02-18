package com.renren.profile.web.controllers;

import java.util.List;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.rest.Get;
import net.paoding.rose.web.annotation.rest.Post;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.renren.profile.biz.dao.KpiDAO;
import com.renren.profile.biz.model.Kpi;
import com.renren.profile.constants.ProfileAction;
import com.renren.profile.util.DateTimeUtil;
import com.renren.profile.web.annotations.AjaxJson;
import com.renren.profile.web.annotations.ProfileHtmlCorrect;
import com.renren.profile.web.annotations.ProfileHtmlEscape;
import com.renren.profile.web.annotations.ProfileSecurity;
import com.renren.profile.web.base.FormValidator;
import com.renren.profile.web.base.HtmlPage;
import com.renren.profile.web.config.ProfileConfigs.PageSizeConfigView;
import com.renren.profile.web.controllers.internal.LoginRequiredController;

public class KpiController extends LoginRequiredController {

	@Autowired
	private KpiDAO kpiDAO;
	
	/**
	 * 入口，为左侧菜单做链接
	 * @param inv
	 * @param id
	 * @return
	 */
	@ProfileSecurity(ProfileAction.VIEW_USER_KPI)
    @Get("")
    @Post("")
    public String Index(Invocation inv,@Param("targetId") int id) {
    	
		if(currentUserId() == id || id == 0)
			return "r:/kpi/my/view";
		else
			return "r:/kpi/" + id + "/view";
    } 
	
	/**
	 * 查看别人KPI，如果是自己跳到个人页面
	 * @param inv
	 * @param owner
	 * @return
	 */
    @ProfileSecurity(ProfileAction.VIEW_USER_KPI)
    @Get("{owner:\\d+}/view")
    public String viewKpi(Invocation inv,@Param("owner") int owner,@Param("curPage") int curPage) {
    	try {
			if (owner == currentUserId())
				return "r:/kpi/my/view";
			
			int total = kpiDAO.countUserKpi(owner);
	    	int pageSize =getPageSize(PageSizeConfigView.KPI);
			curPage = checkAndReturnPage(curPage, total, pageSize);
	    	
			inv.addModel("total", total);
			inv.addModel("pagesize", pageSize);
			inv.addModel("curpage", curPage);
			
			List<Kpi> kpiList = kpiDAO.queryUserKpiByPage(owner,curPage * pageSize,pageSize);
			if(kpiList == null)
				return "e:404";
			inv.addModel("kpiList", kpiList);
		} catch (Exception e) {
			LOG.error(e, e.getMessage());
		}
        return "kpi_other";
    } 
    
    /**
     * 查看自己KPI
     * @param inv
     * @param owner
     * @return
     */
    @Get("my/view")
    public String viewMyKpi(Invocation inv,@Param("curPage") int curPage) {
    	
    	int total = kpiDAO.countUserKpi(currentUserId());
    	int pageSize =getPageSize(PageSizeConfigView.KPI);
		curPage = checkAndReturnPage(curPage, total, pageSize);
    	
		inv.addModel("total", total);
		inv.addModel("pagesize", pageSize);
		inv.addModel("curpage", curPage);
		
    	Integer currentIntSeason = DateTimeUtil.getIntSeason();
    	inv.addModel("quarterTime", currentIntSeason);
    	
    	List<Kpi> kpiList = kpiDAO.queryUserKpiByPage(currentUserId(),curPage * pageSize,pageSize);
    	inv.addModel("kpiList",kpiList);
    	inv.addModel("currentQTitle", DateTimeUtil.getCurrentSeasonString() + "KPI");
    	
    	/*
    	 * 如果当前Q的KPI已经有记录，不显示编辑器
    	 */
    	Kpi kpiModel = kpiDAO.queryByQuarterTime(currentUserId(),currentIntSeason);
    	if(kpiModel != null){
    		inv.addModel("showEditer", "false");
    	}
        return "kpi_my";
    }

//    @Get("{owner:\\d+}/view2")
//    public String viewKpi2(@Param("owner") int owner) {
//        if (ProfileSecurityManager.hasPermission(ProfileAction.VIEW_USER_KPI, owner)) {
//            return "@这是" + owner + "的简历";
//        } else {
//            return authError();
//        }
//    }
    
    /**
     * 编辑KPI
     * @param inv
     * @param kpiId
     * @return
     */
    @Get("edit/{kpiid:\\d+}")
    @Post("edit/{kpiid:\\d+}")
    public String editKpi(Invocation inv,@Param("kpiid") int kpiId){
    	try{
    		Integer seansonInt = DateTimeUtil.getIntSeason();
    		inv.addModel("quarterTime", seansonInt);
    		Kpi kpi = kpiDAO.query(kpiId);
    		if(kpi.getUserId() != currentUserId())//是当前用户才能编辑
    			return "e:404";
    		int firstId = kpiDAO.queryFirstKpiId(currentUserId());
    		if(kpiId != firstId)
    			return "e:404";
    		inv.addModel("kpi", kpi);
    	}catch (Exception e) {
    		return "e:404";
    	}
    	return "kpi_my";
    }
    
    /**
     * 保存修改的简历
     * @param inv
     * @param content
     * @return
     */
    @SuppressWarnings("unchecked")
	@Post("save/{kpiid:\\d+}")
    @AjaxJson
    public void saveKpi(Invocation inv,
    		HtmlPage page,
    		@Param("kpiid") int kpiId,
    		@ProfileHtmlCorrect @Param("content") String content,
    		@Param("status") String status,
    		@ProfileHtmlEscape @Param("kpititle") String kpiTtile,
    		@Param("quartertime") int quarterTime){
       	
    	$:try {
    		
    		 FormValidator fv = page.formValidator();
             fv.notEmpty(content, "content", "需要填写KPI内容");
             fv.notEmpty(status, "status", "内部错误");
             if (fv.isFailed()) {
                 break $;
             }
    		/*
        	 * 保存和提交
        	 */
        	int saveStatus = Kpi.STATUS_SAVE;//save
        	if(status.equals("submit"))
        		saveStatus = Kpi.STATUS_SUBMIT;
             
    		/*
    		 * insert and update
    		 */
			if (kpiId != Kpi.INSERT_ID) {
				//update
				Kpi updateModel = kpiDAO.query(kpiId);
	            if (updateModel == null) {
	                page.expired();
	                break $;
	            }
	            
	            if(updateModel.getUserId() != currentUserId()){
	            	page.error("只允许对自己的周报进行编辑操作");
	            	break $;
	            }
	            
	            if (updateModel.getStatus() == Kpi.STATUS_SUBMIT && saveStatus == Kpi.STATUS_SAVE){
	            	page.error("当前季度KPI已经提交过，只能再次提交！");
	        		break $;
	            }
				updateModel.setContent(content);
				updateModel.setStatus(saveStatus);
				kpiDAO.update(updateModel);
				
				if(saveStatus == Kpi.STATUS_SUBMIT){
					page.redirect("/kpi/my/view");
				}
				else{
					page.info("更新成功");
				}
			} else {
				//insert
				/*
	        	 *查看当前KPI时间段是否已经有记录 ,如果已经有了就不能插入
	        	 */
				Kpi kpiModel = kpiDAO.queryByQuarterTime(currentUserId(),quarterTime);
	        	if(kpiModel != null){
	        		page.error("当前季度KPI已经有过记录，不允许重复插入！");
	        		break $;
	        	}
				
				Kpi newModel = new Kpi();
				newModel.setContent(content);
				newModel.setTitle(kpiTtile);
				newModel.setQuarterTime(quarterTime);
				newModel.setUserId(currentUserId());
				newModel.setStatus(saveStatus);
				
				Integer saveKpiId = kpiDAO.save(newModel);
				if (saveKpiId != null){
					/*
					 * 插入新数据以后，将上一份KPI置为提交，不可编辑
					 * 现在是只有最新一份才可以编辑，在edit时控制
					 */
					LOG.info("saveKpi", saveKpiId);		
					JSONObject json = new JSONObject();
					json.put("savedKpiId", Integer.valueOf(saveKpiId));
					page.data(json);
					break $;
				}
				else{
					   Kpi existsKpi = kpiDAO.queryByQuarterTime(currentUserId(), quarterTime);
		                if (existsKpi == null) {
		                    LOG.error("DB发生异常");
		                    page.error("内部错误！");
		                }
				}
			}
		} catch (Exception e) {
			LOG.error(e,"保存KPI出错", e.getMessage());
			page.error("内部错误");
		}
    } 

    }
