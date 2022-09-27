package com.ycshang.contentservice.openfeign.fallback;


import com.ycshang.contentservice.common.ResponseResult;
import com.ycshang.contentservice.domain.entity.User;
import com.ycshang.contentservice.openfeign.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @program: share-project
 * @description:
 * @author: ycshang
 * @create: 2022-09-08 08:38
 **/
@Slf4j
@Component
public class UserServiceFallback implements UserService {
    @Override
    public ResponseResult getUser(Integer id) {
        log.info("fallback getUser");
        User user = User.builder().id(2).mobile("13913457284").nickname("服务降级用户").build();
        return ResponseResult.success(user);
    }
}
