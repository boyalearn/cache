package com.smart.cache.annotation;/*
 * Copyright © Huawei Technologies Co., Ltd. 2018-2019. All rights reserved.
 * Description: Cache
 * Author: zWX827285
 * Create: 2020/7/22
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cache {
    long interval() default 10*000;
    long expired() default  60*60*1000;
}
