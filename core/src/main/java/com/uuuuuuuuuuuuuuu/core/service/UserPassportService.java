package com.uuuuuuuuuuuuuuu.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.uuuuuuuuuuuuuuu.model.entity.UserAccount;
import com.uuuuuuuuuuuuuuu.model.entity.forum.User;
import com.uuuuuuuuuuuuuuu.model.entity.forum.UserPassport;
import com.uuuuuuuuuuuuuuu.model.vo.RegisterFromThirdPartyVo;
import com.uuuuuuuuuuuuuuu.model.vo.RegisterVo;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author juquansheng
 * @since 2020-07-06
 */
public interface UserPassportService extends IService<UserPassport> {

    /**
     * 根据账号获取用户信息
     * @param account 账号
     * @param type 账号类型
     * @return
     */
    User getUserByPassport(String account,Integer type);


    /**
     * 更新用户最后一次登录的状态信息
     *
     * @param user
     * @return
     */
    void updateUserLastLoginInfo(User user);

    /**
     * 注册
     * @param registerVo
     * @return
     */
    User register(RegisterVo registerVo);



}
