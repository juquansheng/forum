package com.uuuuuuuuuuuuuuu.auth.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "yuuki.security")
@Data
public class SecurityProperties {

    private OAuth2Properties oauth = new OAuth2Properties();
}

