package com.wang.yygh.cmn.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wang.yygh.cmn.service.DictService;
import com.wang.yygh.common.util.Result;
import com.wang.yygh.model.cmn.Dict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * @author Wang
 * @since 2022/1/25
 */

@Api(tags = "数据字典")
@RestController
@RequestMapping("/admin/cmn/dict")
public class DictController {
    @Autowired
    private DictService dictService;

    @ApiOperation("根据id查询子数据列表")
    @GetMapping("/childData/{id}")
    public Result findChildData(@PathVariable Long id) {
        return Result.ok(dictService.findChildData(id));
    }

    @ApiOperation("导出数据字典")
    @GetMapping("/exportData")
    public Result exportData(HttpServletResponse response) {
        dictService.exportData(response);
        return null;
    }

    @ApiOperation("导入数据字典")
    @PostMapping("/importData")
    public Result importData(MultipartFile file) {
        dictService.importData(file);
        return Result.ok();
    }

    @ApiOperation("查询name")
    @GetMapping("/getName/{dictCode}/{value}")
    public String getName(@PathVariable String dictCode, @PathVariable String value) {
        return dictService.getDictName(dictCode, value);
    }

    @ApiOperation("查询name")
    @GetMapping("/getName/{value}")
    public String getName(@PathVariable String value) {
        return dictService.getDictName(value);
    }

    @ApiOperation("根据dictCode获取下级节点")
    @GetMapping("/findByDictCode/{dictCode}")
    public Result findByDictCode(@PathVariable String dictCode) {
        List<Dict> list  = dictService.findByDictCode(dictCode);
        return Result.ok(list);
    }

}
