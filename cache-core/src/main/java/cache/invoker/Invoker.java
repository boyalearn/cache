package cache.invoker;

import com.smart.cache.entity.CallMethod;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

public interface Invoker {
    Object invoker(CallMethod callMethod, Object[] args,Object annotation) throws Throwable;
}
