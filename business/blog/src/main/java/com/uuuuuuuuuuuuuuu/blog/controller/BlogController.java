package com.uuuuuuuuuuuuuuu.blog.controller;

import com.uuuuuuuuuuuuuuu.feign.feign.SearchFeignClient;
import com.uuuuuuuuuuuuuuu.model.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * description: ElasticsearchController
 * date: 2021/1/12 15:41
 * author: juquansheng
 * version: 1.0 <br>
 */
@Api(value = "博客相关接口", tags = {"博客相关接口"})
@RestController
@RequestMapping(value = "blog")
public class BlogController {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Resource
    private SearchFeignClient searchFeignClient;

    @ApiOperation(value = "test", notes = "test", response = String.class)
    @GetMapping("/test")
    public String test(String param) throws Exception {
        return "---------------------------:"+param;
    }

    @ApiOperation(value = "查询博客列表", notes = "查询博客列表", response = String.class)
    @GetMapping("/elasticSearchBlog")
    public Result searchBlog(HttpServletRequest request,
                             @RequestParam(required = false) String userName,
                             @RequestParam(name = "currentPage", required = false, defaultValue = "1") Integer
                                     currentPage,
                             @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer
                                     pageSize,
                             @RequestParam(name = "sorter", required = false, defaultValue = "sort") String
                                         sorter) throws Exception {


        return Result.ok();
    }



}
