package com.ycshang.contentservice.service;


import com.ycshang.contentservice.common.ResponseResult;

public interface NoticeService {
    /**
     * 获取最新公告
     * @return
     */
    ResponseResult getLatestNotice();
}
