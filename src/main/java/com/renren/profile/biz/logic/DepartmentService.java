package com.renren.profile.biz.logic;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.renren.profile.biz.base.DepartmentAssignable;
import com.renren.profile.biz.dao.DepartmentDAO;
import com.renren.profile.biz.model.Department;

@Service
public class DepartmentService {
    @Autowired
    DepartmentDAO departmentDAO;

    public Department queryByOaId(String oaId) {
        return departmentDAO.query(oaId);
    }

    public Department queryById(int departmentId) {
        return departmentDAO.query(departmentId);
    }

    public void update(Department department) {
        departmentDAO.update(department);
    }

    public int save(Department department) {
        return departmentDAO.save(department);
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
}
