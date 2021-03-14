package com.uuuuuuuuuuuuuuu.feign.feign;


import com.uuuuuuuuuuuuuuu.feign.config.FeignConfiguration;
import com.uuuuuuuuuuuuuuu.feign.fallback.SearchFeignFallback;
import com.uuuuuuuuuuuuuuu.model.vo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "search", configuration = FeignConfiguration.class, fallback = SearchFeignFallback.class)
public interface SearchFeignClient {


    /**
     * 通过博客uid删除ElasticSearch博客索引
     *
     * @param uid
     * @return
     */
    @PostMapping("/es/deleteElasticSearchByUid")
    public Result deleteElasticSearchByUid(@RequestParam(required = true) String uid);

    /**
     * 通过uids删除ElasticSearch博客索引
     *
     * @param uids
     * @return
     */
    @PostMapping("/es/deleteElasticSearchByUids")
    public Result deleteElasticSearchByUids(@RequestParam(required = true) String uids);

    /**
     * 初始化ElasticSearch索引
     *
     * @return
     */
    @PostMapping("/es/initElasticSearchIndex")
    public Result initElasticSearchIndex();

    /**
     * 通过uid来增加ElasticSearch索引
     *
     * @return
     */
    @PostMapping("/es/addElasticSearchIndexByUid")
    public Result addElasticSearchIndexByUid(@RequestParam(required = true) String uid);

}
