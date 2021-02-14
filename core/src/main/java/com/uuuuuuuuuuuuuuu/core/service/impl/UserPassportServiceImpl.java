package com.uuuuuuuuuuuuuuu.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.uuuuuuuuuuuuuuu.core.mapper.forum.UserMapper;
import com.uuuuuuuuuuuuuuu.core.mapper.forum.UserPassportMapper;
import com.uuuuuuuuuuuuuuu.core.service.UserPassportService;
import com.uuuuuuuuuuuuuuu.model.constant.Constants;
import com.uuuuuuuuuuuuuuu.model.entity.UserAccount;
import com.uuuuuuuuuuuuuuu.model.entity.forum.User;
import com.uuuuuuuuuuuuuuu.model.entity.forum.UserPassport;
import com.uuuuuuuuuuuuuuu.model.vo.RegisterFromThirdPartyVo;
import com.uuuuuuuuuuuuuuu.model.vo.RegisterVo;
import lombok.Data;
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
public class UserPassportServiceImpl extends ServiceImpl<UserPassportMapper, UserPassport> implements UserPassportService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserPassportMapper userPassportMapper;

    @Override
    public User getUserByPassport(String account,Integer type) {
        try {
            QueryWrapper<UserPassport> userPassportQueryWrapper = new QueryWrapper<UserPassport>();
            userPassportQueryWrapper.lambda().eq(UserPassport::getAccount,account).eq(UserPassport::getType,type).eq(UserPassport::getDeleted, Constants.NORMAL);
            UserPassport userPassport = userPassportMapper.selectOne(userPassportQueryWrapper);

            QueryWrapper<User> userQueryWrapper = new QueryWrapper<User>();
            userQueryWrapper.lambda().eq(User::getId,userPassport.getUserId()).eq(User::getDeleted,Constants.NORMAL);
            return userMapper.selectOne(userQueryWrapper);
        }catch (Exception e){
            return null;
        }
    }


    //TODO
    @Override
    @Transactional(transactionManager = "forumTransactionManager")
    public void updateUserLastLoginInfo(User user) {


        userMapper.updateById(user);
    }

    @Override
    @Transactional(transactionManager = "forumTransactionManager")
    public User register(RegisterVo registerVo) {
        try {
            QueryWrapper<UserPassport> userPassportQueryWrapper = new QueryWrapper<UserPassport>();
            userPassportQueryWrapper.lambda().eq(UserPassport::getAccount,registerVo.getAccount()).eq(UserPassport::getType,registerVo.getType()).eq(UserPassport::getDeleted, Constants.NORMAL);
            UserPassport userPassport = userPassportMapper.selectOne(userPassportQueryWrapper);
            if (userPassport != null){//存在
                return null;
            }
            //新增user
            User user = new User();
            user.setCreateTime(new Date());
            user.setPassword(registerVo.getPassword());
            user.setUserName(registerVo.getAccount());
            userMapper.insert(user);
            //新增UserPassport
            userPassport = new UserPassport();
            userPassport.setAccount(registerVo.getAccount());
            userPassport.setType(registerVo.getType());
            userPassport.setCreateTime(new Date());
            userPassport.setUserId(user.getId());
            userPassportMapper.insert(userPassport);
            return user;
        }catch (Exception e){
            return null;
        }

    }

}
