package com.uuuuuuuuuuuuuuu.auth.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * description: TestController
 * date: 2020/11/13 16:01
 * author: juquansheng
 * version: 1.0 <br>
 */
@Api(value = "auth相关接口test", tags = {"auth相关接口test"})
@RestController
@RequestMapping("/test")
public class TestController {
    @Value("${fdfs.so-timeout}")
    private String soTimeout;

    @ApiOperation(value = "test", notes = "test", response = String.class)
    @RequestMapping(value = "/test",method = RequestMethod.GET)
    @ResponseBody
    public String test() {
        try {
            String result = "success";
            return result;
        } catch (Exception e) {
            return "test";
        }
    }

    public static void main(String[] args) {
        String[] a = new String[1];
        System.out.println(a[0]);
    }
}
