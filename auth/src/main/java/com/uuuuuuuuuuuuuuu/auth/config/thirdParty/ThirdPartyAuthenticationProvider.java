package com.uuuuuuuuuuuuuuu.auth.config.thirdParty;


import com.uuuuuuuuuuuuuuu.auth.config.MyUserDetailsService;
import com.uuuuuuuuuuuuuuu.redis.utils.RedisClient;
import lombok.Builder;
import lombok.Data;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;


@Data
@Builder
public class ThirdPartyAuthenticationProvider implements AuthenticationProvider {

    private MyUserDetailsService userDetailsService;

    private RedisClient redisClient;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //认证逻辑 根据第三方标识查询用户是否存在
        AuthUser authUser = (AuthUser) authentication.getPrincipal();
        String source = (String)authentication.getDetails();
        //不存在插入信用，存在直接返回
        UserDetails userDetails = userDetailsService.loadUserByThirdParty(authUser,source);
        //账号校验
        this.accountStatusCheck(userDetails);
        ThirdPartyAuthenticationToken token = new ThirdPartyAuthenticationToken(userDetails, userDetails.getAuthorities());
        token.setDetails(authentication.getDetails());
        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ThirdPartyAuthenticationToken.class.isAssignableFrom(authentication);
    }

    /**
     * 账号可用性校验
     */
    private void accountStatusCheck(UserDetails userDetails) {
        if (!userDetails.isEnabled()) {
            throw new RuntimeException("账号不可用");
        }
    }
}
