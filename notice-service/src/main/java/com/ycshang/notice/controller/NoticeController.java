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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: share-project
 * @description:
 * @author: ycshang
 * @create: 2022-09-06 16:38
 **/
@RestController
@RequestMapping("/notice")
@Slf4j
@RefreshScope
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NoticeController {
    private final NoticeService noticeService;

    @Value("${disableNoticeRequest:false}")
    private Boolean disableNotice;

    @PostMapping("/latest")
    @SentinelResource(value = "getNotice", blockHandler = "getNoticeBlock")
    public ResponseResult getLatestNotice() {
        if (disableNotice) {
            log.info("暂停公告服务");
            return ResponseResult.failure(ResultCode.INTERFACE_FORBID_VISIT);
        }
        return noticeService.getLatestNotice();
    }


    public ResponseResult getNoticeBlock(BlockException e) {
        log.info("接口被限流");
        return ResponseResult.failure(ResultCode.INTERFACE_FALLBACK);
    }
}
