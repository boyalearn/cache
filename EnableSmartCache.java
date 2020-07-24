
package com.smart.cache.config;

import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zWX827285
 * @version 1.0.0 2020/7/22
 * @see
 * @since PSM 1.0.5
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableAspectJAutoProxy
@Import(value = CacheConfigBeanDefinitionRegistrar.class)
public @interface EnableSmartCache {

}
