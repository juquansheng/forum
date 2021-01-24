package com.uuuuuuuuuuuuuuu.oauth2resource.annotation;


import com.uuuuuuuuuuuuuuu.oauth2resource.config.CustomImportBeanDefinitionRegistrar;
import com.uuuuuuuuuuuuuuu.oauth2resource.handler.GlobalExceptionHandler;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import java.lang.annotation.*;

/**
 * @Classname ForumResourceServer
 * @description 资源服务器注解
 */
@Documented
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Import(value = {CustomImportBeanDefinitionRegistrar.class, GlobalExceptionHandler.class})
public @interface ForumResourceServer {
}
