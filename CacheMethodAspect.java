/*
 * Copyright © Huawei Technologies Co., Ltd. 2018-2019. All rights reserved.
 * Description: CacheMethodAspect
 * Author: zWX827285
 * Create: 2020/7/23
 */

package com.smart.cache.aspect;

import com.smart.cache.invoker.Invoker;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author zWX827285
 * @version 1.0.0 2020/7/23
 * @see
 * @since PSM 1.0.5
 */
@Aspect
public class CacheMethodAspect {

    private Invoker invoker;

    @Pointcut("@annotation(com.smart.cache.annotation.Cache)")
    public void cachePointCut() {
    }

    public CacheMethodAspect(Invoker invoker) {
        this.invoker = invoker;
    }

    @Around(value = "cachePointCut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        return invoker.invoker(pjp);
    }
}
