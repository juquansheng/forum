package com.uuuuuuuuuuuuuuu.search.controller;

import com.uuuuuuuuuuuuuuu.model.es.entity.BlobESData;
import com.uuuuuuuuuuuuuuu.model.global.BaseMessageConf;
import com.uuuuuuuuuuuuuuu.model.vo.Result;
import com.uuuuuuuuuuuuuuu.search.repository.*;
import com.uuuuuuuuuuuuuuu.search.service.ElasticsearchIndex;
import com.uuuuuuuuuuuuuuu.search.service.ElasticsearchTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

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
    private ElasticsearchTemplate<BlobESData,String> elasticsearchTemplate;

    public void test() throws Exception {
        BlobESData blobESMetaData = new BlobESData();
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
                                     pageSize) throws Exception {

        //分页
        PageSortHighLight psh = new PageSortHighLight(currentPage,pageSize);
        //排序字段，注意如果是text类型会默认带有keyword性质，需要拼接.keyword
        String sorter = "age";
        Sort.Order order = new Sort.Order(SortOrder.ASC,sorter);
        psh.setSort(new Sort(order));
        //定制高亮，如果定制了高亮，返回结果会自动替换字段值为高亮内容
        HighLight highLight = new HighLight();
        HighLight field = highLight.field("content").field("title");
        field.setPreTag("<em1>");
        field.setPostTag("</em1>");
        psh.setHighLight(field);
        //可以单独定义高亮的格式

        //psh.setHighLight(new HighLight().field("content").field("desc"));
        /*PageList<BlobESMetaData> pageList = new PageList<>();
        pageList = elasticsearchTemplate.search(new MatchAllQueryBuilder(), psh, BlobESMetaData.class);
        pageList.getList().forEach(main2 -> System.out.println(main2));*/

        if (StringUtils.isEmpty(keywords)) {
            return Result.failed(BaseMessageConf.KEYWORD_IS_NOT_EMPTY);
        }
        MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("name", keywords);
        QueryBuilders.boolQuery().filter();
        PageList<BlobESData> pageList = elasticsearchTemplate.search(queryBuilder, psh, BlobESData.class);
        return Result.ok(pageList);
    }


    @ApiOperation(value = "ElasticSearch初始化索引", notes = "ElasticSearch初始化索引", response = String.class)
    @PostMapping("/initElasticSearchIndex")
    public Result initElasticSearchIndex() throws Exception {

        elasticsearchIndex.createIndex(BlobESData.class);


        return Result.ok();
    }


}
