package com.uuuuuuuuuuuuuuu.auth.config.account;


import com.uuuuuuuuuuuuuuu.auth.config.MyUserDetailsService;
import com.uuuuuuuuuuuuuuu.auth.service.UserAccountService;
import com.uuuuuuuuuuuuuuu.model.dto.UserDto;
import com.uuuuuuuuuuuuuuu.util.util.PatternMatcherUtils;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.concurrent.TimeUnit;


@Data
@Builder
public class AccountAuthenticationProvider implements AuthenticationProvider {

    /**
     * 用户登录次数计数  redisKey 前缀
     */
    private static final String LOGIN_COUNT = "login_count_";
    /**
     * 用户登录是否被锁定    一小时 redisKey 前缀
     */
    private static final String IS_LOCK = "is_lock_";

    private MyUserDetailsService userDetailsService;

    private RedisTemplate redisTemplate;

    private UserAccountService userAccountService;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //判断是邮箱还是手机号的正则表达式
        String em = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        //认证逻辑
        UserDetails userDetails;
        String account = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        //判断是哪种方式
        if (PatternMatcherUtils.isPhoneLegal(account)) {
            userDetails = userDetailsService.loadUserByPhone(account);
            accountStatusCheck(userDetails);
            retryLimitCheckPh((UserDto) userDetails,password);
        }else if (account.matches(em)) {
            userDetails = userDetailsService.loadUserByEmail(account);
            accountStatusCheck(userDetails);
            retryLimitCheckEm((UserDto) userDetails,password);
        }else {
            userDetails = userDetailsService.loadUserByUsername(account);
            accountStatusCheck(userDetails);
            retryLimitCheckPhUserName((UserDto) userDetails,password);
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        token.setDetails(authentication.getDetails());
        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    /**
     * 账号可用性校验
     */
    private void accountStatusCheck(UserDetails userDetails) {
        if (!userDetails.isEnabled()) {
            throw new RuntimeException("账号不可用");
        }
    }

    /**
     * 手机号密码校验
     */
    private void retryLimitCheckPh(UserDto userDto, String password) {
        // 访问一次，计数一次
        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
        String loginCountKey = LOGIN_COUNT + userDto.getMobile();
        String isLockKey = IS_LOCK + userDto.getMobile();
        opsForValue.increment(loginCountKey, 1);

        if (redisTemplate.hasKey(isLockKey)) {
            throw new RuntimeException("帐号[" + userDto.getMobile() + "]已被禁止登录！");
        }

        // 计数大于5时，设置用户被锁定一小时
        String loginCount = String.valueOf(opsForValue.get(loginCountKey));
        int retryCount = (5 - Integer.parseInt(loginCount));
        if (retryCount <= 0) {
            opsForValue.set(isLockKey, "LOCK");
            redisTemplate.expire(isLockKey, 1, TimeUnit.HOURS);
            redisTemplate.expire(loginCountKey, 1, TimeUnit.HOURS);
            throw new RuntimeException("由于密码输入错误次数过多，帐号[" + userDto.getMobile() + "]已被禁止登录！");
        }
        boolean matches = doCredentialsMatch(userDto.getPassword(),password);
        if (!matches) {
            String msg = retryCount <= 0 ? "您的账号一小时内禁止登录！" : "您还剩" + retryCount + "次重试的机会";
            throw new RuntimeException("帐号或密码不正确！" + msg);
        }

        //清空登录计数
        redisTemplate.delete(loginCountKey);
        try {
            userAccountService.updateUserLastLoginInfo(userDto.getPkId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 邮箱密码校验
     */
    private void retryLimitCheckEm(UserDto userDto, String password) {
        // 访问一次，计数一次
        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
        String loginCountKey = LOGIN_COUNT + userDto.getEmail();
        String isLockKey = IS_LOCK + userDto.getEmail();
        opsForValue.increment(loginCountKey, 1);

        if (redisTemplate.hasKey(isLockKey)) {
            throw new RuntimeException("帐号[" + userDto.getEmail() + "]已被禁止登录！");
        }

        // 计数大于5时，设置用户被锁定一小时
        String loginCount = String.valueOf(opsForValue.get(loginCountKey));
        int retryCount = (5 - Integer.parseInt(loginCount));
        if (retryCount <= 0) {
            opsForValue.set(isLockKey, "LOCK");
            redisTemplate.expire(isLockKey, 1, TimeUnit.HOURS);
            redisTemplate.expire(loginCountKey, 1, TimeUnit.HOURS);
            throw new RuntimeException("由于密码输入错误次数过多，帐号[" + userDto.getEmail() + "]已被禁止登录！");
        }
        boolean matches = doCredentialsMatch(userDto.getPassword(),password);
        if (!matches) {
            String msg = retryCount <= 0 ? "您的账号一小时内禁止登录！" : "您还剩" + retryCount + "次重试的机会";
            throw new RuntimeException("帐号或密码不正确！" + msg);
        }

        //清空登录计数
        redisTemplate.delete(loginCountKey);
        try {
            userAccountService.updateUserLastLoginInfo(userDto.getPkId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户名密码校验
     */
    private void retryLimitCheckPhUserName(UserDto userDto, String password) {
        // 访问一次，计数一次
        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
        String loginCountKey = LOGIN_COUNT + userDto.getUsername();
        String isLockKey = IS_LOCK + userDto.getUsername();
        opsForValue.increment(loginCountKey, 1);

        if (redisTemplate.hasKey(isLockKey)) {
            throw new RuntimeException("帐号[" + userDto.getUsername() + "]已被禁止登录！");
        }

        // 计数大于5时，设置用户被锁定一小时
        String loginCount = String.valueOf(opsForValue.get(loginCountKey));
        int retryCount = (5 - Integer.parseInt(loginCount));
        if (retryCount <= 0) {
            opsForValue.set(isLockKey, "LOCK");
            redisTemplate.expire(isLockKey, 1, TimeUnit.HOURS);
            redisTemplate.expire(loginCountKey, 1, TimeUnit.HOURS);
            throw new RuntimeException("由于密码输入错误次数过多，帐号[" + userDto.getUsername() + "]已被禁止登录！");
        }
        boolean matches = doCredentialsMatch(userDto.getPassword(),password);
        if (!matches) {
            String msg = retryCount <= 0 ? "您的账号一小时内禁止登录！" : "您还剩" + retryCount + "次重试的机会";
            throw new RuntimeException("帐号或密码不正确！" + msg);
        }

        //清空登录计数
        redisTemplate.delete(loginCountKey);
        try {
            userAccountService.updateUserLastLoginInfo(userDto.getPkId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param encodedPassword 加密后的密码
     * @param rawPasswordVo 用户输入的密码
     * 登录用原始量芯号加当前密码的方式
     */
    private boolean doCredentialsMatch(String encodedPassword, String rawPasswordVo) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPasswordVo,encodedPassword);
    }
}