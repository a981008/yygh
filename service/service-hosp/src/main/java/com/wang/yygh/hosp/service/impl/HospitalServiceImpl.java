package com.wang.yygh.hosp.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.wang.yygh.cmn.client.DictFeignClient;
import com.wang.yygh.common.exception.YyghException;
import com.wang.yygh.common.util.ResultCodeEnum;
import com.wang.yygh.hosp.repository.HospitalRepository;
import com.wang.yygh.hosp.service.HospitalService;
import com.wang.yygh.model.hosp.Hospital;
import com.wang.yygh.vo.hosp.HospitalQueryVo;
import com.wang.yygh.vo.hosp.HospitalSetQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Wang
 * @since 2022/1/26
 */
@Service
public class HospitalServiceImpl implements HospitalService {
    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private DictFeignClient dictFeignClient;

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

    @Override
    public Page<Hospital> getHospPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo) {
        PageRequest pageRequest = PageRequest.of(page - 1, limit);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);

        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo, hospital);
        Example<Hospital> example = Example.of(hospital, matcher);

        Page<Hospital> pages = hospitalRepository.findAll(example, pageRequest);
        pages.getContent().forEach(this::setHospitalHosType);

        return pages;
    }

    @Override
    public void updateStatus(String id, Integer status) {
        Hospital hospital = hospitalRepository.findById(id).get();
        hospital.setStatus(status);
        hospital.setCreateTime(new Date());
        hospitalRepository.save(hospital);
    }

    @Override
    public  Map<String, Object> showHospDetail(String id) {
        HashMap<String, Object> res = new HashMap<>();
        Hospital hospital = hospitalRepository.findById(id).get();
        res.put("bookingRule", hospital.getBookingRule());
        hospital.setBookingRule(null);
        res.put("hospital", hospital);
        return res;
    }

    @Override
    public String getHosnameByHoscode(String hoscode) {
        return getByHoscode(hoscode).getHosname();
    }

    private void setHospitalHosType(Hospital hospital) {
        String hostype = dictFeignClient.getName("Hostype", hospital.getHostype());

        String province = dictFeignClient.getName(hospital.getProvinceCode());
        String city = dictFeignClient.getName(hospital.getCityCode());
        String district = dictFeignClient.getName(hospital.getDistrictCode());

        Map<String, Object> param = hospital.getParam();
        param.put("hostypeString", hostype);
        param.put("fullAddress", province + city + district);
    }
}
