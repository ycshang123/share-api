package com.ycshang.contentservice.domain.vo;

import com.ycshang.contentservice.domain.entity.Share;
import lombok.Builder;
import lombok.Data;

/**
 * @program: share-project
 * @description:
 * @author: ycshang
 * @create: 2022-09-06 16:17
 **/
@Data
@Builder
public class ShareVo {
    private String nickname;
    private String avatar;
    private Share share;
}
