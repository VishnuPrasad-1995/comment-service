package com.mavericsystems.commentservice.feign;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;


@Component
public class HystrixFallBackLikeFactory implements FallbackFactory<LikeFeign> {
    private static Logger logger = LoggerFactory.getLogger(HystrixFallBackLikeFactory.class);
    @Override
    public LikeFeign create(Throwable cause) {
            logger.info("fallback reason was: {}" + cause.getMessage());
            return null;
    }


}
