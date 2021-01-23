package com.uuuuuuuuuuuuuuu.auth.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.uuuuuuuuuuuuuuu.model.constant.PassPortConst;
import com.uuuuuuuuuuuuuuu.model.dto.UserDto;
import com.uuuuuuuuuuuuuuu.util.util.JwtTokenUtil;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class ForumTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

        if (accessToken instanceof DefaultOAuth2AccessToken) {
            UserDto userDto = (UserDto)authentication.getUserAuthentication().getPrincipal();
            DefaultOAuth2AccessToken token = ((DefaultOAuth2AccessToken) accessToken);
            token.setValue(getNewToken(userDto));
            OAuth2RefreshToken refreshToken = token.getRefreshToken();
            if (refreshToken instanceof DefaultOAuth2RefreshToken) {
                //token.setRefreshToken(new DefaultOAuth2RefreshToken(getNewToken()));
            }
            //为返回的access token 添加返回信息
            Map<String, Object> additionalInformation = new HashMap<String, Object>();
            if (userDto != null){
                //additionalInformation.put("pkId", userDto.getPkId());
            }
            additionalInformation.put("client_id", authentication.getOAuth2Request().getClientId());

            token.setAdditionalInformation(additionalInformation);
            return token;
        }
        return accessToken;
    }

    private String getNewToken(UserDto userDto) {
        //用jwtToken,数据保存token中
        return JwtTokenUtil.createJWT(userDto.getPkId(), userDto.getUsername(), "admin", "audience", "issuer", 10, PassPortConst.JWT_SECURITY);
    }
}
