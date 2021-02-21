package cache.invoker;

import com.smart.cache.entity.CallMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DirectInvoker implements Invoker {
    private final static Logger LOGGER = LoggerFactory.getLogger(DirectInvoker.class);

    @Override
    public Object invoker(CallMethod callMethod, Object[] args, Object annotation) throws Throwable {
        LOGGER.debug("direct invoker");
        return callMethod.invoker(args);
    }
}
