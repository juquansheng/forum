package com.uuuuuuuuuuuuuuu.search.controller;

import com.uuuuuuuuuuuuuuu.model.es.dto.BlogESData;
import com.uuuuuuuuuuuuuuu.model.global.BaseMessageConf;
import com.uuuuuuuuuuuuuuu.model.vo.Result;
import com.uuuuuuuuuuuuuuu.search.repository.*;
import com.uuuuuuuuuuuuuuu.search.service.ElasticsearchIndex;
import com.uuuuuuuuuuuuuuu.search.service.ElasticsearchTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * description: ElasticsearchController
 * date: 2021/1/12 15:41
 * author: juquansheng
 * version: 1.0 <br>
 */
@Api(value = "博客ElasticSearch相关接口", tags = {"博客ElasticSearch相关接口"})
@RestController
@RequestMapping(value = "es")
public class ElasticsearchController {

    @Autowired
    private ElasticsearchIndex elasticsearchIndex;
    @Autowired
    private ElasticsearchTemplate<BlogESData,String> elasticsearchTemplate;

    public void test() throws Exception {
        BlogESData blobESMetaData = new BlogESData();
        blobESMetaData.setId("1");
        elasticsearchTemplate.save(blobESMetaData);
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


    @ApiOperation(value = "ElasticSearch初始化索引", notes = "ElasticSearch初始化索引", response = String.class)
    @PostMapping("/initElasticSearchIndex")
    public Result initElasticSearchIndex() throws Exception {

        elasticsearchIndex.createIndex(BlogESData.class);


        return Result.ok();
    }


}
