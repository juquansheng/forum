package com.uuuuuuuuuuuuuuu.model.vo;


import com.uuuuuuuuuuuuuuu.model.entity.BaseEntity;
import com.uuuuuuuuuuuuuuu.model.es.entity.BlogSort;
import com.uuuuuuuuuuuuuuu.model.es.entity.Tag;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**

 */
@Data
public class BlogVO extends BaseEntity<BlogVO> {

    /**
     * 博客标题
     */
    @NotBlank(message = "博客标题不能为空！")
    private String title;

    /**
     * 博客简介
     */
    private String summary;

    /**
     * 博客内容
     */
    @NotBlank(message = "博客内容不能为空！")
    private String content;

    /**
     * 标签uid
     */
    @NotBlank(message = "标签uid不能为空！")
    private String tagUid;
    /**
     * 博客分类UID
     */
    @NotBlank(message = "博客分类不能为空！")
    private String blogSortUid;
    /**
     * 标题图片UID
     */
    private String fileUid;
    /**
     * 用户id
     */
    //@NotBlank(message = "用户id不能为空！")
    private String userId;
    /**
     * 是否发布
     */
    private String isPublish;
    /**
     * 是否原创
     */
    //@NotBlank(message = "博客内容不能为空！")
    private String isOriginal;
    /**
     * 如果原创，作者为管理员名
     */
    private String author;
    /**
     * 文章出处
     */
    private String articlesPart;
    /**
     * 推荐级别，用于首页推荐
     * 0：正常
     * 1：一级推荐(轮播图)
     * 2：二级推荐(top)
     * 3：三级推荐 ()
     * 4：四级 推荐 (特别推荐)
     */
    private Integer level;

    /**
     * 类型【0 博客， 1：推广】
     */
    private String type;

    /**
     * 外链【如果是推广，那么将跳转到外链】
     */
    private String outsideLink;

    /**
     * 标签,一篇博客对应多个标签
     */
    private List<Tag> tagList;

    // 以下字段不存入数据库，封装为了方便使用
    /**
     * 标题图
     */
    private List<String> photoList;
    /**
     * 博客分类
     */
    private BlogSort blogSort;
    /**
     * 点赞数
     */
    private Integer praiseCount;
    /**
     * 版权申明
     */
    private String copyright;

    /**
     * 博客等级关键字，仅用于getList
     */
    private String levelKeyword;

    /**
     * 使用Sort字段进行排序 （0：不使用， 1：使用），默认为0
     */
    private Integer useSort;

    /**
     * 排序字段，数值越大，越靠前
     */
    private Integer sort;

    /**
     * 是否开启评论(0:否， 1:是)
     */
    private String openComment;

    /**
     * 无参构造方法，初始化默认值
     */
    BlogVO() {
        this.level = 0;
        this.useSort = 0;
    }
}
