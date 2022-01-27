package com.wang.yygh.cmn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wang.yygh.model.cmn.Dict;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface DictService extends IService<Dict> {
    List<Dict> findChildData(Long id);

    /**
     * 导出
     */
    void exportData(HttpServletResponse response);

    /**
     * 导入
     */
    void importData(MultipartFile response);

    String getDictName(String dictCode, String value);

    String getDictName(String value);

    List<Dict> findByDictCode(String dictCode);
}
