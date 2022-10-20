package com.ycshang.contentservice.service;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.ycshang.contentservice.common.ResponseResult;
import com.ycshang.contentservice.domain.dto.ShareDTO;
import com.ycshang.contentservice.domain.dto.ShareSearchDTO;
import org.springframework.data.domain.Pageable;

public interface ShareService {

    /**
     * 根据Id查询内容
     * @param id
     * @return
     */
    ResponseResult findById(Integer id);


    /**
     * 内容列表
     * @return
     */
    ResponseResult shareList(Pageable pageable, ShareSearchDTO shareSearchDTO, Integer userId);

    String getInfo(int  number);


    String blockHandlerGetInfo(int number, BlockException e);

    ResponseResult updateContentInfo(ShareDTO shareDTO);
}
