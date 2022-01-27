package com.wang.yygh.hosp.controller;

import com.wang.yygh.common.util.Result;
import com.wang.yygh.hosp.service.HospitalService;
import com.wang.yygh.model.hosp.Hospital;
import com.wang.yygh.vo.hosp.HospitalQueryVo;
import com.wang.yygh.vo.hosp.HospitalSetQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * @author Wang
 * @since 2022/1/27
 */

@Api(tags = "医院API接口")
@RestController
@RequestMapping("/admin/hosp/hospital")
public class HospitalController {
    @Autowired
    private HospitalService hospitalService;

    @ApiOperation("医院列表(条件查询、分页)")
    @GetMapping("list/{page}/{limit}")
    public Result listHosp(@PathVariable Integer page, @PathVariable Integer limit,
                           HospitalQueryVo hospitalQueryVo) {

        Page<Hospital> hospPage = hospitalService.getHospPage(page, limit, hospitalQueryVo);

        return Result.ok(hospPage);
    }
    @ApiOperation("改变医院状态")
    @GetMapping("updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable String id,
                               @ApiParam(name = "status", value = "状态（0：未上线 1：已上线）", required = true)
                               @PathVariable Integer status){
        hospitalService.updateStatus(id, status);
        return Result.ok();
    }

    @ApiOperation("医院详情")
    @GetMapping("/showHospDetail/{id}")
    public Result showHospDetail(@PathVariable String id){
        return Result.ok(hospitalService.showHospDetail(id));
    }
}
