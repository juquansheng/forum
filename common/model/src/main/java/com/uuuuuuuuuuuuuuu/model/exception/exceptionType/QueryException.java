package com.uuuuuuuuuuuuuuu.model.exception.exceptionType;


import com.uuuuuuuuuuuuuuu.model.global.BaseMessageConf;
import com.uuuuuuuuuuuuuuu.model.global.ErrorCode;

import java.io.Serializable;

/**
 * 自定义查询操作相关的异常
 */
public class QueryException extends RuntimeException implements Serializable {

    /**
     * 异常状态码
     */
    private String code;

    public QueryException() {
        super(BaseMessageConf.QUERY_DEFAULT_ERROR);
        this.code = ErrorCode.QUERY_DEFAULT_ERROR;
    }

    public QueryException(String message, Throwable cause) {
        super(message, cause);
        this.code = ErrorCode.QUERY_DEFAULT_ERROR;
    }

    public QueryException(String message) {
        super(message);
        this.code = ErrorCode.QUERY_DEFAULT_ERROR;
    }

    public QueryException(String code, String message) {
        super(message);
        this.code = code;
    }

    public QueryException(Throwable cause) {
        super(cause);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
