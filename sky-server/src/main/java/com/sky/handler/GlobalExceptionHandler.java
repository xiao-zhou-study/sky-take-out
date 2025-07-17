package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    /**
     * 捕获异常日志
     * @param ex 形参
     */
    private void logException (Exception ex){
        log.error("异常信息：{}", ex.getMessage());
    }
    /**
     * 捕获业务异常
     * @param ex 形参
     * @return 异常结果
     */
    @ExceptionHandler
    public Result<T> exceptionHandler(BaseException ex){
        logException(ex);
        return Result.error(ex.getMessage());
    }

    /**
     * 处理数据重复异常
     * @param ex 形参
     * @return 错误结果
     */
    @ExceptionHandler
    public Result<T> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        String message = ex.getMessage();
        if(!message.contains("Duplicate entry")){
            logException(ex);
            return Result.error(MessageConstant.UNKNOWN_ERROR);
        }
        logException(ex);
        return Result.error(message.split(" ")[2]+MessageConstant.ACCOUNT_EXISTS);

    }

}
