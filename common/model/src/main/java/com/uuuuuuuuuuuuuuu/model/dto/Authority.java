package com.uuuuuuuuuuuuuuu.model.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
@ApiModel("权限")
public class Authority {
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Long userId;
   /* *//**
     * 角色集合
     *//*
    @ApiModelProperty(value = "角色集合")
    private List<Role> roles;
    *//**
     * 权限集合
     *//*
    @ApiModelProperty(value = "权限集合")
    private List<Permission> permissions;*/
}
