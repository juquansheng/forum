package com.uuuuuuuuuuuuuuu.auth.config.account;


import com.uuuuuuuuuuuuuuu.auth.config.MyUserDetailsService;
import com.uuuuuuuuuuuuuuu.model.dto.UserDto;
import com.uuuuuuuuuuuuuuu.redis.utils.RedisClient;
import com.uuuuuuuuuuuuuuu.util.util.PatternMatcherUtils;
import lombok.Builder;
import lombok.Data;
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
    private static final String ACCOUNT_IS_LOCK = "account_is_lock_";
    /**
     * 登录ip是否被锁定    一小时 redisKey 前缀
     */
    private static final String IP_IS_LOCK = "ip_is_lock_";

    private MyUserDetailsService userDetailsService;


    private RedisClient redisClient;



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
        }else if (account.matches(em)) {
            userDetails = userDetailsService.loadUserByEmail(account);
        }else {
            userDetails = userDetailsService.loadUserByUsername(account);
        }
        accountStatusCheck(userDetails);
        retryLimitCheckPhUserName((UserDto) userDetails,password);
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
     * 用户名密码校验
     */
    private void retryLimitCheckPhUserName(UserDto userDto, String password) {
        // 访问一次，计数一次
        String loginCountKey = LOGIN_COUNT + userDto.getUsername();
        String isLockKey = ACCOUNT_IS_LOCK + userDto.getUsername();
        redisClient.incrBy(loginCountKey, 1);

        if (redisClient.hasKey(isLockKey)) {
            throw new RuntimeException("帐号[" + userDto.getUsername() + "]已被禁止登录！");
        }

        // 计数大于5时，设置用户被锁定一小时
        String loginCount = String.valueOf(redisClient.get(loginCountKey));
        int retryCount = (5 - Integer.parseInt(loginCount));
        if (retryCount <= 0) {
            redisClient.set(isLockKey, "LOCK");
            redisClient.expire(isLockKey, 1, TimeUnit.HOURS);
            redisClient.expire(loginCountKey, 1, TimeUnit.HOURS);
            throw new RuntimeException("由于密码输入错误次数过多，帐号[" + userDto.getUsername() + "]已被禁止登录！");
        }
        boolean matches = doCredentialsMatch(userDto.getPassword(),password);
        if (!matches) {
            String msg = "您还剩" + retryCount + "次重试的机会";
            throw new RuntimeException("帐号或密码不正确！" + msg);
        }

        //清空登录计数
        redisClient.delete(loginCountKey);
        try {
            //userAccountService.updateUserLastLoginInfo(userDto.getPkId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param encodedPassword 加密后的密码
     * @param rawPasswordVo 用户输入的密码
     */
    private boolean doCredentialsMatch(String encodedPassword, String rawPasswordVo) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPasswordVo,encodedPassword);
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode("yuuki");
        System.out.println("encode"+encode);
    }
}
