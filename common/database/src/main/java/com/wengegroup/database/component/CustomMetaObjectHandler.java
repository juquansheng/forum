package com.wengegroup.database.component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Classname CustomMetaObjectHandler
 * @description 填充策略  @TableField(fill = FieldFill.INSERT/INSERT_UPDATE) 自动填充值
 */
@Component
@Slf4j
public class CustomMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill");
        //创建时间
        this.setFieldValByName("createTime",new Date(),metaObject);
        //更新时间
        this.setFieldValByName("updateTime",new Date(),metaObject);
        //乐观锁默认值
        this.setFieldValByName("version",1,metaObject);
        //deleted默认为0
        this.setFieldValByName("deleted",0,metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill");
        //更新时间
        this.setFieldValByName("updateTime",new Date(),metaObject);
    }
}
