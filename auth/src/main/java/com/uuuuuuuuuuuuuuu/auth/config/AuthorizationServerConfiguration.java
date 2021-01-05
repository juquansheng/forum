package com.uuuuuuuuuuuuuuu.auth.config;

import cn.hutool.core.util.ArrayUtil;

import com.uuuuuuuuuuuuuuu.auth.properties.OAuth2ClientProperties;
import com.uuuuuuuuuuuuuuu.auth.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.ClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;

/**
 */
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
     * 授权码模式, 需要
     */
    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;

    /**
     * 用户详情服务
     */
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DataSource dataSource;


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
    public ClientDetailsService jdbcClientDetailsService() {
        // 基于JDBC实现，需要实现在数据库配置客户端信息以及密码加密方式
        JdbcClientDetailsService detailsService = new JdbcClientDetailsService(dataSource);
        detailsService.setPasswordEncoder(passwordEncoder);
        return detailsService;
    }

    /**
     *
     */
    @Bean
    public DefaultAccessTokenConverter accessTokenConverter() {
        DefaultAccessTokenConverter converter = new DefaultAccessTokenConverter();
        return converter;
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
        return services;
    }


    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 从数据库中读取客户端配置
        ClientDetailsServiceBuilder<?> clientDetailsServiceBuilder = clients.withClientDetails(jdbcClientDetailsService());
        //InMemoryClientDetailsServiceBuilder clientDetailsServiceBuilder = clients.inMemory();

        if (ArrayUtil.isNotEmpty(securityProperties.getOauth().getClients())) {
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
        }

    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                // 认证管理器
                .authenticationManager(authenticationManager)
                // 授权码管理器
                .authorizationCodeServices(authorizationCodeServices)
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
                //允许表单认证
                .allowFormAuthenticationForClients();

        /*security
                //oauth/token_key是公开
                .tokenKeyAccess("permitAll()")
                //oauth/check_token公开
                .checkTokenAccess("permitAll()")
                //表单认证（申请令牌）
                .allowFormAuthenticationForClients()
        ;*/
    }

}
