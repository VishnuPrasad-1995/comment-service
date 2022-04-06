package com.mavericsystems.commentservice.feign;

import com.mavericsystems.commentservice.configuration.CustomRetryClientConfig;
import com.mavericsystems.commentservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", configuration = CustomRetryClientConfig.class,fallbackFactory = HystrixFallBackUserFactory.class)
public interface UserFeign {
    @GetMapping("/users/{userId}")
    UserDto getUserById(@PathVariable("userId") String userId);
}
