package com.uuuuuuuuuuuuuuu.auth.config.phone;


import com.uuuuuuuuuuuuuuu.auth.config.MyUserDetailsService;
import com.uuuuuuuuuuuuuuu.redis.utils.RedisClient;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;



@Data
@Builder
public class MobileAuthenticationProvider implements AuthenticationProvider {

    private MyUserDetailsService userDetailsService;

    private RedisClient redisClient;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //认证逻辑
        String phone = (String) authentication.getPrincipal();
        UserDetails userDetails = userDetailsService.loadUserByPhone(phone);
        //账号校验
        this.accountStatusCheck(userDetails);
        MobileAuthenticationToken token = new MobileAuthenticationToken(userDetails, userDetails.getAuthorities());
        token.setDetails(authentication.getDetails());
        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return MobileAuthenticationToken.class.isAssignableFrom(authentication);
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
