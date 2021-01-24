package com.uuuuuuuuuuuuuuu.oauth2resource.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @Classname CustomImportBeanDefinitionRegistrar
 */
@Slf4j
public class CustomImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    private static final String RESOURCE_SERVER_BEAN_NAME = "resource_server";


    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry,
                                        BeanNameGenerator importBeanNameGenerator) {
        registerBeanDefinitions(importingClassMetadata, registry);
    }

    /**
     * Register bean definitions as necessary based on the given annotation metadata of
     * the importing {@code @Configuration} class.
     * registered here, due to lifecycle constraints related to {@code @Configuration}
     * class processing.
     * <p>The default implementation is empty.
     * @param importingClassMetadata annotation metadata of the importing class
     * @param registry current bean definition registry
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        if(registry.containsBeanDefinition(RESOURCE_SERVER_BEAN_NAME)) {
            log.info("本地存在默认资源服务器配置");
            return;
        }
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(ResourceServerConfig.class);
        registry.registerBeanDefinition(RESOURCE_SERVER_BEAN_NAME,beanDefinition);
    }

}
