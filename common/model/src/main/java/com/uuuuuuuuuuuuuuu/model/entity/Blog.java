package com.uuuuuuuuuuuuuuu.model.entity;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import java.io.Serializable;
/**
 * <p>
 * 博客表
 * </p>
 *
 * @author juquansheng
 * @since 2021-01-18
 */
public class Blog extends Model<Blog> {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一uid
     */
    private String uid;

    /**
     * 博客标题
     */
    private String title;

    /**
     * 博客简介
     */
    private String summary;

    /**
     * 博客内容
     */
    private String content;

    /**
     * 标签uid
     */
    private String tagUid;

    /**
     * 博客点击数
     */
    private Integer clickCount;

    /**
     * 博客收藏数
     */
    private Integer collectCount;

    /**
     * 标题图片uid
     */
    private String fileUid;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 管理员uid
     */
    private String adminUid;

    /**
     * 是否原创（0:不是 1：是）
     */
    private String isOriginal;

    /**
     * 作者
     */
    private String author;

    /**
     * 文章出处
     */
    private String articlesPart;

    /**
     * 博客分类UID
     */
    private String blogSortUid;

    /**
     * 推荐等级(0:正常)
     */
    private Boolean level;

    /**
     * 是否发布：0：否，1：是
     */
    private String isPublish;

    /**
     * 排序字段
     */
    private Integer sort;

    /**
     * 是否开启评论(0:否 1:是)
     */
    private Boolean openComment;

    /**
     * 类型【0 博客， 1：推广】
     */
    private Boolean type;

    /**
     * 外链【如果是推广，那么将跳转到外链】
     */
    private String outsideLink;

    /**
     * 唯一oid
     */
    private Integer oid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTagUid() {
        return tagUid;
    }

    public void setTagUid(String tagUid) {
        this.tagUid = tagUid;
    }

    public Integer getClickCount() {
        return clickCount;
    }

    public void setClickCount(Integer clickCount) {
        this.clickCount = clickCount;
    }

    public Integer getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(Integer collectCount) {
        this.collectCount = collectCount;
    }

    public String getFileUid() {
        return fileUid;
    }

    public void setFileUid(String fileUid) {
        this.fileUid = fileUid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getAdminUid() {
        return adminUid;
    }

    public void setAdminUid(String adminUid) {
        this.adminUid = adminUid;
    }

    public String getIsOriginal() {
        return isOriginal;
    }

    public void setIsOriginal(String isOriginal) {
        this.isOriginal = isOriginal;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getArticlesPart() {
        return articlesPart;
    }

    public void setArticlesPart(String articlesPart) {
        this.articlesPart = articlesPart;
    }

    public String getBlogSortUid() {
        return blogSortUid;
    }

    public void setBlogSortUid(String blogSortUid) {
        this.blogSortUid = blogSortUid;
    }

    public Boolean getLevel() {
        return level;
    }

    public void setLevel(Boolean level) {
        this.level = level;
    }

    public String getIsPublish() {
        return isPublish;
    }

    public void setIsPublish(String isPublish) {
        this.isPublish = isPublish;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Boolean getOpenComment() {
        return openComment;
    }

    public void setOpenComment(Boolean openComment) {
        this.openComment = openComment;
    }

    public Boolean getType() {
        return type;
    }

    public void setType(Boolean type) {
        this.type = type;
    }

    public String getOutsideLink() {
        return outsideLink;
    }

    public void setOutsideLink(String outsideLink) {
        this.outsideLink = outsideLink;
    }

    public Integer getOid() {
        return oid;
    }

    public void setOid(Integer oid) {
        this.oid = oid;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "Blog{" +
        ", uid=" + uid +
        ", title=" + title +
        ", summary=" + summary +
        ", content=" + content +
        ", tagUid=" + tagUid +
        ", clickCount=" + clickCount +
        ", collectCount=" + collectCount +
        ", fileUid=" + fileUid +
        ", status=" + status +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", adminUid=" + adminUid +
        ", isOriginal=" + isOriginal +
        ", author=" + author +
        ", articlesPart=" + articlesPart +
        ", blogSortUid=" + blogSortUid +
        ", level=" + level +
        ", isPublish=" + isPublish +
        ", sort=" + sort +
        ", openComment=" + openComment +
        ", type=" + type +
        ", outsideLink=" + outsideLink +
        ", oid=" + oid +
        "}";
    }
}
