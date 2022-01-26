package com.wang.yygh.hosp.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wang.yygh.common.exception.YyghException;
import com.wang.yygh.common.util.MD5;
import com.wang.yygh.common.util.Result;
import com.wang.yygh.common.util.ResultCodeEnum;
import com.wang.yygh.hosp.service.DepartmentService;
import com.wang.yygh.hosp.service.HospitalService;
import com.wang.yygh.hosp.service.HospitalSetService;
import com.wang.yygh.hosp.service.ScheduleService;
import com.wang.yygh.model.hosp.Department;
import com.wang.yygh.model.hosp.Hospital;
import com.wang.yygh.model.hosp.Schedule;
import com.wang.yygh.vo.hosp.DepartmentQueryVo;
import com.wang.yygh.vo.hosp.ScheduleQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.wang.yygh.common.helper.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author Wang
 * @since 2022/1/26
 */
@Api(tags = "医院管理API接口")
@RestController
@RequestMapping("/api/hosp")
public class APIController {

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private HospitalSetService hospitalSetService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ScheduleService scheduleService;

    @ApiOperation(value = "上传医院")
    @PostMapping("saveHospital")
    public Result saveHosp(HttpServletRequest request) {
        Map<String, Object> map = HttpRequestHelper.switchMap(request.getParameterMap());
        String mapString = JSONObject.toJSONString(map);
        Hospital hospital = JSONObject.parseObject(mapString, Hospital.class);

        validSign(map); // 权限验证

        // 传来的 '+' -> ' '，所以要转回
        String logoData = (String) map.get("logoData");
        String newData = logoData.replaceAll(" ", "+");
        hospital.setLogoData(newData);

        hospitalService.save(hospital);
        return Result.ok();
    }

    @ApiOperation(value = "查询医院")
    @PostMapping("hospital/show")
    public Result getHospital(HttpServletRequest request) {
        Map<String, Object> map = HttpRequestHelper.switchMap(request.getParameterMap());

        validSign(map);

        String hoscode = (String) map.get("hoscode");
        Hospital hospital = hospitalService.getByHoscode(hoscode);

        return Result.ok(hospital);
    }

    @ApiOperation(value = "上传科室")
    @PostMapping("saveDepartment")
    public Result saveDept(HttpServletRequest request) {
        Map<String, Object> map = HttpRequestHelper.switchMap(request.getParameterMap());
        String mapString = JSONObject.toJSONString(map);
        Department department = JSONObject.parseObject(mapString, Department.class);
        validSign(map);
        departmentService.save(department);
        return Result.ok();
    }

    @ApiOperation(value = "查询科室")
    @PostMapping("department/list")
    public Result getDept(HttpServletRequest request) {
        Map<String, Object> map = HttpRequestHelper.switchMap(request.getParameterMap());
        validSign(map);
        String hoscode = (String) map.get("hoscode");
        String pageStr = (String) map.get("page");
        String limitStr = (String) map.get("limit");
        Integer page = StringUtils.isEmpty(pageStr) ? 1 : Integer.parseInt(pageStr);
        Integer limit = StringUtils.isEmpty(limitStr) ? 1 : Integer.parseInt(limitStr);
        DepartmentQueryVo departmentqueryVo = new DepartmentQueryVo();
        departmentqueryVo.setHoscode(hoscode);
        Page<Department> departments = departmentService.findPageDept(departmentqueryVo, page, limit);

        return Result.ok(departments);
    }

    @ApiOperation(value = "删除科室")
    @PostMapping("department/remove")
    public Result removeDept(HttpServletRequest request) {
        Map<String, Object> map = HttpRequestHelper.switchMap(request.getParameterMap());
        validSign(map);
        String hoscode = (String) map.get("hoscode");
        String depcode = (String) map.get("depcode");
        departmentService.removeDept(hoscode, depcode);

        return Result.ok();
    }

    @ApiOperation(value = "上传排班")
    @PostMapping("saveSchedule")
    public Result saveSchedule(HttpServletRequest request) {
        Map<String, Object> map = HttpRequestHelper.switchMap(request.getParameterMap());
        String mapString = JSONObject.toJSONString(map);
        Schedule schedule = JSONObject.parseObject(mapString, Schedule.class);
        validSign(map);
        scheduleService.save(schedule);
        return Result.ok();
    }

    @ApiOperation(value = "查询排班")
    @PostMapping("schedule/list")
    public Result getSchedule(HttpServletRequest request) {
        Map<String, Object> map = HttpRequestHelper.switchMap(request.getParameterMap());
        validSign(map);
        String hoscode = (String) map.get("hoscode");
        String depcode = (String) map.get("depcode");
        String pageStr = (String) map.get("page");
        String limitStr = (String) map.get("limit");
        Integer page = StringUtils.isEmpty(pageStr) ? 1 : Integer.parseInt(pageStr);
        Integer limit = StringUtils.isEmpty(limitStr) ? 1 : Integer.parseInt(limitStr);

        ScheduleQueryVo scheduleQueryVo = new ScheduleQueryVo();
        scheduleQueryVo.setHoscode(hoscode);
        scheduleQueryVo.setDepcode(depcode);
        Page<Schedule> schedules = scheduleService.findPageSchedule(scheduleQueryVo, page, limit);

        return Result.ok(schedules);
    }

    @ApiOperation(value = "删除排班")
    @PostMapping("schedule/remove")
    public Result removeSchedule(HttpServletRequest request) {
        Map<String, Object> map = HttpRequestHelper.switchMap(request.getParameterMap());
        validSign(map);
        String hoscode = (String) map.get("hoscode");
        String hosScheduleId = (String) map.get("hosScheduleId");
        scheduleService.removeSchedule(hoscode, hosScheduleId);
        return Result.ok();
    }

    /**
     * 权限验证
     */
    private void validSign(Map<String, Object> map) {
        String hoscode = (String) map.get("hoscode");
        if (StringUtils.isEmpty(hoscode)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        // 签名比对
        String sign = (String) map.get("sign"); // 传来的签名是经过二次加密的
        String signByHoscode = hospitalSetService.getSignKey(hoscode);
        String signMd5 = MD5.encrypt(signByHoscode);
        if (!sign.equals(signMd5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
    }
}
