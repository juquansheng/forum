package com.uuuuuuuuuuuuuuu.model.entity.forum;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
/**
 * <p>
 * 
 * </p>
 *
 * @author juquansheng
 * @since 2021-01-27
 */
public class UserPassport extends Model<UserPassport> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 删除标识
     */
    private Integer deleted;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * user表主键id
     */
    private Long userId;

    /**
     * 登录账号类型（1账号 2手机号 2邮箱）
     */
    private Integer type;

    /**
     * 登录账号
     */
    private String account;

    /**
     * 状态默认0正常-1禁用
     */
    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "UserPassport{" +
        ", id=" + id +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", deleted=" + deleted +
        ", version=" + version +
        ", userId=" + userId +
        ", type=" + type +
        ", account=" + account +
        ", status=" + status +
        "}";
    }
}
