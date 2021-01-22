package com.uuuuuuuuuuuuuuu.auth.config;

import com.uuuuuuuuuuuuuuu.auth.config.account.AccountSecurityConfigurer;
import com.uuuuuuuuuuuuuuu.auth.config.phone.MobileSecurityConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;

/**
 * 配置哪些请求被拦截，哪些请求可以匿名访问
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Resource(name = "userDetailsService")
    private UserDetailsService userDetailsService;

    @Autowired
    private MobileSecurityConfigurer mobileSecurityConfigurer;

    @Autowired
    private AccountSecurityConfigurer accountSecurityConfigurer;

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder());

        // 在内存中创建用户,用于登录
        /*auth.inMemoryAuthentication()
                .withUser("user").password(bCryptPasswordEncoder().encode("123456")).roles("USER")
                .and()
                .withUser("admin").password(bCryptPasswordEncoder().encode("654321")).roles("ADMIN");*/
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //因为SpringSecurity使用X-Frame-Options防止网页被Frame。所以需要关闭为了让后端的接口管理的swagger页面正常显示
        http.headers().frameOptions().disable();

        http.apply(accountSecurityConfigurer).and()//账号密码登录
                //手机登录配置
                .apply(mobileSecurityConfigurer).and()
                //新加入,允许跨域
                .cors()
                .and()
                // 由于使用的是JWT，我们这里不需要csrf
                .csrf().disable()
                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                //.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // 允许对于网站静态资源的无授权访问
                .antMatchers(
                        "/swagger-ui.html",
                        "/swagger-ui/*",
                        "/swagger-resources/**",
                        "/v2/api-docs",
                        "/v3/api-docs",
                        "/webjars/**",
                        "/actuator/**",
                        "/test/**",
                        "/index",
                        "/oauth/**",
                        "/druid/**"
                ).permitAll()
                // 对于获取token的RestApi要允许匿名访问
                .antMatchers("/auth/**",
                        "/creatCode/**",
                        "/file/**"
                ).permitAll()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();

        // 禁用缓存
        http.headers().cacheControl();



        /*http.formLogin().
                and().authorizeRequests()
                //.antMatchers("/index.html").permitAll()
                .antMatchers( "/swagger-ui.html",
                        "/v3/api-docs",
                        "/swagger-resources",
                        "/swagger-resources/configuration/ui",
                        "/swagger-resources/configuration/security",
                        "/index",
                        "/v1/oauth/**",
                        "/v1/register",
                        "/actuator/**",
                        "/test/**",
                        "/v3/**",
                        "/swagger-ui/**").permitAll()
                .and()
                .authorizeRequests().
                anyRequest().
                authenticated();*/
    }

    /*@Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(
                        "/swagger-ui.html",
                        "/v3/api-docs",
                        "/swagger-resources",
                        "/swagger-resources/configuration/ui",
                        "/swagger-resources/configuration/security"
                );
    }*/

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
