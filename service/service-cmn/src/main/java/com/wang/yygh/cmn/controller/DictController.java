package com.wang.yygh.cmn.controller;

import com.wang.yygh.cmn.service.DictService;
import com.wang.yygh.common.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;


/**
 * @author Wang
 * @since 2022/1/25
 */

@CrossOrigin
@Api(tags = "数据字典")
@RestController
@RequestMapping("/admin/cmn/dict")
public class DictController {
    @Autowired
    private DictService dictService;

    @ApiOperation(value = "根据id查询子数据列表")
    @GetMapping("childData/{id}")
    public Result findChildData(@PathVariable Long id) {
        return Result.ok(dictService.findChildData(id));
    }

    @ApiOperation(value = "导出数据字典")
    @GetMapping("exportData")
    public Result exportData(HttpServletResponse response) {
        dictService.exportData(response);
        return null;
    }

    @ApiOperation(value = "导入数据字典")
    @PostMapping("importData")
    public Result importData(MultipartFile file) {
        dictService.importData(file);
        return Result.ok();
    }

}
