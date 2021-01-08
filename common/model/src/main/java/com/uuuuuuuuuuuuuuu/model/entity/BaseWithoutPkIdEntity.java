package com.uuuuuuuuuuuuuuu.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @Classname BaseEntity
 * @description 公共实体
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel("公共实体")
@Data
public class BaseWithoutPkIdEntity<T extends Model<?>> extends Model<T> {

    private static final long serialVersionUID = 1557037565710352118L;


    @ApiModelProperty("创建时间")
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("修改时间")
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty("删除标志")
    @TableField(value = "deleted",fill = FieldFill.INSERT)
    @TableLogic(value = "0",delval = "1")
    private Integer deleted;

    @ApiModelProperty("乐观锁标识")
    @Version
    @TableField(value = "version",fill = FieldFill.INSERT)
    private Integer version;

}
