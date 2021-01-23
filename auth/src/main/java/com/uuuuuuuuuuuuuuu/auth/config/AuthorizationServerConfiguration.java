package com.uuuuuuuuuuuuuuu.auth.config;


import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.ExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.config.annotation.builders.ClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 */
@Slf4j
@Configuration
@EnableAuthorizationServer
@Order(2)
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {


    /**
     * 令牌策略
     */
    @Autowired
    private TokenStore tokenStore;

    /**
     * 客户端详情服务
     */
    @Autowired
    private ClientDetailsService clientDetailsService;

    /**
     * 认证管理器 , 密码模式需要
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * 授权码模式, (存在会导致druidConfigYML配置加载不上，原因未知,应该是其他有注入失败的，则配置也会加载失败)
     */
    /*@Autowired
    private AuthorizationCodeServices authorizationCodeServices;*/

    /**
     * 用户详情服务
     */
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    //@Autowired
    //private DataSource dataSource;

    //  获取配置文件里面当前数据源的的配置信息
    @Value("${spring.datasource.dynamic.datasource.master.url}")
    private String url;
    @Value("${spring.datasource.dynamic.datasource.master.username}")
    private String user;
    @Value("${spring.datasource.dynamic.datasource.master.password}")
    private String password;
    @Value("${spring.datasource.dynamic.datasource.master.driver-class-name}")
    private String driverClass;

    /**
     * DataSource 配置
     * @return
     */
    /*@ConfigurationProperties(prefix = "spring.security.datasource")
    @Bean
    public DataSource dataSource() {
        return new DruidDataSource();
    }*/

    @Bean("oauth2DateSource")
    //@Primary
    public DataSource dataSource() {
        // 配置数据源
        log.info("---初始化AuthorizationServerConfiguration数据源------{},{},{},{}:",url,user,password,driverClass);
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(url);
        datasource.setUsername(user);
        datasource.setPassword(password);
        datasource.setDriverClassName(driverClass);
        datasource.setInitialSize(5);
        datasource.setMinIdle(5);
        datasource.setMaxActive(20);
        datasource.setMaxWait(60000);
        datasource.setTimeBetweenEvictionRunsMillis(60000);
        datasource.setMinEvictableIdleTimeMillis(300000);
        datasource.setValidationQuery("SELECT 1 FROM DUAL");
        datasource.setTestWhileIdle(true);
        datasource.setTestOnBorrow(false);
        datasource.setTestOnReturn(false);
        return datasource;
    }

    /**
     * token仓库
     * @return token存储仓库
     */
    @Bean
    public TokenStore tokenStore(RedisConnectionFactory factory) {
        // redis存储token
        return new RedisTokenStore(factory);
    }

    @Bean
    @DS("oauth2")
    public ClientDetailsService jdbcClientDetailsService() {
        // 基于JDBC实现，需要实现在数据库配置客户端信息以及密码加密方式
        JdbcClientDetailsService detailsService = new JdbcClientDetailsService(dataSource());
        detailsService.setPasswordEncoder(passwordEncoder);
        return detailsService;
    }

    /**
     * 用于扩展token数据
     * @return
     */
    @Bean
    public TokenEnhancer forumTokenEnhancer() {
        return new ForumTokenEnhancer();
    }
    /**
     * 管理令牌的服务
     *
     * @return
     */
    @Bean("myAuthorizationServerTokenServices")
    @Primary
    public AuthorizationServerTokenServices myAuthorizationServerTokenServices() {
        DefaultTokenServices services = new DefaultTokenServices();
        // 客户端详情 服务
        services.setClientDetailsService(clientDetailsService);
        // 是否刷新令牌
        services.setSupportRefreshToken(true);
        // 令牌策略
        services.setTokenStore(tokenStore);
        // 令牌有效期
        services.setAccessTokenValiditySeconds(60*60*2);
        //services.setAccessTokenValiditySeconds(60*5);//五分钟 测试用
        // 刷新令牌时间
        services.setRefreshTokenValiditySeconds(60*60*24*15);

        //services.setReuseRefreshToken(true);

        //令牌增强
        //将增强的token设置到增强链中
        services.setTokenEnhancer(forumTokenEnhancer());

        return services;
    }

    /**
     * 授权码服务
     */
    @Bean
    @Primary
    @DS("oauth2")
    public AuthorizationCodeServices authorizationCodeServices() {
        return new JdbcAuthorizationCodeServices(dataSource());
    }


    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 从数据库中读取客户端配置
        ClientDetailsServiceBuilder<?> clientDetailsServiceBuilder = clients.withClientDetails(jdbcClientDetailsService());

        /*clients
                // 使用本地的方式存储, 为了简易方便测试
                .inMemory()
                // 客户端id
                .withClient("yuuki")
                // 客户端密钥
                .secret(new BCryptPasswordEncoder().encode("yuuki"))
                // 客户端可以请求的资源列表, id
                .resourceIds("yuuki")
                // 该客户端允许的授权类型 authorization_code(授权码模式),password(密码模式),refresh_token(刷新令牌),implicit(简化模式),client_credentials(客户端模式)
                .authorizedGrantTypes("authorization_code", "password", "client_credentials", "implicit", "refresh_token")
                // 允许的授权范围
                .scopes("all")
                // false 授权码模式时, 申请授权码时是跳转到授权页面, true 不跳转页面, 直接获取到授权码
                .autoApprove(false)
                //加上验证回调地址
                .redirectUris("http://www.baidu.com");*/
        //内存
        //InMemoryClientDetailsServiceBuilder clientDetailsServiceBuilder = clients.inMemory();
        /*if (ArrayUtil.isNotEmpty(securityProperties.getOauth().getClients())) {
            for (OAuth2ClientProperties config : securityProperties.getOauth().getClients()) {
                clientDetailsServiceBuilder.withClient(config.getClientId())
                        .secret(passwordEncoder.encode(config.getClientSecret()))
                        .accessTokenValiditySeconds(config.getAccessTokenValiditySeconds())
                        .refreshTokenValiditySeconds(config.getRefreshTokenValiditySecond())
                        // 该客户端允许的授权类型 authorization_code(授权码模式),password(密码模式),refresh_token(刷新令牌),implicit(简化模式),client_credentials(客户端模式)
                        .authorizedGrantTypes("authorization_code", "password", "client_credentials", "implicit", "refresh_token")//OAuth2支持的验证模式
                        .redirectUris(config.getRedirectUri())
                        // 允许的授权范围
                        .scopes("all")
                        // false 授权码模式时, 申请授权码时是跳转到授权页面, true 不跳转页面, 直接获取到授权码
                        .autoApprove(false)
                        // 客户端可以请求的资源列表, id
                        .resourceIds("user-client")
                        .autoApprove(config.getAutoApprove())//设置自动认证
                        .scopes(config.getScope());
            }
        }*/

    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                // 认证管理器
                .authenticationManager(authenticationManager)
                // 授权码管理器
                .authorizationCodeServices(authorizationCodeServices())
                // 令牌管理器
                .tokenServices(myAuthorizationServerTokenServices())
                // 必须注入userDetailsService否则根据refresh_token无法加载用户信息
                //用户详情服务
                .userDetailsService(userDetailsService)
                // 支持GET  POST  请求获取token
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                // 开启刷新token
                .reuseRefreshTokens(true);

        DefaultAccessTokenConverter converter = new DefaultAccessTokenConverter();
        UserAuthenticationConverter userAuthenticationConverter
                = new UserAuthenticationConverter();
        userAuthenticationConverter.setUserDetailsService(userDetailsService);
        converter.setUserTokenConverter(userAuthenticationConverter);
        endpoints.accessTokenConverter(converter);
    }

    /**
     * 认证服务器的安全配置
     *
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .tokenKeyAccess("permitAll()")
                //isAuthenticated():排除anonymous   isFullyAuthenticated():排除anonymous以及remember-me
                .checkTokenAccess("isAuthenticated()")
                //oauth/check_token公开
                //.checkTokenAccess("permitAll()")
                //允许表单认证
                .allowFormAuthenticationForClients();
    }

}
