package com.uuuuuuuuuuuuuuu.feign.fallback;


import com.uuuuuuuuuuuuuuu.feign.feign.SearchFeignClient;
import com.uuuuuuuuuuuuuuu.model.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class SearchFeignFallback implements SearchFeignClient {

    @Override
    public Result deleteElasticSearchByUid(String uid) {
        log.error("搜索服务出现异常, 服务降级返回, 删除ElasticSearch索引失败");
        return Result.failed("搜索服务出现异常, 服务降级返回, 删除ElasticSearch索引失败");
    }

    @Override
    public Result deleteElasticSearchByUids(String uids) {
        log.error("搜索服务出现异常, 服务降级返回, 批量删除ElasticSearch索引失败");
        return Result.failed("搜索服务出现异常, 服务降级返回, 批量删除ElasticSearch索引失败");
    }

    @Override
    public Result initElasticSearchIndex() {
        log.error("搜索服务出现异常, 服务降级返回, 初始化ElasticSearch索引失败");
        return Result.failed("搜索服务出现异常, 服务降级返回, 初始化ElasticSearch索引失败");
    }

    @Override
    public Result addElasticSearchIndexByUid(String uid) {
        log.error("搜索服务出现异常, 服务降级返回, 添加ElasticSearch索引失败");
        return Result.failed("搜索服务出现异常, 服务降级返回, 添加ElasticSearch索引失败");
    }

}
