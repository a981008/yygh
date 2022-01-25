package com.wang.yygh.hosp.controller;

import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wang.yygh.common.result.Result;
import com.wang.yygh.hosp.service.HospitalSetService;
import com.wang.yygh.model.hosp.HospitalSet;
import com.wang.yygh.vo.hosp.HospitalSetQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;


/**
 * @author Wang
 * @since 2022/1/25
 */
@Api(tags = "医院设置管理")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {
    @Autowired
    private HospitalSetService hospitalSetService;

    @ApiOperation("根据ID删除医院设置信息")
    @DeleteMapping("/{id}")
    public Result removeHospSet(@PathVariable Long id) {
        return hospitalSetService.removeById(id) ? Result.ok() : Result.fail();
    }

    @ApiOperation("查询医院设置信息")
    @GetMapping("/{current}/{limit}")
    public Result findPageHospSet(@PathVariable Long current,
                                  @PathVariable Long limit,
                                  HospitalSetQueryVo queryVo) {

        String hosname = queryVo.getHosname();
        String hoscode = queryVo.getHoscode();

        Page<HospitalSet> page = hospitalSetService.lambdaQuery()
                .like(StringUtils.isNotEmpty(hosname), HospitalSet::getHosname, hosname)
                .eq(StringUtils.isNotEmpty(hoscode), HospitalSet::getHoscode, hoscode)
                .page(new Page<>(current, limit));

        return Result.ok(page);
    }

    @ApiOperation("添加医院设置")
    @PostMapping
    public Result insertHospSet(@RequestBody HospitalSet hospitalSet) {
        MD5 md5 = new MD5((System.currentTimeMillis() + "" + new Random().nextInt(1000)).getBytes());
        hospitalSet.setStatus(1).setSignKey(md5.toString());
        return hospitalSetService.save(hospitalSet) ? Result.ok() : Result.fail();
    }

    @ApiOperation("根据ID获取医院设置")
    @GetMapping("/{id}")
    public Result findHospSetById(@PathVariable Long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return ObjectUtils.isNotNull(hospitalSet) ? Result.ok(hospitalSet) : Result.fail();
    }

    @ApiOperation("根据ID修改医院设置")
    @PutMapping
    public Result updateHospSet(@RequestBody HospitalSet hospitalSet) {
        return hospitalSetService.updateById(hospitalSet) ? Result.ok() : Result.fail();
    }

    @ApiOperation("批量删除")
    @DeleteMapping("/batch")
    public Result batchRemoveHospSet(@RequestBody List<Long> ids) {
        return hospitalSetService.removeBatchByIds(ids) ? Result.ok() : Result.fail();
    }

    @ApiOperation("锁定和解锁")
    @PutMapping("/lock/{id}/{status}")
    public Result lockHospSet(@PathVariable Long id, @PathVariable Integer status) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        hospitalSet.setStatus(status);
        return hospitalSetService.updateById(hospitalSet) ? Result.ok() : Result.fail();
    }

    @ApiOperation("发送签名密钥")
    @GetMapping("/key/{id}")
    public Result sendKey(@PathVariable Long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        String signKey = hospitalSet.getSignKey();
        String hoscode = hospitalSet.getHoscode();
        // TODO 发送短信
        return Result.ok();
    }
}
