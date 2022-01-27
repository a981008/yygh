package com.wang.yygh.cmn.client;

import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("service-cmn")
public interface DictFeignClient {
    @GetMapping("/admin/cmn/dict/getName/{dictCode}/{value}")
    String getName(@PathVariable String dictCode, @PathVariable String value);

    @GetMapping("/admin/cmn/dict/getName/{value}")
    String getName(@PathVariable String value);
}
