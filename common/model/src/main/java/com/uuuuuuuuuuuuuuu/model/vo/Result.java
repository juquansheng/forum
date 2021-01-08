
package com.uuuuuuuuuuuuuuu.model.vo;

import cn.hutool.http.HttpStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 响应信息主体
 * @param <T>
 */
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "响应信息主体")
public class Result<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	@ApiModelProperty(value = "返回标记：成功标记=0，失败标记=1")
	private int code;

	@Getter
	@Setter
	@ApiModelProperty(value = "返回信息")
	private String msg;


	@Getter
	@Setter
	@ApiModelProperty(value = "数据")
	private T data;

	public static <T> Result<T> ok() {
		return restResult(null, HttpStatus.HTTP_OK, null);
	}

	public static <T> Result<T> ok(T data) {
		return restResult(data, HttpStatus.HTTP_OK, null);
	}

	public static <T> Result<T> ok(String msg) {
		return restResult(null, HttpStatus.HTTP_OK, msg);
	}

	public static <T> Result<T> ok(T data, String msg) {
		return restResult(data, HttpStatus.HTTP_OK, msg);
	}

	public static <T> Result<T> failed() {
		return restResult(null, HttpStatus.HTTP_INTERNAL_ERROR, null);
	}

	public static <T> Result<T> failed(String msg) {
		return restResult(null, HttpStatus.HTTP_INTERNAL_ERROR, msg);
	}

	public static <T> Result<T> failed(T data, String msg) {
		return restResult(data, HttpStatus.HTTP_INTERNAL_ERROR, msg);
	}

	public static <T> Result<T> unAuth(String msg) {
		return restResult(null, HttpStatus.HTTP_UNAUTHORIZED, msg);
	}

	public static <T> Result<T> loginFailed(String msg) {
		return restResult(null, HttpStatus.HTTP_UNAUTHORIZED, msg);
	}

	private static <T> Result<T> restResult(T data, int code, String msg) {
		Result<T> apiResult = new Result<>();
		apiResult.setCode(code);
		apiResult.setData(data);
		apiResult.setMsg(msg);
		return apiResult;
	}
}
