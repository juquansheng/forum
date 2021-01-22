package com.uuuuuuuuuuuuuuu.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description 手机号验证码登录Vo
 */
@ApiModel("手机号验证码登录")
@Data
public class MobileLoginVo {

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("验证码")
    private String code;
}
