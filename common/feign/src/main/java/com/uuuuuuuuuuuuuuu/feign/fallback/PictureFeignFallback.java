package com.uuuuuuuuuuuuuuu.feign.fallback;


import com.uuuuuuuuuuuuuuu.feign.feign.PictureFeignClient;
import com.uuuuuuuuuuuuuuu.model.holder.RequestHolder;
import com.uuuuuuuuuuuuuuu.model.vo.FileVO;
import com.uuuuuuuuuuuuuuu.model.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Component
@Slf4j
public class PictureFeignFallback implements PictureFeignClient {

    @Override
    public Result getPicture(String fileIds, String code) {
        HttpServletRequest request = RequestHolder.getRequest();
        StringBuffer requestURL = request.getRequestURL();
        log.error("图片服务出现异常，服务降级返回，请求路径: {}", requestURL);
        return Result.failed("获取图片服务降级返回");
    }

    @Override
    public Result uploadPicsByUrl(FileVO fileVO) {
        HttpServletRequest request = RequestHolder.getRequest();
        StringBuffer requestURL = request.getRequestURL();
        log.error("图片服务出现异常，更新图片失败，服务降级返回，请求路径: {}", requestURL);
        return Result.failed("更新图片服务降级返回");
    }

    @Override
    public Result initStorageSize(String adminUid, Long maxStorageSize) {
        HttpServletRequest request = RequestHolder.getRequest();
        StringBuffer requestURL = request.getRequestURL();
        log.error("图片服务出现异常，初始化网盘容量失败，服务降级返回，请求路径: {}", requestURL);
        return Result.failed("图片服务出现异常，初始化网盘容量失败");
    }

    @Override
    public Result editStorageSize(String adminUid, Long maxStorageSize) {
        HttpServletRequest request = RequestHolder.getRequest();
        StringBuffer requestURL = request.getRequestURL();
        log.error("图片服务出现异常，更新网盘容量失败，服务降级返回，请求路径: {}", requestURL);
        return Result.failed("图片服务出现异常，更新网盘容量失败，服务降级返回");
    }

    @Override
    public Result getStorageByAdminUid(List<String> adminUidList) {
        HttpServletRequest request = RequestHolder.getRequest();
        StringBuffer requestURL = request.getRequestURL();
        log.error("图片服务出现异常，获取网盘容量失败，服务降级返回，请求路径: {}", requestURL);
        return Result.failed("图片服务出现异常，获取网盘容量失败，服务降级返回");
    }
}
