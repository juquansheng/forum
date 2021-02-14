package com.uuuuuuuuuuuuuuu.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.uuuuuuuuuuuuuuu.model.entity.forum.User;
import com.uuuuuuuuuuuuuuu.model.entity.forum.UserPassport;
import com.uuuuuuuuuuuuuuu.model.entity.forum.UserThirdParty;
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
public interface UserThirdPartyService extends IService<UserThirdParty> {


    /**
     * 根据第三方信息获取用户信息
     * @param uuid 第三方唯一id
     * @param source 来源
     * @return
     */
    User getUserByThirdParty(String uuid, String source);


    /**
     * 第三方注册
     * @param registerFromThirdPartyVo
     * @param source
     * @return
     */
    User registerFromThirdParty(RegisterFromThirdPartyVo registerFromThirdPartyVo, String source);

}
