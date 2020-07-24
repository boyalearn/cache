/*
 * Copyright © Huawei Technologies Co., Ltd. 2018-2019. All rights reserved.
 * Description: CacheConfigBeanDefintionRegistrar
 * Author: zWX827285
 * Create: 2020/7/22
 */

package com.smart.cache.config;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author zWX827285
 * @version 1.0.0 2020/7/22
 * @see
 * @since PSM 1.0.5
 */
public class CacheConfigBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(SmartCacheConfiguration.class);
        registry.registerBeanDefinition("SmartCacheConfiguration", rootBeanDefinition);
    }
}
