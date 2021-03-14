package com.uuuuuuuuuuuuuuu.blog.controller;

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

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * description: ElasticsearchController
 * date: 2021/1/12 15:41
 * author: juquansheng
 * version: 1.0 <br>
 */
@Slf4j
@Api(value = "博客相关接口", tags = {"博客相关接口"})
@RestController
@RequestMapping(value = "blog")
public class BlogController {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Resource
    private SearchFeignClient searchFeignClient;


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
        UserDto userDto = JSON.parseObject(JSON.toJSONString(SecurityContextHolder.getContext().getAuthentication().getPrincipal()), UserDto.class);
        // 参数校验
        ThrowableUtils.checkParamArgument(result);
        blogVO.setUserId(userDto.getPkId().toString());
        mongoTemplate.getCollection("blog").insertOne(MongoDBConvertUtils.toDocument(blogVO));
        //同步内容到es
        Result result1 = searchFeignClient.addElasticSearchIndexByUid("1");
        return Result.ok(result1);
    }

    @ApiOperation(value = "获取博客", notes = "获取博客", response = String.class)
    @GetMapping("/get")
    public Result add(@RequestParam(required = false) String id) throws Exception {
        UserDto userDto = JSON.parseObject(JSON.toJSONString(SecurityContextHolder.getContext().getAuthentication().getPrincipal()), UserDto.class);
        // 根据文章主键获取
        FindIterable<Document> documents = mongoTemplate.getCollection("blog").find(new BasicDBObject().append("userId", id));
        return Result.ok(documents.cursor().next());
    }

}
