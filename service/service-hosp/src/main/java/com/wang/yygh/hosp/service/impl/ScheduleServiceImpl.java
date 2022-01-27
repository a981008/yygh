package com.wang.yygh.hosp.service.impl;

import cn.hutool.core.lang.hash.Hash;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.wang.yygh.hosp.repository.ScheduleRepository;
import com.wang.yygh.hosp.service.DepartmentService;
import com.wang.yygh.hosp.service.HospitalService;
import com.wang.yygh.hosp.service.ScheduleService;
import com.wang.yygh.model.hosp.Department;
import com.wang.yygh.model.hosp.Hospital;
import com.wang.yygh.model.hosp.Schedule;
import com.wang.yygh.vo.hosp.BookingScheduleRuleVo;
import com.wang.yygh.vo.hosp.ScheduleQueryVo;
import io.swagger.annotations.ApiOperation;
import lombok.experimental.Accessors;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Wang
 * @since 2022/1/27
 */

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private DepartmentService departmentService;

    @Override
    public void save(Schedule schedule) {
        String hoscode = schedule.getHoscode();
        String hosScheduleId = schedule.getHosScheduleId();
        Schedule oldSchedule = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(hoscode, hosScheduleId);
        Date date = new Date();
        schedule.setUpdateTime(date);
        schedule.setIsDeleted(0);
        if (ObjectUtils.isNotNull(oldSchedule)) {
            BeanUtils.copyProperties(schedule, oldSchedule);
            scheduleRepository.save(oldSchedule);
        } else {
            schedule.setCreateTime(date);
            scheduleRepository.save(schedule);

        }
    }

    @Override
    public Page<Schedule> findPageSchedule(ScheduleQueryVo scheduleQueryVo, Integer page, Integer limit) {
        PageRequest pageRequest = PageRequest.of(page - 1, limit);

        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleQueryVo, schedule);
        schedule.setIsDeleted(0);
        ExampleMatcher matching = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        Example<Schedule> example = Example.of(schedule, matching);

        return scheduleRepository.findAll(example, pageRequest);
    }

    @Override
    public void removeSchedule(String hoscode, String hosScheduleId) {
        Schedule schedule = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(hoscode, hosScheduleId);
        if (ObjectUtils.isNotNull(schedule)){
            schedule.setUpdateTime(new Date());
            schedule.setIsDeleted(1);
            scheduleRepository.save(schedule);
        }
    }

    @Override
    public Map<String, Object> getScheduleRule(String hoscode, String depcode, Long page, Long limit) {
        Criteria criteria = Criteria.where("hoscode").is(hoscode).and("depcode").is(depcode);
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate")
                        .first("workDate").as("workDate")
                        .count().as("docCount")
                        .sum("reservedNumber").as("reservedNumber")
                        .sum("availableNumber").as("availableNumber"),
                Aggregation.sort(Sort.Direction.DESC, "workDate"),
                Aggregation.skip((page - 1) * limit),
                Aggregation.limit(limit)
        );
        AggregationResults<BookingScheduleRuleVo> aggRes =
                mongoTemplate.aggregate(agg, Schedule.class, BookingScheduleRuleVo.class);
        List<BookingScheduleRuleVo> bsrVos = aggRes.getMappedResults();

        //分组查询的总记录数
        Aggregation totalAgg = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate")
        );
        AggregationResults<BookingScheduleRuleVo> totalAggResults =
                mongoTemplate.aggregate(totalAgg, Schedule.class, BookingScheduleRuleVo.class);
        int total = totalAggResults.getMappedResults().size();


        for (BookingScheduleRuleVo bsrVo : bsrVos) {
            Date workDate = bsrVo.getWorkDate();
            String week = this.getDayOfWeek(new DateTime(workDate));
            bsrVo.setDayOfWeek(week);
        }



        Map<String, Object> baseMap = new HashMap<>();
        baseMap.put("hosname", hospitalService.getHosnameByHoscode(hoscode));

        Map<String, Object> res = new HashMap<>();
        res.put("bookingScheduleRuleList", bsrVos);
        res.put("total", total);
        res.put("baseMap", baseMap);

        return res;
    }

    private String getDayOfWeek(DateTime dateTime) {
        String dayOfWeek = "";
        switch (dateTime.getDayOfWeek()) {
            case DateTimeConstants.SUNDAY:
                dayOfWeek = "周日";
                break;
            case DateTimeConstants.MONDAY:
                dayOfWeek = "周一";
                break;
            case DateTimeConstants.TUESDAY:
                dayOfWeek = "周二";
                break;
            case DateTimeConstants.WEDNESDAY:
                dayOfWeek = "周三";
                break;
            case DateTimeConstants.THURSDAY:
                dayOfWeek = "周四";
                break;
            case DateTimeConstants.FRIDAY:
                dayOfWeek = "周五";
                break;
            case DateTimeConstants.SATURDAY:
                dayOfWeek = "周六";
                break;

        }
        return dayOfWeek;
    }

    @Override
    public List<Schedule> getScheduleDetail(String hoscode, String depcode, String workDate) {
        List<Schedule> list = scheduleRepository
                .getScheduleByHoscodeAndDepcodeAndWorkDate(hoscode, depcode, new DateTime(workDate).toDate());
        list.forEach(item -> {
            Map<String, Object> param = item.getParam();
            param.put("hosname", hospitalService.getHosnameByHoscode(hoscode));
            param.put("depname", departmentService.getDepname(hoscode, depcode));
            param.put("dayOfweek", getDayOfWeek(new DateTime(item.getWorkDate())));
        });

        return list;
    }


}
