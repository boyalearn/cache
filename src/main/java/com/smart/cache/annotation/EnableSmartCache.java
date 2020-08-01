
package com.smart.cache.annotation;

import com.smart.cache.config.CacheConfigBeanDefinitionRegistrar;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableAspectJAutoProxy
@Import(value = CacheConfigBeanDefinitionRegistrar.class)
public @interface EnableSmartCache {

}
