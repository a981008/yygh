package com.wang.yygh.cmn.controller;

import com.wang.yygh.cmn.service.DictService;
import com.wang.yygh.common.result.Result;
import com.wang.yygh.model.cmn.Dict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
