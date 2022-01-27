package com.wang.yygh.hosp.service;


import com.wang.yygh.model.hosp.Hospital;
import com.wang.yygh.vo.hosp.HospitalQueryVo;
import com.wang.yygh.vo.hosp.HospitalSetQueryVo;
import org.springframework.data.domain.Page;

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

    Page<Hospital> getHospPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo);

    void updateStatus(String id, Integer status);

    Map<String, Object> showHospDetail(String id);

    String getHosnameByHoscode(String hoscode);
}
