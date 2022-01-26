package com.wang.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.wang.yygh.hosp.repository.HospitalRepository;
import com.wang.yygh.hosp.service.HospitalService;
import com.wang.yygh.model.hosp.Hospital;
import com.wang.yygh.model.hosp.Schedule;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * @author Wang
 * @since 2022/1/26
 */
@Service
public class HospitalServiceImpl implements HospitalService {
    @Autowired
    private HospitalRepository hospitalRepository;

    @Override
    public void save(Hospital hospital) {
        String hoscode = hospital.getHoscode();
        Hospital oldHospital = getByHoscode(hoscode);
        Date date = new Date();
        hospital.setUpdateTime(date);
        hospital.setIsDeleted(0);
        if (ObjectUtils.isNotNull(oldHospital)) {
            BeanUtils.copyProperties(hospital, oldHospital);
            hospitalRepository.save(oldHospital);
        } else {
            hospital.setCreateTime(date);
            hospitalRepository.save(hospital);
        }

        hospitalRepository.save(hospital);
    }

    @Override
    public Hospital getByHoscode(String hoscode) {
        Hospital hospital = new Hospital();
        hospital.setHoscode(hoscode);
        hospital.setIsDeleted(0);
        ExampleMatcher matching = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        Example<Hospital> example = Example.of(hospital, matching);
        return hospitalRepository.findOne(example).get();
    }
}
