package com.wang.yygh.hosp.controller;

import com.wang.yygh.common.util.Result;
import com.wang.yygh.hosp.service.DepartmentService;
import com.wang.yygh.vo.hosp.DepartmentVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Wang
 * @since 2022/1/27
 */

@Api(tags = "科室API接口")
@RestController
@RequestMapping("/admin/hosp/department")
public class DepartmentController {
    @Autowired
    DepartmentService departmentService;

    @ApiOperation("查询医院所有科室")
    @GetMapping("/getDeptAll/{hoscode}")
    public Result getDeptAll(@PathVariable String hoscode) {
        List<DepartmentVo> list= departmentService.findDeptTree(hoscode);
        return Result.ok(list);
    }
}
