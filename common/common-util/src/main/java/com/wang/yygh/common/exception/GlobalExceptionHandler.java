package com.wang.yygh.common.exception;


import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import com.wang.yygh.common.util.Result;

/**
 * 全局异常处理
 *
 * @author Wang
 * @since 2022/1/25
 */

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(YyghException.class)
    @ResponseBody
    public Result error(YyghException e) {

        e.printStackTrace();
        return Result.fail();
    }
}
