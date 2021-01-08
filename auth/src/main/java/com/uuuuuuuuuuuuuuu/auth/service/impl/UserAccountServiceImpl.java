package com.uuuuuuuuuuuuuuu.auth.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.uuuuuuuuuuuuuuu.auth.mapper.UserAccountMapper;
import com.uuuuuuuuuuuuuuu.auth.service.UserAccountService;
import com.uuuuuuuuuuuuuuu.model.entity.UserAccount;
import com.uuuuuuuuuuuuuuu.model.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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
public class UserAccountServiceImpl extends ServiceImpl<UserAccountMapper, UserAccount> implements UserAccountService {

    @Autowired
    private UserAccountMapper userAccountMapper;

    @Override
    public UserAccount getUserByAccount(String account) {
        QueryWrapper<UserAccount> queryWrapper = new QueryWrapper<UserAccount>();
        List<UserAccount> userAccountList = userAccountMapper.selectList(queryWrapper);
        return userAccountList != null && userAccountList.size() > 0 ? userAccountList.get(0) : null ;
    }

    @Override
    public UserAccount getUserByEmail(String email) {
        QueryWrapper<UserAccount> queryWrapper = new QueryWrapper<UserAccount>();
        List<UserAccount> userAccountList = userAccountMapper.selectList(queryWrapper);
        return userAccountList != null && userAccountList.size() > 0 ? userAccountList.get(0) : null ;
    }

    @Override
    public UserAccount getUserByPhone(String phone) {
        QueryWrapper<UserAccount> queryWrapper = new QueryWrapper<UserAccount>();
        List<UserAccount> userAccountList = userAccountMapper.selectList(queryWrapper);
        return userAccountList != null && userAccountList.size() > 0 ? userAccountList.get(0) : null ;
    }

    @Override
    public void updateUserLastLoginInfo(UserAccount userAccount) {
        UserAccount userAccountUpdate = new UserAccount();
        userAccountUpdate.setPkId(userAccount.getPkId());
        userAccountMapper.updateById(userAccountUpdate);
    }

    @Override
    public void updateUserLastLoginInfo(String pkId) {
        UserAccount userAccountUpdate = new UserAccount();
        userAccountUpdate.setPkId(pkId);
        userAccountMapper.updateById(userAccountUpdate);
    }

}
