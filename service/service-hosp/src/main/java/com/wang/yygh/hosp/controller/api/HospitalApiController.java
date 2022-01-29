package com.wang.yygh.hosp.controller.api;

import com.wang.yygh.common.util.Result;
import com.wang.yygh.hosp.service.HospitalService;
import com.wang.yygh.model.hosp.Hospital;
import com.wang.yygh.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Wang
 * @since 2022/1/28
 */

@Api(tags = "前台API接口")
@RestController
@RequestMapping("/api/hosp/hospital")
public class HospitalApiController {
    @Autowired
    private HospitalService hospitalService;

    @ApiOperation("查询医院列表")
    @GetMapping("/findHospList/{page}/{limit}")
    public Result findHospList(@PathVariable Integer page, @PathVariable Integer limit,
                               HospitalQueryVo hospitalQueryVo) {

        return Result.ok(hospitalService.getHospPage(page, limit, hospitalQueryVo));
    }

    @ApiOperation("根据医院名称查询")
    @GetMapping("/findHosName/{hosname}")
    public Result findHosName(@PathVariable String hosname) {
        List<Hospital> res = hospitalService.findByHosname(hosname);
        return Result.ok(res);
    }
}
