package com.uuuuuuuuuuuuuuu.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author juquansheng
 * @since 2020-07-06
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
@ApiModel("用户账号信息表")
@TableName("user_account")
public class UserAccount extends BaseEntity<UserAccount> {

    private static final long serialVersionUID = 1L;



}
