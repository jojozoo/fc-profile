package com.orientalcomics.profile.biz.base;

import com.orientalcomics.profile.biz.model.Department;


public interface DepartmentAssignable {
    int departmentId();

    void department(Department department);
}
