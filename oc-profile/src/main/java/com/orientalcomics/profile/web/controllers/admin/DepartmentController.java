package com.orientalcomics.profile.web.controllers.admin; 

import java.util.List;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.rest.Get;
import net.paoding.rose.web.annotation.rest.Post;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.orientalcomics.profile.biz.dao.DepartmentDAO;
import com.orientalcomics.profile.biz.logic.UserService;
import com.orientalcomics.profile.biz.model.Department;
import com.orientalcomics.profile.biz.model.Shadow;
import com.orientalcomics.profile.biz.model.User;
import com.orientalcomics.profile.biz.model.UserProfile;

/** 
 * @author 张浩 E-mail:zhanghao@foundercomics.com 
 * @version 创建时间：2014年2月24日 下午6:10:53 
 * 类说明 管理部门的数据
 */
public class DepartmentController {

	@Autowired
	private DepartmentDAO departmentDAO;
	
	@Autowired
	private UserService userService;
	
	/**
	 * list
	 * @param inv
	 * @return
	 */
	@Get("")
	public String index(Invocation inv){
		
		List<Department> departmentList = departmentDAO.queryAll(0,100);
		
		inv.addModel("departmentList",departmentList);
		
		
		inv.addModel("totalNum",departmentList.size());
		
		inv.addModel("pageList",0);
		
		return "department.vm";
	}
	
	
	/**
	 * 创建部门
	 * @param inv
	 * @param loginName
	 * @param loginPassword
	 * @param email
	 * @return
	 */
	@Post("")
	public String add(Invocation inv,
			@Param("departmentName") String departmentName ,//部门名
			@Param("parentDepartment") int parentDepartment,//上级id
			@Param("managerId") int managerId//负责人id
			){
		
		String trimedName = StringUtils.trimToNull(departmentName);
		if(trimedName == null){
			inv.addModel("msg", "部门名为空");
			return "department.vm";
		}
		
		Department department = departmentDAO.queryByName(trimedName);
		
		if(department != null){
			inv.addModel("msg", "部门名被占用");
			return "department.vm";
		}
		
		if(parentDepartment != 0){
			Department pdepartment = departmentDAO.query(parentDepartment);
			if(pdepartment == null){
				inv.addModel("msg", "上级部门不存在");
				return "department.vm";
			}
		}
		
		if(managerId != 0){
			User use = userService.query(managerId);
			if(use == null){
				inv.addModel("msg", "负责人不存在");
				return "department.vm";
			}
		}
		
		
		//添加部门
		department = new Department();
		department.setDepartmentName(trimedName);
		department.setEditorId(1);
		department.setManagerId(managerId);
		if(parentDepartment != 0){
			department.setParentDepartment(parentDepartment);			
		}
		
		Integer departmentId = departmentDAO.saveForAdmin(department);
		
		if	(departmentId != null && departmentId != 0){

		}else{
			inv.addModel("msg", "创建失败");
			return "department.vm";
		}
		
		List<Department> departmentList = departmentDAO.queryAll(0,100);
		
		inv.addModel("departmentList",departmentList);
		
		
		inv.addModel("totalNum",departmentList.size());
		
		inv.addModel("pageList",0);
		
		return "department.vm";
	}
}
 