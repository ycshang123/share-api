package com.ycshang.notice.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.ycshang.notice.common.ResponseResult;
import com.ycshang.notice.common.ResultCode;
import com.ycshang.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * @program: share-project
 * @description:
 * @author: ycshang
 * @create: 2022-09-06 16:38
 **/
@RestController
@RequestMapping("api/notice")
@Slf4j
@RefreshScope
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NoticeController {
    private final NoticeService noticeService;

    @Value("${disableNoticeRequest:false}")
    private Boolean disableNotice;

    @GetMapping("/latest")
    @SentinelResource(value = "getNotice", blockHandler = "getNoticeBlock")
    public ResponseResult getLatestNotice(HttpServletRequest request) {
        String header = request.getHeader("X-Request-Foo");
        String header1 = request.getHeader("X-Request-Blue");
        log.info("AddRequestHeader:  " + header);
        log.info("X-Request-Blue:   " + header1);
        String param = request.getParameter("username");
        log.info("AddRequestParameter:  " + param);
        if (param != null && param.equals("lisi")) {
            return ResponseResult.failure(ResultCode.REDIRECT_CODE);
        }
        return noticeService.getLatestNotice();
    }


    public ResponseResult getNoticeBlock(BlockException e) {
        log.info("接口被限流");
        return ResponseResult.failure(ResultCode.INTERFACE_FALLBACK);
    }

    @GetMapping("/{id}")
    public ResponseResult getId(@PathVariable("id") int id) throws InterruptedException {
        if (1 == id) {
            Thread.sleep(500);
        }
        return ResponseResult.success();
    }
}
