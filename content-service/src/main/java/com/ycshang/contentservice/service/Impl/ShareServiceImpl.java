package com.ycshang.contentservice.service.Impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSONObject;

import com.mysql.cj.protocol.Message;
import com.ycshang.contentservice.common.ResponseResult;
import com.ycshang.contentservice.common.ResultCode;
import com.ycshang.contentservice.domain.dto.ShareDTO;
import com.ycshang.contentservice.domain.dto.ShareSearchDTO;
import com.ycshang.contentservice.domain.dto.UserAddBonusDto;
import com.ycshang.contentservice.domain.entity.MidUserShare;
import com.ycshang.contentservice.domain.entity.Share;
import com.ycshang.contentservice.domain.entity.User;
import com.ycshang.contentservice.domain.vo.ShareVo;
import com.ycshang.contentservice.enmus.AuditStatusEnum;
import com.ycshang.contentservice.openfeign.UserService;
import com.ycshang.contentservice.repository.MidUserShareRepository;
import com.ycshang.contentservice.repository.ShareRepository;
import com.ycshang.contentservice.service.ShareService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


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

    private final MidUserShareRepository midUserShareRepository;
    private final RocketMQTemplate rocketMQTemplate;

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
    public ResponseResult shareList(Pageable pageable, ShareSearchDTO searchDTO, Integer userId) {

        List<Share> list = new ArrayList<>();
        Page<Share> result = shareRepository.findAll((Specification<Share>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (null != searchDTO.getTitle() && !"".equals(searchDTO.getTitle())) {
                predicates.add(criteriaBuilder.equal(root.get("title").as(String.class), searchDTO.getTitle()));
            }
            if (null != searchDTO.getName() && !"".equals(searchDTO.getName())) {
                predicates.add(criteriaBuilder.equal(root.get("author").as(String.class), searchDTO.getName()));
            }
            if (null != searchDTO.getContent() && !"".equals(searchDTO.getContent())) {
                predicates.add(criteriaBuilder.equal(root.get("summary").as(String.class), searchDTO.getContent()));
            }
            Predicate[] p = new Predicate[predicates.size()];
            return criteriaBuilder.and(predicates.toArray(p));

        }, pageable);
//        用户没登录
        if (userId == 0) {
            list = result.stream().peek(share -> {
                share.setDownloadUrl(null);
            }).collect(Collectors.toList());
        } else {
            list = result.stream().peek(share -> {
                MidUserShare midUserShare = MidUserShare.builder()
                        .shareId(share.getId()).userId(userId).build();
                Example<MidUserShare> example = Example.of(midUserShare);
                if (!midUserShareRepository.findOne(example).isPresent()) {
                    share.setDownloadUrl(null);

                }
            }).collect(Collectors.toList());

        }


        return ResponseResult.success(list);
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
                Share newShare = shareRepository.saveAndFlush(share);

                MidUserShare midUserShare = MidUserShare.builder().shareId(newShare.getId())
                        .userId(newShare.getUserId()).build();
                midUserShareRepository.save(midUserShare);


                if (AuditStatusEnum.PASS.equals(shareDTO.getAuditStatusEnum())) {
                    rocketMQTemplate.convertAndSend("add-bonus",
                            UserAddBonusDto.builder().userId(share.getUserId()).bonus(50).build());

                }
                return ResponseResult.success(share);
            }
        } catch (Exception e) {
            new NoSuchElementException();
        }


        return ResponseResult.failure(ResultCode.DATA_IS_UPDATE);
    }
}
