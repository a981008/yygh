package com.wang.yygh.hosp.service;


import com.wang.yygh.model.hosp.Schedule;
import com.wang.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface ScheduleService {
    void save(Schedule schedule);

    Page<Schedule> findPageSchedule(ScheduleQueryVo scheduleQueryVo, Integer page, Integer limit);

    void removeSchedule(String hoscode, String hosScheduleId);

    Map<String, Object> getScheduleRule(String hoscode, String depcode, Long page, Long limit);

    List<Schedule> getScheduleDetail(String hoscode, String depcode, String workDate);
}
