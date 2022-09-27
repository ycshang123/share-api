package com.ycshang.userservice.controller;


import com.ycshang.userservice.common.ResponseResult;
import com.ycshang.userservice.domain.dto.UserDto;
import com.ycshang.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: share-project
 * @description:
 * @author: ycshang
 * @create: 2022-09-06 16:02
 **/
@RestController
@Slf4j
@RequestMapping("/users")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {
    private final UserService userService;

    /**
     * 根据Id查询用户
     *
     * @param id
     * @return
     */
    @PostMapping("{id}")
//    @SentinelResource(value = "getUser",blockHandler = "getUserBlock")
    public ResponseResult getUserById(@PathVariable Integer id) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return userService.findById(id);
    }

//    public ResponseResult getUserBlock(BlockException e){
//        log.info("接口被限流");
//        return ResponseResult.failure(ResultCode.INTERFACE_FALLBACK);
//    }
    /**
     * 用户登录
     *
     * @param userDto
     * @return
     */
    @PostMapping("/login")
    public ResponseResult userLogin(@RequestBody UserDto userDto) {
        return userService.login(userDto);
    }


}
