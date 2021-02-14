package com.uuuuuuuuuuuuuuu.model.vo;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("第三方注册vo")
public class RegisterFromThirdPartyVo {

    private String uuid;
    private String username;
    private String nickname;
    private String avatar;
    private String blog;
    private String company;
    private String location;
    private String email;
    private String remark;
    private Integer gender;
    private String source;
    private JSONObject rawUserInfo;

}
