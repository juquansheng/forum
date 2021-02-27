package com.uuuuuuuuuuuuuuu.auth.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uuuuuuuuuuuuuuu.auth.config.AuthStateRedisCache;
import com.uuuuuuuuuuuuuuu.auth.config.phone.MobileAuthenticationToken;
import com.uuuuuuuuuuuuuuu.auth.config.thirdParty.ThirdPartyAuthenticationToken;
import com.uuuuuuuuuuuuuuu.model.constant.Constants;
import com.uuuuuuuuuuuuuuu.model.constant.PassPortConst;
import com.uuuuuuuuuuuuuuu.model.vo.LoginVo;
import com.uuuuuuuuuuuuuuu.model.vo.MobileLoginVo;
import com.uuuuuuuuuuuuuuu.model.vo.Result;
import com.uuuuuuuuuuuuuuu.redis.utils.RedisClient;
import com.xkcoding.http.config.HttpConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.enums.scope.*;
import me.zhyd.oauth.exception.AuthException;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthToken;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.*;
import me.zhyd.oauth.utils.AuthScopeUtils;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.client.InMemoryClientDetailsService;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @description OAuth2相关操作
 */
@Api(value = "OAuth2相关操作",tags = "OAuth2相关操作")
@Slf4j
@RestController
@RequestMapping("/oauth")
public class Oauth2Controller {
    @Resource
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthorizationServerTokenServices myAuthorizationServerTokenServices;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private Map<String, ClientDetailsService> clientDetailsServiceMap;

    @Autowired
    private RedisClient redisUtil;

    @Autowired
    private AuthStateRedisCache stateRedisCache;

    //后续可改为配置类
    @Value("${third-party.qq.appId}")
    private String appIdQQ;
    @Value("${third-party.qq.appKey}")
    private String appKeyQQ;
    @Value("${third-party.qq.redirectUri}")
    private String redirectUri;

    @Value(value = "${url.webSite}")
    private String webSiteUrl;
    /**
     * 登录ip是否被锁定    一小时 redisKey 前缀
     */
    private static final String IP_IS_LOCK = "ip_is_lock_";
    //暂时写死
    static final String CLIENT_ID = "yuuki";
    static final String CLIENT_SECRET = "yuuki";



    @ApiOperation(value = "账号密码登录")
    @PostMapping("/account/login")
    public void login(@RequestBody LoginVo loginVo,
                                 HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (StringUtils.isEmpty(loginVo.getAccount())) {
            exceptionHandler(response, new UnapprovedClientAuthenticationException("账号为空"));
            return;
        }
        if (StringUtils.isEmpty(loginVo.getPassword())) {
            exceptionHandler(response, new UnapprovedClientAuthenticationException("密码为空"));
            return;
        }
        AbstractAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginVo.getAccount(),loginVo.getPassword());
        writerToken(request, response, token, "凭证错误",1L);
    }


    @ApiOperation(value = "手机号登录")
    @PostMapping(value = "/phone/login")
    public void phone(@RequestBody MobileLoginVo mobileLoginVo,
                      HttpServletRequest request, HttpServletResponse response) throws IOException {
        //短信验证码验证
        String verifyCode= (String) redisUtil
                .get(PassPortConst.VERIFY_CODE_LOGIN + mobileLoginVo.getPhone());
        if (StrUtil.isBlank(verifyCode) || !verifyCode.equals(mobileLoginVo.getCode())) {
            log.error("无效验证码");
            exceptionHandler(response, new RuntimeException("无效验证码"));
            return;
        }
        MobileAuthenticationToken token = new MobileAuthenticationToken(mobileLoginVo.getPhone(),mobileLoginVo.getCode());
        writerToken(request, response, token, "凭证错误",1L);
    }

    /**
     * 当前登陆用户信息
     * security获取当前登录用户的方法是SecurityContextHolder.getContext().getAuthentication()
     * 这里的实现类是org.springframework.security.login.provider.OAuth2Authentication
     *
     * @return
     */
    @ApiOperation(value = "当前登陆用户信息")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public Object getCurrentUserDetail(String access_token) {
        if(tokenStore.readAccessToken(access_token)!=null&&!tokenStore.readAccessToken(access_token).isExpired()) {
            return Result.ok(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        }else {
            return Result.failed("token无效");
        }
    }
    @ApiOperation(value = "登出功能")
    @RequestMapping(value = "/oauth/logout", method = RequestMethod.POST)
    @ResponseBody
    public Object logout(@RequestHeader("Authorization") String authorization) {
        String accessToken = authorization.split(" ")[1];
        try {
            removeToken(accessToken);
        } catch (Exception e) {
            Result.failed("登出失败:"+e.getMessage());
        }
        SecurityContextHolder.clearContext();
        return Result.ok(null,"登出成功");
    }

    @ApiOperation(value = "access_token刷新token")
    @PostMapping(value = "/refresh/token", params = "access_token")
    public void getTokenInfoByToken(String access_token, HttpServletRequest request, HttpServletResponse response) {
        // 拿到当前用户信息
        try {
            OAuth2AccessToken accessToken = tokenStore.readAccessToken(access_token);
            //这里采用内存客户端
            ClientDetailsService clientDetailsService = clientDetailsServiceMap.get("clientDetailsService");

            ClientDetails clientDetails = clientDetailsService
                    .loadClientByClientId(CLIENT_ID);

            OAuth2RequestFactory requestFactory = new DefaultOAuth2RequestFactory(clientDetailsService);

            RefreshTokenGranter refreshTokenGranter = new RefreshTokenGranter(myAuthorizationServerTokenServices,
                    clientDetailsService, requestFactory);

            Map<String, String> map = new HashMap<>();
            map.put("grant_type", "refresh_token");
            map.put("refresh_token", accessToken.getRefreshToken().getValue());
            TokenRequest tokenRequest = new TokenRequest(map, clientDetails.getClientId(),
                    clientDetails.getScope(), "refresh_token");

            OAuth2AccessToken oAuth2AccessToken = refreshTokenGranter.grant("refresh_token", tokenRequest);

            tokenStore.removeAccessToken(accessToken);

            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(Result.ok(oAuth2AccessToken,"刷新成功")));
            response.getWriter().flush();
            response.getWriter().close();
        } catch (Exception e) {
            response.setStatus(HttpStatus.OK.value());
            response.setContentType("application/json;charset=UTF-8");
            Map<String, String> rsp = new HashMap<>();
            rsp.put("code", HttpStatus.UNAUTHORIZED.value() + "");
            rsp.put("msg", "access_token无效或refresh_token超时");
            rsp.put("data", null);

            try {
                response.getWriter().write(objectMapper.writeValueAsString(rsp));
                response.getWriter().flush();
                response.getWriter().close();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

    }

    @ApiOperation(value = "refresh_token刷新token")
    @PostMapping(value = "/refresh/refresh_token", params = "refresh_token")
    public void getTokenInfoByRefreshToken(String refresh_token, HttpServletRequest request, HttpServletResponse response) {
        // 拿到当前用户信息
        try {
            OAuth2RefreshToken refreshToken = tokenStore.readRefreshToken(refresh_token);
            //这里采用内存客户端
            ClientDetailsService clientDetailsService = clientDetailsServiceMap.get("clientDetailsService");

            ClientDetails clientDetails = clientDetailsService
                    .loadClientByClientId(CLIENT_ID);

            OAuth2RequestFactory requestFactory = new DefaultOAuth2RequestFactory(clientDetailsService);

            RefreshTokenGranter refreshTokenGranter = new RefreshTokenGranter(myAuthorizationServerTokenServices,
                    clientDetailsService, requestFactory);

            Map<String, String> map = new HashMap<>();
            map.put("grant_type", "refresh_token");
            map.put("refresh_token", refreshToken.getValue());
            TokenRequest tokenRequest = new TokenRequest(map, clientDetails.getClientId(),
                    clientDetails.getScope(), "refresh_token");

            OAuth2AccessToken oAuth2AccessToken = refreshTokenGranter.grant("refresh_token", tokenRequest);


            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(Result.ok(oAuth2AccessToken,"刷新成功")));
            response.getWriter().flush();
            response.getWriter().close();
        } catch (Exception e) {
            response.setStatus(HttpStatus.OK.value());
            response.setContentType("application/json;charset=UTF-8");
            Map<String, String> rsp = new HashMap<>();
            rsp.put("code", HttpStatus.UNAUTHORIZED.value() + "");
            rsp.put("msg", "refresh_token超时");
            rsp.put("data", null);

            try {
                response.getWriter().write(objectMapper.writeValueAsString(rsp));
                response.getWriter().flush();
                response.getWriter().close();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

    }

    /**
     * 移除access_token和refresh_token
     *
     * @param access_token
     */
    @ApiOperation(value = "移除token")
    @PostMapping(value = "/remove/token", params = "access_token")
    public void removeToken(String access_token) {
        OAuth2AccessToken accessToken = tokenStore.readAccessToken(access_token);
        if (accessToken != null) {
            // 移除access_token
            tokenStore.removeAccessToken(accessToken);

            // 移除refresh_token
            if (accessToken.getRefreshToken() != null) {
                tokenStore.removeRefreshToken(accessToken.getRefreshToken());
            }
        }else {
            throw new RuntimeException("登出失败");
        }
    }


    @ApiOperation(value = "第三方登录")
    @RequestMapping("/render/{source}")
    @ResponseBody
    public void renderAuth(@PathVariable("source") String source, HttpServletResponse response) throws IOException {
        log.info("进入render：" + source);
        AuthRequest authRequest = getAuthRequest(source);
        String authorizeUrl = authRequest.authorize(AuthStateUtils.createState());
        log.info(authorizeUrl);
        response.sendRedirect(authorizeUrl);
    }



    /**
     * oauth平台中配置的授权回调地址，以本项目为例，在创建github授权应用时的回调地址应为：http://127.0.0.1:8443/oauth/callback/github
     */
    @ApiOperation(value = "回调地址")
    @RequestMapping("/callback/{source}")
    public void callback(@PathVariable("source") String source, AuthCallback callback,
                              HttpServletRequest request, HttpServletResponse httpServletResponse) throws IOException {
        log.info("进入callback：" + source + " callback params：" + JSONObject.toJSONString(callback));
        AuthRequest authRequest = getAuthRequest(source);
        AuthResponse<AuthUser> loginResult = authRequest.login(callback);
        log.info(JSONObject.toJSONString(loginResult));

        if (!loginResult.ok()) {
            log.error(loginResult.getMsg());
            Map<String, Object> map = new HashMap<>(1);
            map.put("errorMsg", loginResult.getMsg());
            // 跳转到500错误页面
            httpServletResponse.sendRedirect(webSiteUrl + Constants.STR_500);
            return;
        }
        //登录成功后流程
        AbstractAuthenticationToken token = new ThirdPartyAuthenticationToken(loginResult.getData(),source);
        OAuth2AccessToken oAuth2AccessToken = getOAuth2AccessToken(token);
        //writerToken(request, response, token, "凭证错误",1L);
        httpServletResponse.sendRedirect(webSiteUrl + "?access_token=" + oAuth2AccessToken.getValue() + "?refresh_token=" + oAuth2AccessToken.getRefreshToken());
    }

    @RequestMapping("/revoke/{source}/{uuid}")
    @ResponseBody
    public Result revokeAuth(@PathVariable("source") String source, @PathVariable("uuid") String uuid) throws IOException {
        AuthRequest authRequest = getAuthRequest(source.toLowerCase());

        AuthUser user = null;
        if (null == user) {
            return Result.failed("用户不存在");
        }
        AuthResponse<AuthToken> response = null;
        try {
            response = authRequest.revoke(user.getToken());
            if (response.ok()) {
                //userService.remove(user.getUuid());
                return Result.ok("用户 [" + user.getUsername() + "] 的 授权状态 已收回！");
            }
            return Result.failed("用户 [" + user.getUsername() + "] 的 授权状态 收回失败！" + response.getMsg());
        } catch (AuthException e) {
            return Result.failed(e.getErrorMsg());
        }
    }

    @RequestMapping("/refresh/{source}/{uuid}")
    @ResponseBody
    public Result refreshAuth(@PathVariable("source") String source, @PathVariable("uuid") String uuid) {
        AuthRequest authRequest = getAuthRequest(source.toLowerCase());

        AuthUser user = null;
        if (null == user) {
            return Result.failed("用户不存在");
        }
        AuthResponse<AuthToken> response = null;
        try {
            response = authRequest.refresh(user.getToken());
            if (response.ok()) {
                user.setToken(response.getData());
                //userService.save(user);
                return Result.ok("用户 [" + user.getUsername() + "] 的 access token 已刷新！新的 accessToken: " + response.getData().getAccessToken());
            }
            return Result.failed("用户 [" + user.getUsername() + "] 的 access token 刷新失败！" + response.getMsg());
        } catch (AuthException e) {
            return Result.failed(e.getErrorMsg());
        }
    }

    /**
     * 根据具体的授权来源，获取授权请求工具类
     *
     * @param source
     * @return
     */
    private AuthRequest getAuthRequest(String source) {
        AuthRequest authRequest = null;
        switch (source.toLowerCase()) {
            case "dingtalk":
                authRequest = new AuthDingTalkRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri("http://localhost:8443/oauth/callback/dingtalk")
                        .build());
                break;
            case "baidu":
                authRequest = new AuthBaiduRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri("http://localhost:8443/oauth/callback/baidu")
                        .scopes(Arrays.asList(
                                AuthBaiduScope.BASIC.getScope(),
                                AuthBaiduScope.SUPER_MSG.getScope(),
                                AuthBaiduScope.NETDISK.getScope()
                        ))
//                        .clientId("")
//                        .clientSecret("")
//                        .redirectUri("http://localhost:9001/oauth/baidu/callback")
                        .build());
                break;
            case "github":
                authRequest = new AuthGithubRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri("http://localhost:8443/oauth/callback/github")
                        .scopes(AuthScopeUtils.getScopes(AuthGithubScope.values()))
                        // 针对国外平台配置代理
                        .httpConfig(HttpConfig.builder()
                                .timeout(15000)
                                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 10080)))
                                .build())
                        .build(), stateRedisCache);
                break;
            case "gitee":
                authRequest = new AuthGiteeRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri("http://127.0.0.1:8443/oauth/callback/gitee")
                        .scopes(AuthScopeUtils.getScopes(AuthGiteeScope.values()))
                        .build(), stateRedisCache);
                break;
            case "weibo":
                authRequest = new AuthWeiboRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri("http://dblog-web.zhyd.me/oauth/callback/weibo")
                        .scopes(Arrays.asList(
                                AuthWeiboScope.EMAIL.getScope(),
                                AuthWeiboScope.FRIENDSHIPS_GROUPS_READ.getScope(),
                                AuthWeiboScope.STATUSES_TO_ME_READ.getScope()
                        ))
                        .build());
                break;
            case "coding":
                authRequest = new AuthCodingRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri("http://dblog-web.zhyd.me/oauth/callback/coding")
                        .codingGroupName("")
                        .scopes(Arrays.asList(
                                AuthCodingScope.USER.getScope(),
                                AuthCodingScope.USER_EMAIL.getScope(),
                                AuthCodingScope.USER_PHONE.getScope()
                        ))
                        .build());
                break;
            case "oschina":
                authRequest = new AuthOschinaRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri("http://localhost:8443/oauth/callback/oschina")
                        .build());
                break;
            case "alipay":
                // 支付宝在创建回调地址时，不允许使用localhost或者127.0.0.1，所以这儿的回调地址使用的局域网内的ip
                authRequest = new AuthAlipayRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .alipayPublicKey("")
                        .redirectUri("https://www.zhyd.me/oauth/callback/alipay")
                        .build());
                break;
            case "qq":
                authRequest = new AuthQqRequest(AuthConfig.builder()
                        .clientId(appIdQQ)
                        .clientSecret(appKeyQQ)
                        .redirectUri(redirectUri)
                        .build());
                break;
            case "wechat_open":
                authRequest = new AuthWeChatOpenRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri("http://www.zhyd.me/oauth/callback/wechat")
                        .build());
                break;
            case "csdn":
                authRequest = new AuthCsdnRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri("http://dblog-web.zhyd.me/oauth/callback/csdn")
                        .build());
                break;
            case "taobao":
                authRequest = new AuthTaobaoRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri("http://dblog-web.zhyd.me/oauth/callback/taobao")
                        .build());
                break;
            case "google":
                authRequest = new AuthGoogleRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri("http://localhost:8443/oauth/callback/google")
                        .scopes(AuthScopeUtils.getScopes(AuthGoogleScope.USER_EMAIL, AuthGoogleScope.USER_PROFILE, AuthGoogleScope.USER_OPENID))
                        // 针对国外平台配置代理
                        .httpConfig(HttpConfig.builder()
                                .timeout(15000)
                                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 10080)))
                                .build())
                        .build());
                break;
            case "facebook":
                authRequest = new AuthFacebookRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri("https://justauth.cn/oauth/callback/facebook")
                        .scopes(AuthScopeUtils.getScopes(AuthFacebookScope.values()))
                        // 针对国外平台配置代理
                        .httpConfig(HttpConfig.builder()
                                .timeout(15000)
                                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 10080)))
                                .build())
                        .build());
                break;
            case "douyin":
                authRequest = new AuthDouyinRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri("http://dblog-web.zhyd.me/oauth/callback/douyin")
                        .build());
                break;
            case "linkedin":
                authRequest = new AuthLinkedinRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri("http://localhost:8443/oauth/callback/linkedin")
                        .scopes(null)
                        .build());
                break;
            case "microsoft":
                authRequest = new AuthMicrosoftRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri("http://localhost:8443/oauth/callback/microsoft")
                        .scopes(Arrays.asList(
                                AuthMicrosoftScope.USER_READ.getScope(),
                                AuthMicrosoftScope.USER_READWRITE.getScope(),
                                AuthMicrosoftScope.USER_READBASIC_ALL.getScope(),
                                AuthMicrosoftScope.USER_READ_ALL.getScope(),
                                AuthMicrosoftScope.USER_READWRITE_ALL.getScope(),
                                AuthMicrosoftScope.USER_INVITE_ALL.getScope(),
                                AuthMicrosoftScope.USER_EXPORT_ALL.getScope(),
                                AuthMicrosoftScope.USER_MANAGEIDENTITIES_ALL.getScope(),
                                AuthMicrosoftScope.FILES_READ.getScope()
                        ))
                        .build());
                break;
            case "mi":
                authRequest = new AuthMiRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri("http://dblog-web.zhyd.me/oauth/callback/mi")
                        .build());
                break;
            case "toutiao":
                authRequest = new AuthToutiaoRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri("http://dblog-web.zhyd.me/oauth/callback/toutiao")
                        .build());
                break;
            case "teambition":
                authRequest = new AuthTeambitionRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri("http://127.0.0.1:8443/oauth/callback/teambition")
                        .build());
                break;
            case "pinterest":
                authRequest = new AuthPinterestRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri("https://eadmin.innodev.com.cn/oauth/callback/pinterest")
                        // 针对国外平台配置代理
                        .httpConfig(HttpConfig.builder()
                                .timeout(15000)
                                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 10080)))
                                .build())
                        .build());
                break;
            case "renren":
                authRequest = new AuthRenrenRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri("http://127.0.0.1:8443/oauth/callback/teambition")
                        .build());
                break;
            case "stack_overflow":
                authRequest = new AuthStackOverflowRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("((")
                        .redirectUri("http://localhost:8443/oauth/callback/stack_overflow")
                        .stackOverflowKey("")
                        .build());
                break;
            case "huawei":
                authRequest = new AuthHuaweiRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri("http://127.0.0.1:8443/oauth/callback/huawei")
                        .scopes(Arrays.asList(
                                AuthHuaweiScope.BASE_PROFILE.getScope(),
                                AuthHuaweiScope.MOBILE_NUMBER.getScope(),
                                AuthHuaweiScope.ACCOUNTLIST.getScope(),
                                AuthHuaweiScope.SCOPE_DRIVE_FILE.getScope(),
                                AuthHuaweiScope.SCOPE_DRIVE_APPDATA.getScope()
                        ))
                        .build());
                break;
            case "wechat_enterprise":
                authRequest = new AuthWeChatEnterpriseQrcodeRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri("http://justauth.cn/oauth/callback/wechat_enterprise")
                        .agentId("1000003")
                        .build());
                break;
            case "kujiale":
                authRequest = new AuthKujialeRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri("http://dblog-web.zhyd.me/oauth/callback/kujiale")
                        .build());
                break;
            case "gitlab":
                authRequest = new AuthGitlabRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri("http://localhost:8443/oauth/callback/gitlab")
                        .scopes(AuthScopeUtils.getScopes(AuthGitlabScope.values()))
                        .build());
                break;
            case "meituan":
                authRequest = new AuthMeituanRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri("http://localhost:8443/oauth/callback/meituan")
                        .build());
                break;
            case "eleme":
                authRequest = new AuthElemeRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri("http://dblog-web.zhyd.me/oauth/callback/eleme")
                        .build());
                break;
//            case "mygitlab":
//                authRequest = new AuthMyGitlabRequest(AuthConfig.builder()
//                        .clientId("")
//                        .clientSecret("")
//                        .redirectUri("http://127.0.0.1:8443/oauth/callback/mygitlab")
//                        .build());
//                break;
            case "twitter":
                authRequest = new AuthTwitterRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri("https://threelogin.31huiyi.com/oauth/callback/twitter")
                        // 针对国外平台配置代理
                        .httpConfig(HttpConfig.builder()
                                .timeout(15000)
                                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 10080)))
                                .build())
                        .build());
                break;
            case "wechat_mp":
                authRequest = new AuthWeChatMpRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri("")
                        .build());
                break;
            case "aliyun":
                authRequest = new AuthAliyunRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri("http://localhost:8443/oauth/callback/aliyun")
                        .build());
                break;
            case "xmly":
                authRequest = new AuthXmlyRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri("http://localhost:8443/oauth/callback/xmly")
                        .build());
                break;
            case "feishu":
                authRequest = new AuthFeishuRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri("http://localhost:8443/oauth/callback/feishu")
                        .build());
                break;
            default:
                break;
        }
        if (null == authRequest) {
            throw new AuthException("未获取到有效的Auth配置");
        }
        return authRequest;
    }






    private void exceptionHandler(HttpServletResponse response, Exception e) throws IOException {
        log.error("exceptionHandler-error:", e);
        exceptionHandler(response, e.getMessage());
    }

    private void exceptionHandler(HttpServletResponse response, String msg) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        writerObj(response, msg);
    }

    private void writerObj(HttpServletResponse response, Object obj) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        int status = response.getStatus();
        response.setStatus(HttpStatus.OK.value());
        try (
                Writer writer = response.getWriter()
        )
        {
            if (status == HttpStatus.OK.value()) {
                writer.write(objectMapper.writeValueAsString(Result.ok(obj,"登录成功")));
            }else {
                writer.write(objectMapper.writeValueAsString(Result.loginFailed(obj.toString())));
            }
            writer.flush();
        }
    }

    private ClientDetails getClient(String clientId, String clientSecret, InMemoryClientDetailsService clientDetailsService) {
        ClientDetailsService detailsService = null;
        if (clientDetailsService == null) {
            //动态代理只能用接口接收 jdk
           detailsService = clientDetailsServiceMap.get("clientDetailsService");
        }
        assert detailsService != null;
        ClientDetails clientDetails = detailsService.loadClientByClientId(clientId);
        if (clientDetails == null) {
            throw new UnapprovedClientAuthenticationException("clientId对应的信息不存在");
        } else if (!passwordEncoder.matches(clientSecret, clientDetails.getClientSecret())) {
            throw new UnapprovedClientAuthenticationException("clientSecret不匹配");
        }
        return clientDetails;
    }

    private void writerToken(HttpServletRequest request, HttpServletResponse response, AbstractAuthenticationToken token
            , String badCredenbtialsMsg, Long userId) throws IOException {
        try {
            OAuth2AccessToken oAuth2AccessToken = getOAuth2AccessToken(token);
            writerObj(response, oAuth2AccessToken);
        } catch (BadCredentialsException | InternalAuthenticationServiceException e) {
            exceptionHandler(response, badCredenbtialsMsg);
            e.printStackTrace();
        } catch (Exception e) {
            exceptionHandler(response, e);
        }
    }

    private OAuth2AccessToken getOAuth2AccessToken(AbstractAuthenticationToken token){
        //暂时写死
        ClientDetails clientDetails = getClient(CLIENT_ID, CLIENT_SECRET, null);
        TokenRequest tokenRequest = new TokenRequest(new HashMap<>(10), CLIENT_ID, clientDetails.getScope(), "customer");
        OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);
        OAuth2AccessToken oAuth2AccessToken =  myAuthorizationServerTokenServices.createAccessToken(oAuth2Authentication);
        oAuth2Authentication.setAuthenticated(true);
        return oAuth2AccessToken;
    }
}
