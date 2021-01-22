package com.uuuuuuuuuuuuuuu.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @description 登录vo
 */
@Data
@ApiModel("登录vo")
public class LoginVo implements Serializable {

    private static final long serialVersionUID = 123456L;

    @ApiModelProperty("账号")
    private String account;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("记住我")
    private boolean rememberMe;

    @ApiModelProperty("图片验证码")
    private String captchaCode;

    @ApiModelProperty("图片验证码key")
    private String captchaCodeKey;


}
