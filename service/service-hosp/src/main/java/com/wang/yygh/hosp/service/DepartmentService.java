package com.wang.yygh.hosp.service;

import com.wang.yygh.model.hosp.Department;
import com.wang.yygh.vo.hosp.DepartmentQueryVo;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface DepartmentService {
    void save(Department department);

    Page<Department> findPageDept(DepartmentQueryVo departmentqueryVo, Integer page, Integer limit);

    void removeDept(String hoscode, String depcode);
}
