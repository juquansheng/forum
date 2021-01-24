package com.uuuuuuuuuuuuuuu.oauth2resource.handler;


import com.uuuuuuuuuuuuuuu.model.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理
 * @author chuanfang
 */
@Slf4j
@RestController
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result processDefaultException(HttpServletResponse response,
                                          Exception e) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setContentType("application/json;charset=UTF-8");
        Result<Object> result = new Result<>();
        result.setCode(cn.hutool.http.HttpStatus.HTTP_INTERNAL_ERROR);
        result.setMsg(e.getMessage());
        return result;
    }
}