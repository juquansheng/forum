package com.uuuuuuuuuuuuuuu.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("注册vo")
public class RegisterVo {

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("短信/邮箱验证码")
    private String verifyCode;

    @ApiModelProperty("账号")
    private String account;

    @ApiModelProperty("登录账号类型（1账号 2手机号 2邮箱）")
    private Integer type;

}
