package com.uuuuuuuuuuuuuuu.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel("权限dto")
public class AuthorityDto {
    /**
     * 角色名称
     */
    @ApiModelProperty(value = "用户账户")
    private String roleName;
    /**
     * 权限名称
     */
    @ApiModelProperty(value = "用户账户")
    private String pmsName;
    /**
     * 资源
     */
    @ApiModelProperty(value = "资源")
    private String path;
}
