package com.uuuuuuuuuuuuuuu.auth.controller;

import com.uuuuuuuuuuuuuuu.model.constant.Constants;
import com.uuuuuuuuuuuuuuu.model.constant.RedisConstant;
import com.uuuuuuuuuuuuuuu.model.vo.Result;
import com.uuuuuuuuuuuuuuu.redis.utils.RedisUtil;
import com.uuuuuuuuuuuuuuu.util.util.IpUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;


@RestController
//@RefreshScope
@RequestMapping("/auth")
@Api(value = "登录相关接口", tags = {"登录相关接口"})
@Slf4j
public class LoginRestApi {

    @Autowired
    private RedisUtil redisUtil;


    @ApiOperation(value = "用户登录", notes = "用户登录")
    @PostMapping("/login")
    public Result login(HttpServletRequest request,
                        @ApiParam(name = "username", value = "用户名或邮箱或手机号") @RequestParam(name = "username", required = false) String username,
                        @ApiParam(name = "password", value = "密码") @RequestParam(name = "password", required = false) String password,
                        @ApiParam(name = "isRememberMe", value = "是否记住账号密码") @RequestParam(name = "isRememberMe", required = false, defaultValue = "false") Boolean isRememberMe) {

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return Result.failed("账号或密码不能为空");
        }
        String ip = IpUtil.getIpAddr(request);
        String limitCount = redisUtil.get(RedisConstant.LOGIN_LIMIT + RedisConstant.SEGMENTATION + ip);
        if (StringUtils.isNotEmpty(limitCount)) {
            Integer tempLimitCount = Integer.valueOf(limitCount);
            if (tempLimitCount >= Constants.NUM_FIVE) {
                return Result.failed("密码输错次数过多,已被锁定30分钟");
            }
        }

        return Result.ok();
    }

    @ApiOperation(value = "用户信息", notes = "用户信息", response = String.class)
    @GetMapping(value = "/info")
    public Result info(HttpServletRequest request,
                       @ApiParam(name = "token", value = "token令牌", required = false) @RequestParam(name = "token", required = false) String token) {

        return Result.ok();
    }

    @ApiOperation(value = "获取当前用户的菜单", notes = "获取当前用户的菜单", response = String.class)
    @GetMapping(value = "/getMenu")
    public Result getMenu(HttpServletRequest request) {

        return Result.ok();
    }

    @ApiOperation(value = "获取网站名称", notes = "获取网站名称", response = String.class)
    @GetMapping(value = "/getWebSiteName")
    public Result getWebSiteName() {
        return Result.ok();
    }


    @ApiOperation(value = "退出登录", notes = "退出登录", response = String.class)
    @PostMapping(value = "/logout")
    public Result logout() {
        return Result.ok();
    }

    /**
     * 设置登录限制，返回剩余次数
     * 密码错误五次，将会锁定10分钟
     *
     * @param request
     */
    private Integer setLoginCommit(HttpServletRequest request) {
        String ip = IpUtil.getIpAddr(request);
        String count = redisUtil.get(RedisConstant.LOGIN_LIMIT + RedisConstant.SEGMENTATION + ip);
        Integer surplusCount = 5;
        if (StringUtils.isNotEmpty(count)) {
            Integer countTemp = Integer.valueOf(count) + 1;
            surplusCount = surplusCount - countTemp;
            redisUtil.setEx(RedisConstant.LOGIN_LIMIT + RedisConstant.SEGMENTATION + ip, String.valueOf(countTemp), 10, TimeUnit.MINUTES);
        } else {
            surplusCount = surplusCount - 1;
            redisUtil.setEx(RedisConstant.LOGIN_LIMIT + RedisConstant.SEGMENTATION + ip, "1", 30, TimeUnit.MINUTES);
        }
        return surplusCount;
    }

}
