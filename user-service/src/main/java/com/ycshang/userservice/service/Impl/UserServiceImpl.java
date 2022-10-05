package com.ycshang.userservice.service.Impl;


import com.google.common.collect.Maps;
import com.ycshang.userservice.common.ResponseResult;
import com.ycshang.userservice.common.ResultCode;
import com.ycshang.userservice.domain.dto.UserDto;
import com.ycshang.userservice.domain.entity.User;
import com.ycshang.userservice.repository.UserRepository;
import com.ycshang.userservice.service.RedisService;
import com.ycshang.userservice.service.UserService;
import com.ycshang.userservice.utils.JwtOperator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @program: share-project
 * @description:
 * @author: ycshang
 * @create: 2022-09-06 15:53
 **/
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {
    final private UserRepository userRepository;


    private final JwtOperator jwtOperator;

    private  final RedisService redisService;



    /**
     * 根据Id查询用户
     *
     * @param Id
     * @return
     */
    @Override
    public ResponseResult findById(Integer Id) {
        Boolean isPresent = userRepository.findById(Id).isPresent();
        if (isPresent) {
            User user = userRepository.findById(Id).get();
            return ResponseResult.success(user);
        }
        return ResponseResult.failure(ResultCode.USER_NOT_FOUND);
    }

    /**
     * 用户登录
     *
     * @param userDto
     * @return
     */
    @Override
    public ResponseResult login(UserDto userDto) {
        User user = userRepository.findByMobileAndPassword(userDto.getMobile(), userDto.getPassword());
        if (user != null) {
            HashMap<String, Object> objectObjectHashMap = Maps.newHashMap();
            objectObjectHashMap.put("id", user.getId());
            objectObjectHashMap.put("nickname", user.getNickname());
            objectObjectHashMap.put("role",user.getRoles());
            String token = jwtOperator.generateToken(objectObjectHashMap);
            redisService.set("x-token",token);

            return ResponseResult.success(user);
        }
        return ResponseResult.failure(ResultCode.USER_SIGN_IN_FAIL);
    }
}
