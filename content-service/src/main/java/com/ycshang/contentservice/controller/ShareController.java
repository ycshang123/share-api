package com.ycshang.contentservice.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.ycshang.contentservice.auth.CheckAuthorization;
import com.ycshang.contentservice.common.ResponseResult;
import com.ycshang.contentservice.common.ResultCode;

import com.ycshang.contentservice.domain.dto.ShareDTO;
import com.ycshang.contentservice.domain.dto.ShareSearchDTO;
import com.ycshang.contentservice.service.ShareService;
import com.ycshang.contentservice.utils.JwtOperator;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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

    private final JwtOperator jwtOperator;


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
            @RequestParam(defaultValue = "1", required = false, value = "pageIndex") Integer pageIndex,
            @RequestParam(defaultValue = "10", required = false, value = "pageSize") Integer pageSize,
            @RequestParam(defaultValue = "", required = false, value = "title") String title,
            @RequestParam(defaultValue = "", required = false, value = "name") String name,
            @RequestParam(defaultValue = "", required = false, value = "content") String content,
            @RequestHeader(value = "x-token", required = false) String token
    ) {
        Integer userId = 0;
        if (token != null) {
            Claims claims = jwtOperator.getClaimsFromToken(token);
            userId = (Integer) claims.get("id");
        }
        if (pageSize > 100) {
            pageSize = 100;
        }
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        ShareSearchDTO searchDTO = ShareSearchDTO.builder().title(title).content(content).name(name).build();
        return shareService.shareList(pageable, searchDTO, userId);
    }

    public ResponseResult getAllShareBlock(BlockException e) {
        log.info("接口被限流");
        return ResponseResult.failure(ResultCode.INTERFACE_FALLBACK);
    }


    /**
     * 审核
     *
     * @param shareDTO
     * @return
     */
    @PostMapping("/check")
    @CheckAuthorization("admin")
    public ResponseResult checkContent(@RequestBody ShareDTO shareDTO) {
        return shareService.updateContentInfo(shareDTO);
    }
}
