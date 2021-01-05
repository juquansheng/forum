package com.uuuuuuuuuuuuuuu.auth.properties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
public class OAuth2CoreConfig {
}
