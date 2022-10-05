package com.ycshang.contentservice.service.Impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSONObject;

import com.mysql.cj.protocol.Message;
import com.ycshang.contentservice.common.ResponseResult;
import com.ycshang.contentservice.common.ResultCode;
import com.ycshang.contentservice.domain.dto.ShareDTO;
import com.ycshang.contentservice.domain.entity.Share;
import com.ycshang.contentservice.domain.entity.User;
import com.ycshang.contentservice.domain.vo.ShareVo;
import com.ycshang.contentservice.openfeign.UserService;
import com.ycshang.contentservice.repository.ShareRepository;
import com.ycshang.contentservice.service.ShareService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * @program: share-project
 * @description:
 * @author: ycshang
 * @create: 2022-09-06 16:20
 **/
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareServiceImpl implements ShareService {

    private final ShareRepository shareRepository;
    private final UserService userService;

    @Override
    public ResponseResult findById(Integer id) {

        Boolean result = shareRepository.findById(id).isPresent();
        if (result) {
            Share share = shareRepository.findById(id).get();
            if (share != null && share.getUserId() != null) {
                ResponseResult userResult = userService.getUser(share.getUserId());
                if (userResult.getCode() == 1) {
                    Object obj = userResult.getData();
                    String userStr = JSONObject.toJSONString(obj);
                    JSONObject jsonObject = JSONObject.parseObject(userStr);
                    User user = JSONObject.toJavaObject(jsonObject, User.class);
                    ShareVo shareVo = ShareVo.builder().share(share).avatar(user.getAvatar()).nickname(user.getNickname()).build();
                    return ResponseResult.success(shareVo);
                } else {
                    return ResponseResult.failure(ResultCode.DATA_IS_WRONG);
                }
            }

        }

        return ResponseResult.failure(ResultCode.RESULT_CODE_DATA_NONE);
    }

    /**
     * 分享列表
     *
     * @return
     */
    @Override
    public ResponseResult shareList(Integer pageIndex, Integer pageSize) {
//        Sort sort = Sort.by("createTime").descending();
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<Share> list = shareRepository.findByShowFlag(true, pageable);

        return ResponseResult.success(list.getContent());
    }

    @Override
    @SentinelResource(value = "getNumber", blockHandler = "blockHandlerGetInfo")
    public String getInfo(int number) {
        return "number={" + number + "}";
    }

    @Override
    public String blockHandlerGetInfo(int number, BlockException e) {
        return "BLOCKED";
    }

    @Override
    public ResponseResult updateContentInfo(ShareDTO shareDTO) {
        try {
            Share share = shareRepository.findById(shareDTO.getId()).get();
            if (share.getAuditStatus().equals("NOT_YET")) {
                share.setAuditStatus(shareDTO.getAuditStatusEnum().name());
                share.setReason(shareDTO.getReason());
                share.setShowFlag(shareDTO.getShowFlag());
                shareRepository.saveAndFlush(share);
                return ResponseResult.success(share);
            }
        } catch (Exception e) {
            new NoSuchElementException();
        }
        return ResponseResult.failure(ResultCode.DATA_IS_UPDATE);
    }
}
