package com.ycshang.contentservice.openfeign.fallback;


import com.ycshang.contentservice.common.ResponseResult;
import com.ycshang.contentservice.domain.entity.User;
import com.ycshang.contentservice.openfeign.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;


/**
 * @program: share-project
 * @description:
 * @author: ycshang
 * @create: 2022-09-08 09:38
 **/
@Slf4j
@Component
public class UserServiceFallbackFactory implements FallbackFactory<UserService> {
    @Override
    public UserService create(Throwable cause) {
        return new UserService() {
            @Override
            public ResponseResult getUser(Integer id) {
                log.info("fallback factory method test " + cause);
                User user = User.builder().nickname("降级方案返回用户").avatar("default.jpg").build();
                return ResponseResult.success(user);
            }
        };
    }
}
