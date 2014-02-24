package com.orientalcomics.profile.biz.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.orientalcomics.profile.biz.base.DepartmentAssignable;
import com.orientalcomics.profile.biz.dao.DepartmentDAO;
import com.orientalcomics.profile.biz.dao.UserDAO;
import com.orientalcomics.profile.biz.logic.ProfileConfigs.IntegerConfigView;
import com.orientalcomics.profile.biz.model.Department;
import com.orientalcomics.profile.biz.model.User;
import com.orientalcomics.profile.util.common.Collections0;
import com.orientalcomics.profile.util.common.ProfileStore;


@Service
public class DepartmentService {

    @Autowired
    DepartmentDAO departmentDAO;
    
    @Autowired
    UserDAO       userDAO;
    
    private ProfileStore<Map<Integer, Department>> firstLevelDepartments = new ProfileStore<Map<Integer, Department>>("renren.profile.departmentList.cache", "部门列表") {
        @Override
        protected Map<Integer, Department> newObject() {
            Map<Integer, Department> res = null;
            Map<Integer, Department> departments = null;
            try {
                res = departmentDAO.queryAll();
                departments = wrapFirstDepartments(res);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return departments;
        }

        @Override
        protected int expireSeconds() {
            return (int) TimeUnit.MINUTES.toSeconds(10);
        }
    };

	  public void clearCache() {
	        firstLevelDepartments.reset();
	    }
	public List<Department> queryByIds(Collection<Integer> ids) {
		return departmentDAO.queryByIds(ids);
	}

    public Department queryById(int departmentId) {
        return departmentDAO.query(departmentId);
    }

    public void update(Department department) {
        departmentDAO.update(department);
        clearCache();
    }

    public Integer save(Department department) {
        Integer result = departmentDAO.save(department);
        clearCache();
        return result;
    }

    public void wrap(DepartmentAssignable input) {
        if (input == null) {
            return;
        }
        input.department(queryById(input.departmentId()));
    }

    public void wrap(Collection<? extends DepartmentAssignable> inputs) {
        if (CollectionUtils.isEmpty(inputs)) {
            return;
        }
        for (DepartmentAssignable input : inputs) {
            wrap(input);
        }
    }

    public Map<Integer, Department> getFirstDepartment() {
        return firstLevelDepartments.getObject();
    }
    
    /**
     * 根据departmentId能够查询一层部门
     * 
     * @param departmentId
     * @return
     */
    public List<Map<String,String>> getDepartmentOneBy(int departmentId) {
    	Map<Integer,Department> departmentMap = departmentDAO.queryAll();
    	Map<Integer, Department> childMap     = getChildDepartments(departmentMap, departmentId);
    	
    	Map<String,String> retuMap       = new HashMap<String,String>(3);
    	List<Map<String,String>> retList = new ArrayList<Map<String,String>>();
    	for (Department dept : childMap.values()) {
    		retuMap.put("departmentId",String.valueOf(dept.getId()));
    		retuMap.put("departmentName",dept.simpleName());
    		retList.add(retuMap);
    	}
    	
    	return retList;
    	
    }
    
    /**
     * 根据departmentId能够查询子部门的信息一共是三级
     * 
     * @param departmentId
     * @return
     */
    public Map<Integer,Department> getDepartmentBy(int departmentId) {
    	Map<Integer,Department> queryDepMap   = new HashMap<Integer,Department>();
    	Map<Integer,Department> departmentMap = departmentDAO.queryAll();
    	Map<Integer, Department> childMap     = getChildDepartments(departmentMap, departmentId);
    	if (childMap != null && childMap.size() != 0) {
    		for (Department secondDepartment : childMap.values()) {
                Map<Integer, Department> thirdLevels = getChildDepartments(departmentMap, secondDepartment.getId());
                if (thirdLevels != null && thirdLevels.size() != 0) {
                	secondDepartment.childDepartments(thirdLevels);
                }
            }
    	}
    	
    	queryDepMap.putAll(childMap);
    	
    	queryDepMap.put(Integer.valueOf(departmentId), this.queryById(departmentId));
    	
    	Map<Integer,Department> resultDepMap   = new HashMap<Integer,Department>();

    	for (Entry<Integer, Department> entry : queryDepMap.entrySet()) {
    		resultDepMap.put(entry.getKey(), entry.getValue());
    		if (entry.getValue().childDepartments() != null) {
    			for (Entry<Integer, Department> entry1 : entry.getValue().childDepartments().entrySet()) {
    				resultDepMap.put(entry1.getKey(), entry1.getValue());
    			}
    		}
    	}

    	return resultDepMap;
    }

    /**
     * 部门级别一共显示三级
     *
     * @param res
     * @return
     */
    public Map<Integer, Department> wrapFirstDepartments(Map<Integer, Department> res) {

        Map<Integer, Department> firstMap = getFirstDepartments(res);
        for (Department department : firstMap.values()) {
            Map<Integer, Department> secondLevels = getChildDepartments(res, department.getId());
            for (Department secondDepartment : secondLevels.values()) {
                Map<Integer, Department> thirdLevels = getChildDepartments(res, secondDepartment.getId());
                secondDepartment.childDepartments(thirdLevels);
            }
            department.childDepartments(secondLevels);
        }
        return firstMap;
    }

    /**
     * 找到单纯的一级部门，这里的一级部门表示，父节点为45；
     *
     * @param res
     * @return
     */
    private Map<Integer, Department> getFirstDepartments(Map<Integer, Department> res) {

        Map<Integer, Department> firstmap = new HashMap<Integer, Department>();
        for (Department department : res.values()) {
            if (department.getParentDepartment() == ProfileConfigHelper.ins().getValue(IntegerConfigView.DEPARTMENT_TREE_ROOT_ID)) {
                firstmap.put(department.getId(), department);
            }
        }
        return firstmap;
    }

    /**
     * 找到下级部门列表
     *
     * @param res
     * @return
     */

    public Map<Integer, Department> getChildDepartments(Map<Integer, Department> res, int parentId) {

        Map<Integer, Department> childs = new HashMap<Integer, Department>();
        for (Department department : res.values()) {
            if (department.getParentDepartment() == parentId) {
                childs.put(department.getId(), department);
            }
        }
        return childs;
    }
    
    public List<Department> getBaseDepartment() {
    	List<Department> lists   =  departmentDAO.getBaseDepartment();
    	List<Department> retList = new ArrayList<Department>();
    	for (Department department : lists) {
    		List<User> users =userDAO.queryByDepartment(department.getId());
    		if (Collections0.isNotEmpty(users)) {
    			retList.add(department);
    		}
    	}
    	
    	return retList;
    }
    
}
