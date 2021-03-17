package com.uuuuuuuuuuuuuuu.user.controller;

import com.alibaba.fastjson.JSON;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.uuuuuuuuuuuuuuu.feign.feign.SearchFeignClient;
import com.uuuuuuuuuuuuuuu.model.dto.UserDto;
import com.uuuuuuuuuuuuuuu.model.exception.ThrowableUtils;
import com.uuuuuuuuuuuuuuu.model.vo.BlogVO;
import com.uuuuuuuuuuuuuuu.model.vo.Result;
import com.uuuuuuuuuuuuuuu.util.util.MongoDBConvertUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * description: UserController
 * date: 2021/3/16 23:41
 * author: juquansheng
 * version: 1.0 <br>
 */
@Slf4j
@Api(value = "用户信息相关接口", tags = {"用户信息相关接口"})
@RestController
@RequestMapping(value = "user")
public class UserController {



    @ApiOperation(value = "获取用户信息", notes = "获取用户信息", response = String.class)
    @PostMapping("/get")
    public Result get() {
        UserDto userDto = JSON.parseObject(JSON.toJSONString(SecurityContextHolder.getContext().getAuthentication().getPrincipal()), UserDto.class);

        return Result.ok(userDto);
    }


}
