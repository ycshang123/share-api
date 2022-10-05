package com.ycshang.contentservice.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.ycshang.contentservice.auth.CheckAuthorization;
import com.ycshang.contentservice.common.ResponseResult;
import com.ycshang.contentservice.common.ResultCode;

import com.ycshang.contentservice.domain.dto.ShareDTO;
import com.ycshang.contentservice.service.ShareService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: share-project
 * @description:
 * @author: ycshang
 * @create: 2022-09-06 16:31
 **/
@RestController
@RequestMapping("/share")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareController {

    private final ShareService shareService;


    @PostMapping("{id}")
    @SentinelResource(value = "getShare")
    public ResponseResult findById(@PathVariable Integer id) {
        String result = shareService.getInfo(2023);
        System.out.println(result);
        if ("BLOCKED".equals(result)) {
            return ResponseResult.failure(ResultCode.INTERFACE_EXCEED_LOAD);
        }
        return shareService.findById(id);

    }


    @GetMapping("/list")
    public ResponseResult shareList(
            @RequestParam(defaultValue = "0",required = false,value = "pageIndex") Integer pageIndex,
            @RequestParam(defaultValue = "999",required = false,value = "pageSize") Integer pageSize
    ) {
        String result = shareService.getInfo(2022);
        if ("BLOCKED".equals(result)) {
            return ResponseResult.failure(ResultCode.INTERFACE_EXCEED_LOAD);
        }
        return shareService.shareList(pageIndex,pageSize);
    }

    public ResponseResult getAllShareBlock(BlockException e) {
        log.info("接口被限流");
        return ResponseResult.failure(ResultCode.INTERFACE_FALLBACK);
    }


    @PostMapping("/check")
    @CheckAuthorization("admin")
    public ResponseResult checkContent(@RequestBody ShareDTO shareDTO) {
        return shareService.updateContentInfo(shareDTO);
    }
}
