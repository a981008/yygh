package com.wang.yygh.hosp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wang.yygh.hosp.mapper.HospitalSetMapper;
import com.wang.yygh.hosp.service.HospitalSetService;
import com.wang.yygh.model.hosp.HospitalSet;
import org.springframework.stereotype.Service;

/**
 * @author Wang
 * @since 2022/1/25
 */
@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet> implements HospitalSetService {

    @Override
    public String getSignKey(String hoscode) {
        LambdaQueryWrapper<HospitalSet> wrapper = Wrappers.<HospitalSet>lambdaQuery()
                .eq(HospitalSet::getHoscode, hoscode);
        HospitalSet hospitalSet = baseMapper.selectOne(wrapper);
        return hospitalSet.getSignKey();
    }
}
