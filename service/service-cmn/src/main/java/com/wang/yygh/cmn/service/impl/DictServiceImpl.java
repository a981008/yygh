package com.wang.yygh.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wang.yygh.cmn.listener.DictListener;
import com.wang.yygh.cmn.mapper.DictMapper;
import com.wang.yygh.cmn.service.DictService;
import com.wang.yygh.model.cmn.Dict;
import com.wang.yygh.model.hosp.HospitalSet;
import com.wang.yygh.vo.cmn.DictEeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Wang
 * @since 2022/1/25
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
    @Override
    @Cacheable(value = "dict", keyGenerator = "keyGenerator")
    public List<Dict> findChildData(Long id) {
        List<Dict> children = baseMapper.selectList(Wrappers.<Dict>lambdaQuery()
                .eq(Dict::getParentId, id));
        // 设置是否存在子节点
        for (Dict child : children) {
            child.setHasChildren(isChildren(child.getId()));
        }
        return children;
    }

    @Override
    public void exportData(HttpServletResponse response) {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");

        String fileName = "dict";
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

        List<Dict> dictList = baseMapper.selectList(null);
        List<DictEeVo> dictVoList = new ArrayList<>(dictList.size());
        for (Dict dict : dictList) {
            DictEeVo dictVo = new DictEeVo();
            BeanUtils.copyProperties(dict, dictVo);
            dictVoList.add(dictVo);
        }

        try {
            EasyExcel.write(response.getOutputStream(), DictEeVo.class).sheet().doWrite(dictVoList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @CacheEvict(value = "dict", allEntries = true)
    public void importData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), DictEeVo.class, new DictListener(baseMapper)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getDictName(String dictCode, String value) {
        if (StringUtils.isEmpty(dictCode)) {
            return baseMapper.selectOne(Wrappers.<Dict>lambdaQuery()
                            .eq(Dict::getValue, value))
                    .getName();
        } else {
            Dict dict = getDictByDictCode(dictCode);

            return baseMapper.selectOne(Wrappers.<Dict>lambdaQuery()
                            .eq(Dict::getParentId, dict.getId()).eq(Dict::getValue, value))
                    .getName();
        }
    }

    private Dict getDictByDictCode(String dictCode) {
        return baseMapper.selectOne(Wrappers.<Dict>lambdaQuery()
                .eq(Dict::getDictCode, dictCode));
    }

    @Override
    public String getDictName(String value) {
        return getDictName(null, value);
    }

    @Override
    public List<Dict> findByDictCode(String dictCode) {
        Dict dict = getDictByDictCode(dictCode);
        return findChildData(dict.getId());
    }

    /**
     * 是否存在子节点
     */
    private boolean isChildren(Long id) {
        return baseMapper.selectCount(Wrappers
                .<Dict>lambdaQuery().eq(Dict::getId, id)) > 0;
    }
}
