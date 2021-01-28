package com.uuuuuuuuuuuuuuu.auth.controller;


import com.uuuuuuuuuuuuuuu.core.service.UserPassportService;
import com.uuuuuuuuuuuuuuu.model.constant.PassPortConst;
import com.uuuuuuuuuuuuuuu.model.vo.RegisterVo;
import com.uuuuuuuuuuuuuuu.model.vo.Result;
import com.uuuuuuuuuuuuuuu.redis.utils.RedisClient;
import com.uuuuuuuuuuuuuuu.util.util.PatternMatcherUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname RegisterCtl
 * @description 注册控制器
 * @Author chuanfang
 * @Date 2020/7/6 12:21
 * @Version 1.0
 */
@Api(tags = "注册")
@RestController
@Slf4j
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserPassportService userPassportService;

    @Autowired
    private RedisClient redisClient;

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation("注册")
    public Result register(@RequestBody RegisterVo registerVo) throws Exception {

        String code = redisClient
                .get(PassPortConst.VERIFY_CODE_KEY + registerVo.getType() + ":" + registerVo.getAccount());
        if (!registerVo.getVerifyCode().equals(code)) {
            return Result.failed("验证码错误");
        }
        //验证密码是否合法 暂时不用
        String password = registerVo.getPassword();
        if (!PatternMatcherUtils.passwordNormal(password)){
            return Result.failed("密码不合法");
        }
        try {
            //密码加密
            password = passwordEncoder.encode(password);
            registerVo.setPassword(password);
            userPassportService.register(registerVo);
            return Result.ok("注册成功");
        } catch (Exception e) {
            log.error("注册失败", e);
            return Result.failed("注册失败:"+e.getMessage());
        }
    }
}
