package com.uuuuuuuuuuuuuuu.search.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.uuuuuuuuuuuuuuu.model.constant.Constants;
import com.uuuuuuuuuuuuuuu.model.dto.UserDto;
import com.uuuuuuuuuuuuuuu.model.es.dto.BlogESData;
import com.uuuuuuuuuuuuuuu.model.global.BaseMessageConf;
import com.uuuuuuuuuuuuuuu.model.vo.Result;
import com.uuuuuuuuuuuuuuu.search.repository.*;
import com.uuuuuuuuuuuuuuu.search.service.ElasticsearchIndex;
import com.uuuuuuuuuuuuuuu.search.service.ElasticsearchTemplate;
import com.uuuuuuuuuuuuuuu.util.util.StringConvertUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * description: ElasticsearchController
 * date: 2021/1/12 15:41
 * author: juquansheng
 * version: 1.0 <br>
 */
@Slf4j
@Api(value = "博客ElasticSearch相关接口", tags = {"博客ElasticSearch相关接口"})
@RestController
@RequestMapping(value = "es")
public class ElasticsearchController {

    @Autowired
    private ElasticsearchIndex elasticsearchIndex;
    @Autowired
    private ElasticsearchTemplate<BlogESData,String> elasticsearchTemplate;

    public void test() throws Exception {
        BlogESData blogESMetaData = new BlogESData();
        blogESMetaData.setId("1");
        elasticsearchTemplate.save(blogESMetaData);
    }

    @ApiOperation(value = "通过ElasticSearch搜索博客", notes = "通过ElasticSearch搜索博客", response = String.class)
    @GetMapping("/elasticSearchBlog")
    public Result searchBlog(HttpServletRequest request,
                             @RequestParam(required = false) String keywords,
                             @RequestParam(name = "currentPage", required = false, defaultValue = "1") Integer
                                     currentPage,
                             @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer
                                     pageSize,
                             @RequestParam(name = "sorter", required = false, defaultValue = "sort") String
                                         sorter) throws Exception {

        //分页
        PageSortHighLight psh = new PageSortHighLight(currentPage,pageSize);
        //排序字段，注意如果是text类型会默认带有keyword性质，需要拼接.keyword
        Sort.Order order = new Sort.Order(SortOrder.ASC,sorter);
        psh.setSort(new Sort(order));
        //定制高亮，如果定制了高亮，返回结果会自动替换字段值为高亮内容
        HighLight highLight = new HighLight();
        HighLight field = highLight.field("title").field("content");
        field.setPreTag("<span style='color:red'>");
        field.setPostTag("</span>");
        psh.setHighLight(field);

        if (StringUtils.isEmpty(keywords)) {
            return Result.failed(BaseMessageConf.KEYWORD_IS_NOT_EMPTY);
        }
        MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("title", keywords);
        BoolQueryBuilder filter = QueryBuilders.boolQuery().filter(queryBuilder);

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("title", keywords)).should(QueryBuilders.matchQuery("content", keywords));

        PageList<BlogESData> pageList = elasticsearchTemplate.search(boolQueryBuilder, psh, BlogESData.class);
        return Result.ok(pageList);
    }

    @ApiOperation(value = "通过uids删除ElasticSearch博客索引", notes = "通过uids删除ElasticSearch博客索引", response = String.class)
    @PostMapping("/deleteElasticSearchByUids")
    public Result deleteElasticSearchByUids(@RequestParam(required = true) String uids) throws Exception {

        List<String> uidList = StringConvertUtils.changeStringToString(uids, Constants.FILE_SEGMENTATION);

        for (String uid : uidList) {
            elasticsearchTemplate.deleteById(uid,BlogESData.class);
        }

        return Result.ok();
    }

    @ApiOperation(value = "通过博客uid删除ElasticSearch博客索引", notes = "通过uid删除博客", response = String.class)
    @PostMapping("/deleteElasticSearchByUid")
    public Result deleteElasticSearchByUid(@RequestParam(required = true) String uid) throws Exception {
        elasticsearchTemplate.deleteById(uid,BlogESData.class);
        return Result.ok();
    }


    @ApiOperation(value = "ElasticSearch通过博客Uid添加索引", notes = "添加博客", response = String.class)
    @PostMapping("/addElasticSearchIndexByUid")
    public Result addElasticSearchIndexByUid(@RequestParam(required = true) String uid) {
        UserDto userDto = JSON.parseObject(JSON.toJSONString(SecurityContextHolder.getContext().getAuthentication().getPrincipal()), UserDto.class);
        //查询数据库是否存在对应博客数据

        //如果存在则保存数据

        log.info("ElasticSearch通过博客Uid添加索引");
        return Result.ok(uid);

    }


    @ApiOperation(value = "ElasticSearch初始化索引", notes = "ElasticSearch初始化索引", response = String.class)
    @PostMapping("/initElasticSearchIndex")
    public Result initElasticSearchIndex() throws Exception {
        UserDto userDto = JSON.parseObject(JSON.toJSONString(SecurityContextHolder.getContext().getAuthentication().getPrincipal()), UserDto.class);
        elasticsearchIndex.createIndex(BlogESData.class);


        return Result.ok();
    }


}
