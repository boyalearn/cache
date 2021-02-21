package cache.config;

import cache.annotation.Cache;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class OnNoCacheCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        try {
            Cache cache = conditionContext.getBeanFactory().getBean(Cache.class);
            if (null == cache) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return true;
        }
    }
}
