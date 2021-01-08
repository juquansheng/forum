package com.uuuuuuuuuuuuuuu.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.uuuuuuuuuuuuuuu.model.entity.UserAccount;
import com.uuuuuuuuuuuuuuu.model.vo.Result;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author juquansheng
 * @since 2020-07-06
 */
public interface UserAccountService extends IService<UserAccount> {

    /**
     * 根据账号获取用户信息
     * @param account
     * @return
     */
    UserAccount getUserByAccount(String account);
    /**
     * 根据邮箱获取用户信息
     * @param account
     * @return
     */
    UserAccount getUserByEmail(String account);
    /**
     * 根据手机号获取用户信息
     * @param account
     * @return
     */
    UserAccount getUserByPhone(String account);
    /**
     * 更新用户最后一次登录的状态信息
     *
     * @param userAccount
     * @return
     */
    void updateUserLastLoginInfo(UserAccount userAccount);
    /**
     * 更新用户最后一次登录的状态信息
     *
     * @param pkId 主键
     * @return
     */
    void updateUserLastLoginInfo(String pkId);


}
