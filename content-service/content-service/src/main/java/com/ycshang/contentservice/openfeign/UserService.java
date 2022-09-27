package com.ycshang.contentservice.openfeign;


import com.ycshang.contentservice.common.ResponseResult;
import com.ycshang.contentservice.openfeign.fallback.UserServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

//@FeignClient(value = "user-service", path = "/users", fallback = UserServiceFallback.class)
@FeignClient(value = "user-service", path = "/users", fallbackFactory = UserServiceFallbackFactory.class)
public interface UserService {

    @PostMapping("{id}")
    ResponseResult getUser(@PathVariable("id") Integer id);
}
