package com.uuuuuuuuuuuuuuu.feign.feign;


import com.uuuuuuuuuuuuuuu.feign.config.FeignConfiguration;
import com.uuuuuuuuuuuuuuu.model.vo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



@FeignClient(name = "mogu-admin", configuration = FeignConfiguration.class)
public interface AdminFeignClient {


    /**
     * 获取系统配置信息
     */
    @RequestMapping(value = "/systemConfig/getSystemConfig", method = RequestMethod.GET)
    public Result getSystemConfig();

}