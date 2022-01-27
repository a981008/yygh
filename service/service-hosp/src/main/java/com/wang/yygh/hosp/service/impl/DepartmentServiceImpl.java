package com.wang.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.wang.yygh.hosp.repository.DepartmentRepository;
import com.wang.yygh.hosp.service.DepartmentService;
import com.wang.yygh.model.hosp.Department;
import com.wang.yygh.vo.hosp.DepartmentQueryVo;
import com.wang.yygh.vo.hosp.DepartmentVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        if (ObjectUtils.isNotNull(department)) {
            department.setUpdateTime(new Date());
            department.setIsDeleted(1);
            departmentRepository.save(department);
        }
    }

    @Override
    public List<DepartmentVo> findDeptTree(String hoscode) {
        List<DepartmentVo> res = new ArrayList<>();

        Department department = new Department();
        department.setHoscode(hoscode);
        department.setIsDeleted(0);
        Example<Department> example = Example.of(department);
        List<Department> all = departmentRepository.findAll(example);

        // 按照 bigcode 分组
        Map<String, List<Department>> deptMap = all.stream().collect(Collectors.groupingBy(Department::getBigcode));

        for (Map.Entry<String, List<Department>> entry : deptMap.entrySet()) {
            String bigcode = entry.getKey();
            List<Department> depts = entry.getValue();
            DepartmentVo bigVo = new DepartmentVo();
            bigVo.setDepcode(bigcode);
            bigVo.setDepname(depts.get(0).getBigname());
            List<DepartmentVo> children = new ArrayList<>();
            for (Department dept : depts) {
                DepartmentVo childVo = new DepartmentVo();
                BeanUtils.copyProperties(dept, childVo);
                children.add(childVo);
            }
            bigVo.setChildren(children);
            res.add(bigVo);
        }

        return res;
    }

    @Override
    public String getDepname(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if (ObjectUtils.isNotNull(department)) {
            return department.getDepname();
        }
        return null;
    }
}
