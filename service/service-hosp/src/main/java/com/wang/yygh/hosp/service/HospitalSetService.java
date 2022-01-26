package com.wang.yygh.hosp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wang.yygh.model.hosp.HospitalSet;

/**
 * 医院设置接口
 */
public interface HospitalSetService extends IService<HospitalSet> {


    String getSignKey(String hoscode);
}
