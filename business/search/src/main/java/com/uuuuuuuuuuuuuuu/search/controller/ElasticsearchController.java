package com.uuuuuuuuuuuuuuu.search.controller;

import com.uuuuuuuuuuuuuuu.model.es.entity.BlobESMetaData;
import com.uuuuuuuuuuuuuuu.model.global.BaseMessageConf;
import com.uuuuuuuuuuuuuuu.model.vo.Result;
import com.uuuuuuuuuuuuuuu.search.repository.HighLight;
import com.uuuuuuuuuuuuuuu.search.repository.PageList;
import com.uuuuuuuuuuuuuuu.search.repository.PageSortHighLight;
import com.uuuuuuuuuuuuuuu.search.repository.Sort;
import com.uuuuuuuuuuuuuuu.search.service.ElasticsearchIndex;
import com.uuuuuuuuuuuuuuu.search.service.ElasticsearchTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    private ElasticsearchTemplate<BlobESMetaData,String> elasticsearchTemplate;

    public void test() throws Exception {
        BlobESMetaData blobESMetaData = new BlobESMetaData();
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
        //排序字段，注意如果proposal_no是text类型会默认带有keyword性质，需要拼接.keyword
        String sorter = "proposal_no.keyword";
        Sort.Order order = new Sort.Order(SortOrder.ASC,sorter);
        psh.setSort(new Sort(order));
        //定制高亮，如果定制了高亮，返回结果会自动替换字段值为高亮内容
        psh.setHighLight(new HighLight().field(keywords));
        //可以单独定义高亮的格式
        //new HighLight().setPreTag("<em>");
        //new HighLight().setPostTag("</em>");
        PageList<BlobESMetaData> pageList = new PageList<>();
        pageList = elasticsearchTemplate.search(new MatchAllQueryBuilder(), psh, BlobESMetaData.class);
        pageList.getList().forEach(main2 -> System.out.println(main2));

        if (StringUtils.isEmpty(keywords)) {
            return Result.failed(BaseMessageConf.KEYWORD_IS_NOT_EMPTY);
        }

        return Result.ok(elasticsearchTemplate.search(QueryBuilders.matchQuery("content",keywords),BlobESMetaData.class));
    }




}
