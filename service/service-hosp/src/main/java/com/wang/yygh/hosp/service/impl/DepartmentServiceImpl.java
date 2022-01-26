package com.wang.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.wang.yygh.hosp.repository.DepartmentRepository;
import com.wang.yygh.hosp.service.DepartmentService;
import com.wang.yygh.model.hosp.Department;
import com.wang.yygh.vo.hosp.DepartmentQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * @author Wang
 * @since 2022/1/27
 */

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    DepartmentRepository departmentRepository;

    @Override
    public void save(Department department) {
        String hoscode = department.getHoscode();
        String depcode = department.getDepcode();
        Department oldDepartment = departmentRepository
                .getDepartmentByHoscodeAndDepcode(hoscode, depcode);

        Date date = new Date();
        department.setUpdateTime(date);
        department.setIsDeleted(0);
        if (ObjectUtils.isNotNull(oldDepartment)) {
            BeanUtils.copyProperties(department, oldDepartment);
            departmentRepository.save(oldDepartment);
        } else {
            department.setCreateTime(date);
            departmentRepository.save(department);
        }
    }

    @Override
    public Page<Department> findPageDept(DepartmentQueryVo departmentqueryVo, Integer page, Integer limit) {
        PageRequest pageRequest = PageRequest.of(page - 1, limit);

        Department department = new Department();
        BeanUtils.copyProperties(departmentqueryVo, department);
        department.setIsDeleted(0);
        ExampleMatcher matching = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        Example<Department> example = Example.of(department, matching);

        return departmentRepository.findAll(example, pageRequest);
    }

    @Override
    public void removeDept(String hoscode, String depcode) {
        Department department = departmentRepository
                .getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if (ObjectUtils.isNotNull(department)){
            department.setUpdateTime(new Date());
            department.setIsDeleted(1);
            departmentRepository.save(department);
        }
    }
}
