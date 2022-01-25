package com.wang.yygh.cmn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wang.yygh.model.cmn.Dict;
import com.wang.yygh.model.hosp.HospitalSet;

import java.util.List;

public interface DictService extends IService<Dict> {
    List<Dict> findChildData(Long id);
}
