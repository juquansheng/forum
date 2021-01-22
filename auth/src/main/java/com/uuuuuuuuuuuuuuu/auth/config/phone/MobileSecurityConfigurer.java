

package com.uuuuuuuuuuuuuuu.auth.config.phone;




import com.uuuuuuuuuuuuuuu.auth.config.MyUserDetailsService;
import com.uuuuuuuuuuuuuuu.auth.config.phone.MobileAuthenticationProvider;
import com.uuuuuuuuuuuuuuu.redis.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;

/**
 */
@Configuration
public class MobileSecurityConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

	@Autowired
	private MyUserDetailsService myUserDetailsService;

	@Autowired
	private RedisUtil redisUtil;

	@Override
	public void configure(HttpSecurity http) {
		MobileAuthenticationProvider mobileAuthenticationProvider = MobileAuthenticationProvider
				.builder()
				.redisUtil(redisUtil)
				.userDetailsService(myUserDetailsService)
				.build();
		http.authenticationProvider(mobileAuthenticationProvider);
	}

}
