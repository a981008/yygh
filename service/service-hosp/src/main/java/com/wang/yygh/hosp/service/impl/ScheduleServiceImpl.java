package com.wang.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.wang.yygh.hosp.repository.ScheduleRepository;
import com.wang.yygh.hosp.service.ScheduleService;
import com.wang.yygh.model.hosp.Department;
import com.wang.yygh.model.hosp.Schedule;
import com.wang.yygh.vo.hosp.ScheduleQueryVo;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * @author Wang
 * @since 2022/1/27
 */

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    ScheduleRepository scheduleRepository;

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
}
