package com.wang.yygh.hosp.controller;

import com.wang.yygh.common.util.Result;
import com.wang.yygh.hosp.service.ScheduleService;
import com.wang.yygh.model.hosp.Schedule;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author Wang
 * @since 2022/1/27
 */

@Api(tags = "排班API接口")
@RestController
@RequestMapping("/admin/hosp/schedule")
public class ScheduleController {
    @Autowired
    ScheduleService scheduleService;

    @ApiOperation("根据hoscode和depcode查询排班规则")
    @GetMapping("/getScheduleRule/{hoscode}/{depcode}/{page}/{limit}")
    public Result getScheduleRule(@PathVariable String hoscode, @PathVariable String depcode,
                                  @PathVariable Long page, @PathVariable Long limit) {
        Map<String, Object> map =  scheduleService.getScheduleRule(hoscode, depcode, page,limit);
        return Result.ok(map);
    }

    @ApiOperation("查询排班信息")
    @GetMapping("/getScheduleDetail/{hoscode}/{depcode}/{workDate}")
    public Result getScheduleDetail(@PathVariable String hoscode, @PathVariable String depcode, @PathVariable String workDate) {
        List<Schedule> list = scheduleService.getScheduleDetail(hoscode, depcode, workDate);
        return Result.ok(list);
    }
}
