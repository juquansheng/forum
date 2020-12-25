package com.wengegroup.auth.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * description: TestController
 * date: 2020/11/13 16:01
 * author: juquansheng
 * version: 1.0 <br>
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @Value("${fdfs.so-timeout}")
    private String soTimeout;

    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public String test() {
        try {
            return soTimeout;
        } catch (Exception e) {
            return "test";
        }
    }
}
