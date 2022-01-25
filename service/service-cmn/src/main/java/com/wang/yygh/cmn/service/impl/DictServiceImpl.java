package com.wang.yygh.cmn.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wang.yygh.cmn.mapper.DictMapper;
import com.wang.yygh.cmn.service.DictService;
import com.wang.yygh.model.cmn.Dict;
import com.wang.yygh.model.hosp.HospitalSet;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Wang
 * @since 2022/1/25
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
    @Override
    public List<Dict> findChildData(Long id) {
        List<Dict> children = baseMapper.selectList(Wrappers.<Dict>lambdaQuery()
                .eq(Dict::getParentId, id));
        // 设置是否存在子节点
        for (Dict child : children) {
            child.setHasChildren(isChildren(child.getId()));
        }
        return children;
    }

    /**
     * 是否存在子节点
     */
    private boolean isChildren(Long id) {
        return baseMapper.selectCount(Wrappers
                .<Dict>lambdaQuery().eq(Dict::getId, id)) > 0;
    }
}
