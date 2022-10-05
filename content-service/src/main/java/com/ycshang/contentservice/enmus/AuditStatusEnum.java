package com.ycshang.contentservice.enmus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuditStatusEnum {
    REJECT,
    PASS,
    NOT_YET;
}
