package com.renren.profile.web.controllers;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.rest.Get;
import net.paoding.rose.web.annotation.rest.Post;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.renren.profile.biz.dao.ResumeDAO;
import com.renren.profile.biz.logic.UserService;
import com.renren.profile.biz.model.Resume;
import com.renren.profile.biz.model.User;
import com.renren.profile.util.DateTimeUtil;
import com.renren.profile.web.annotations.AjaxJson;
import com.renren.profile.web.annotations.ProfileHtmlCorrect;
import com.renren.profile.web.base.FormValidator;
import com.renren.profile.web.base.HtmlPage;
import com.renren.profile.web.controllers.internal.LoginRequiredController;

public class ResumeController extends LoginRequiredController{
	
	
	@Autowired
	private ResumeDAO resumeDAO;
	
	@Autowired
	private UserService userService;

	/**
	 * 入口，为左侧菜单做链接
	 * @param inv
	 * @param id
	 * @return
	 */
    @Get("")
    @Post("")
    public String Index(Invocation inv,@Param("targetId") int id) {
    	
		if(currentUserId() == id || id == 0)
			return "r:/resume/my/view";
		else
			return "r:/resume/" + id + "/view";
    } 
	
	/**
	 * 查看别人简历，如果是自己跳到个人页面
	 * @param inv
	 * @param owner
	 * @return
	 */
    @Get("{owner:\\d+}/view")
    public String viewResume(Invocation inv,@Param("owner") int owner) {
    	try {
			if (owner == currentUserId())
				return "r:/resume/my/view";
			Resume resume = resumeDAO.query(owner);
			if(resume == null)
				return "e:404";
			inv.addModel("resume", resume);
		} catch (Exception e) {
			LOG.error(e, e.getMessage());
		}
        return "resume_view";
    } 
    
    /**
     * 查看自己简历
     * @param inv
     * @param owner
     * @return
     */
    @Get("my/view")
    public String viewMyResume(Invocation inv) {
    	
    	Resume resume = resumeDAO.query(currentUserId());
    	User user = userService.query(currentUserId());
    	if(resume != null)
    		inv.addModel("resume",resume);
    	if(user != null)
    		inv.addModel("user", user);
    	inv.addModel("currentQTitle", DateTimeUtil.getCurrentSeasonString() + "KPI");
        return "resume_view";
    }

    
    /**
     * 编辑Resume
     * @param inv
     * @param kpiId
     * @return
     */
    @Post("edit")
    public String editResume(Invocation inv){
    	try{
    		Resume resume = resumeDAO.query(currentUserId());
    		if(resume != null){
    			/*
    			 * 有可能用户没有写简历，需要编辑
    			 */
    			if(resume.getUserId() != currentUserId())//是当前用户才能编辑
    				return "e:404";
    		}
    		User user = userService.query(currentUserId());
        	if(user != null)
        		inv.addModel("user", user);
        	
    		inv.addModel("resume", resume);
    	}catch (Exception e) {
    		return "e:404";
    	}
    	return "resume_edit";
    }
    
    /**
     * 保存修改的简历
     * @param inv
     * @param content
     * @return
     */
    @Post("save")
    @AjaxJson
    public void saveResume(Invocation inv,HtmlPage page,@ProfileHtmlCorrect @Param("content") String content){
    	
    	$:try {
    		FormValidator fv = page.formValidator();
    		content = StringUtils.trimToNull(content);
    		fv.notEmpty(content, "content", "需要填写简历内容");
            if (fv.isFailed()) {
                break $;
            }
    		
    		/*
    		 * insert or update
    		 */
    		Resume updateModel = resumeDAO.query(currentUserId());
			if (updateModel != null) {//update
				updateModel.setContent(content);
				resumeDAO.update(updateModel);
			} else {
				//insert
				Resume newModel = new Resume();
				newModel.setContent(content);
				newModel.setUserId(currentUserId());
				
				Integer savedResumeId = resumeDAO.save(newModel);
				if (savedResumeId == null){
				    Resume resume =	resumeDAO.query(currentUserId());
					if(resume == null){
						LOG.error("DB发生异常");
						page.error("DB错误");
					}
				}
			}
			page.redirect("/resume/my/view");
		} catch (Exception e) {
			LOG.error(e, e.getMessage());
			page.error("内部错误!");
		}
    	
    } 
	
}
