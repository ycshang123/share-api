package com.ycshang.contentservice.service;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.ycshang.contentservice.common.ResponseResult;
import com.ycshang.contentservice.domain.dto.ShareDTO;

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
    ResponseResult shareList(Integer pageIndex,Integer pageSize);

    String getInfo(int  number);


    String blockHandlerGetInfo(int number, BlockException e);

    ResponseResult updateContentInfo(ShareDTO shareDTO);
}
