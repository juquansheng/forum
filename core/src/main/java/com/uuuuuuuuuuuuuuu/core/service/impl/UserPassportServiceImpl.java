package com.uuuuuuuuuuuuuuu.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.uuuuuuuuuuuuuuu.core.mapper.forum.UserMapper;
import com.uuuuuuuuuuuuuuu.core.mapper.forum.UserPassportMapper;
import com.uuuuuuuuuuuuuuu.core.service.UserPassportService;
import com.uuuuuuuuuuuuuuu.model.entity.forum.User;
import com.uuuuuuuuuuuuuuu.model.entity.forum.UserPassport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public User getUserByPassport(String account,Integer type) {
        try {
            QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
            return userMapper.selectOne(queryWrapper);
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

}
