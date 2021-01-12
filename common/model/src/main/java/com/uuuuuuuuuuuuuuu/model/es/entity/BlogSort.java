package com.uuuuuuuuuuuuuuu.model.es.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * <p>
 * 博客分类表
 * </p>
 */
@Data
@TableName("blog_sort")
public class BlogSort {

    private static final long serialVersionUID = 1L;


    /**
     * 分类名
     */
    private String sortName;

    /**
     * 分类介绍
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String content;

    /**
     * 点击数
     */
    private Integer clickCount;

    /**
     * 排序字段，数值越大，越靠前
     */
    private Integer sort;
}
