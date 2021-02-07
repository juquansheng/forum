package com.uuuuuuuuuuuuuuu.blog.controller;

import com.uuuuuuuuuuuuuuu.feign.feign.SearchFeignClient;
import com.uuuuuuuuuuuuuuu.model.exception.ThrowableUtils;
import com.uuuuuuuuuuuuuuu.model.vo.BlogVO;
import com.uuuuuuuuuuuuuuu.model.vo.Result;
import com.uuuuuuuuuuuuuuu.util.util.MongoDBConvertUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
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


    @ApiOperation(value = "新增博客", notes = "新增博客", response = String.class)
    @PostMapping("/add")
    public Result add(@Validated @RequestBody BlogVO blogVO, BindingResult result) throws Exception {
        // 参数校验
        ThrowableUtils.checkParamArgument(result);
        mongoTemplate.getCollection("blog").insertOne(MongoDBConvertUtils.toDocument(blogVO));
        return Result.ok();
    }



}
