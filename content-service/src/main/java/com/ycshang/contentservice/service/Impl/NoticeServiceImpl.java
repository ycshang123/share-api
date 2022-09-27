package com.ycshang.contentservice.service.Impl;

import com.ycshang.contentservice.common.ResponseResult;
import com.ycshang.contentservice.common.ResultCode;
import com.ycshang.contentservice.domain.entity.Notice;
import com.ycshang.contentservice.repository.NoticeRepository;
import com.ycshang.contentservice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * @program: share-project
 * @description:
 * @author: ycshang
 * @create: 2022-09-06 16:21
 **/
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;

    /**
     * 查询最新公告
     * @return
     */
    @Override
    public ResponseResult getLatestNotice() {
        Sort sort = Sort.by("createTime").descending();
        Notice notice = noticeRepository.findByShowFlag(true, sort).get(0);
        if (notice != null) {
            return ResponseResult.success(notice);
        }
        return ResponseResult.failure(ResultCode.RESULT_CODE_DATA_NONE);
    }
}
