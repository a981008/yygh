package com.wang.yygh.hosp.service;


import com.wang.yygh.model.hosp.Hospital;

import java.util.Map;

/**
 * 医院接口
 */
public interface HospitalService {
    /**
     * 数据库中不存在则添加，存在则更新
     */
    void save(Hospital hospital);

    /**
     * 更具 hoscode 查询医院信息
     */
    Hospital getByHoscode(String hoscode);
}
