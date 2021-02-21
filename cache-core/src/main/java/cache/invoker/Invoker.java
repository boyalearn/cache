package cache.invoker;

import cache.entity.CallMethod;

public interface Invoker {
    Object invoker(CallMethod callMethod, Object[] args, Object annotation) throws Throwable;
}
