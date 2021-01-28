package com.uuuuuuuuuuuuuuu.auth.controller;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uuuuuuuuuuuuuuu.auth.config.phone.MobileAuthenticationToken;
import com.uuuuuuuuuuuuuuu.model.constant.PassPortConst;
import com.uuuuuuuuuuuuuuu.model.vo.LoginVo;
import com.uuuuuuuuuuuuuuu.model.vo.MobileLoginVo;
import com.uuuuuuuuuuuuuuu.model.vo.Result;
import com.uuuuuuuuuuuuuuu.redis.utils.RedisClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
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
            if (status == cn.hutool.http.HttpStatus.HTTP_OK) {
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
            //暂时写死
            ClientDetails clientDetails = getClient(CLIENT_ID, CLIENT_SECRET, null);
            TokenRequest tokenRequest = new TokenRequest(new HashMap<>(10), CLIENT_ID, clientDetails.getScope(), "customer");
            OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);
            Authentication authentication = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);
            OAuth2AccessToken oAuth2AccessToken =  myAuthorizationServerTokenServices.createAccessToken(oAuth2Authentication);
            oAuth2Authentication.setAuthenticated(true);
            writerObj(response, oAuth2AccessToken);
        } catch (BadCredentialsException | InternalAuthenticationServiceException e) {
            exceptionHandler(response, badCredenbtialsMsg);
            e.printStackTrace();
        } catch (Exception e) {
            exceptionHandler(response, e);
        }
    }
}
