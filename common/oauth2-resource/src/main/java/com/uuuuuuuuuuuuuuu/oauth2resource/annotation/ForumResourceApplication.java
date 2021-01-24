package com.uuuuuuuuuuuuuuu.oauth2resource.annotation;


import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

import java.lang.annotation.*;

/**
 * @Classname ForumResourceApplication
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ForumResourceServer
@SpringBootApplication(exclude = {DruidDataSourceAutoConfigure.class, UserDetailsServiceAutoConfiguration.class})
public @interface ForumResourceApplication {
}
