package com.ycshang.userservice.service;


import com.ycshang.userservice.common.ResponseResult;
import com.ycshang.userservice.domain.dto.UserDto;

public interface UserService {
    /**
     * 根据Id查询用户
     * @param Id
     * @return
     */
    ResponseResult findById(Integer Id);

    /**
     * 用户登录
     * @param userDto
     * @return
     */
    ResponseResult login(UserDto userDto);
}
