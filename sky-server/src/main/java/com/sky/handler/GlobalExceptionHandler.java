package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
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
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }


    /**
     *  处理sql异常
     * @param ex
     * @return
     */
    @ExceptionHandler
   public Result exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        // Duplicate entry 'xiaozhi' for key 'employee.idx_username'
        String message = ex.getMessage();
        // 如果包含了这个异常信息，就return这个Result.error结果给前端
        if (message.contains("Duplicate entry ")) {
            String[] split = message.split(" ");// 以空格分格字符串，然后放到字符串数组中去
            // 'xiaozhi' 所在的索引是 2
            String username = split[2];
            // 通过常量类，来引用这个常量（常量被static修饰）
            String msg = username + MessageConstant.ALREADY_EXISTS;
            return Result.error(msg);
        }else {
            return Result.error(MessageConstant.UNKNOWN_ERROR);
        }
    }

}
