package com.uuuuuuuuuuuuuuu.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.uuuuuuuuuuuuuuu.core.mapper.forum.UserMapper;
import com.uuuuuuuuuuuuuuu.core.mapper.forum.UserPassportMapper;
import com.uuuuuuuuuuuuuuu.core.mapper.forum.UserThirdPartyMapper;
import com.uuuuuuuuuuuuuuu.core.service.UserPassportService;
import com.uuuuuuuuuuuuuuu.core.service.UserThirdPartyService;
import com.uuuuuuuuuuuuuuu.model.constant.Constants;
import com.uuuuuuuuuuuuuuu.model.entity.forum.User;
import com.uuuuuuuuuuuuuuu.model.entity.forum.UserPassport;
import com.uuuuuuuuuuuuuuu.model.entity.forum.UserThirdParty;
import com.uuuuuuuuuuuuuuu.model.vo.RegisterFromThirdPartyVo;
import com.uuuuuuuuuuuuuuu.model.vo.RegisterVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author juquansheng
 * @since 2020-07-06
 */
@Service
@Slf4j
public class UserThirdPartyServiceImpl extends ServiceImpl<UserThirdPartyMapper, UserThirdParty> implements UserThirdPartyService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserThirdPartyMapper userThirdPartyMapper;


    @Override
    public User getUserByThirdParty(String uuid,String source) {
        try {
            QueryWrapper<UserThirdParty> userThirdPartyQueryWrapper = new QueryWrapper<UserThirdParty>();
            userThirdPartyQueryWrapper.lambda().eq(UserThirdParty::getUuid,uuid).eq(UserThirdParty::getSource,source).eq(UserThirdParty::getDeleted, Constants.NORMAL);
            UserThirdParty userThirdParty = userThirdPartyMapper.selectOne(userThirdPartyQueryWrapper);

            QueryWrapper<User> userQueryWrapper = new QueryWrapper<User>();
            userQueryWrapper.lambda().eq(User::getId,userThirdParty.getUserId()).eq(User::getDeleted,Constants.NORMAL);
            return userMapper.selectOne(userQueryWrapper);
        }catch (Exception e){
            return null;
        }
    }


    @Override
    @Transactional(transactionManager = "forumTransactionManager")
    public User registerFromThirdParty(RegisterFromThirdPartyVo registerFromThirdPartyVo, String source) {
        try {
            //查询是否存在当前第三方用户
            User user = getUserByThirdParty(registerFromThirdPartyVo.getUuid(), source);
            //存在直接返回用户
            if (user != null){
                return user;
            }else {
                //不存在新增用户
                User userInsert = new User();
                userInsert.setCreateTime(new Date());
                userInsert.setPassword(null);
                userInsert.setUserName(null);
                userMapper.insert(userInsert);
                //新增第三方账号数据
                UserThirdParty userThirdParty = new UserThirdParty();
                userThirdParty.setUuid(registerFromThirdPartyVo.getUuid());
                userThirdParty.setSource(source);
                userThirdPartyMapper.insert(userThirdParty);
                return userInsert;
            }
        }catch (Exception e){
            return null;
        }

    }

}
