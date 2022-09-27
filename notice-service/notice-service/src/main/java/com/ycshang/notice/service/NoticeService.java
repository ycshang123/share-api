package com.ycshang.notice.service;


import com.ycshang.notice.common.ResponseResult;

public interface NoticeService {
    /**
     * 获取最新公告
     * @return
     */
    ResponseResult getLatestNotice();
}
