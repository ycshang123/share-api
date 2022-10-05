package com.ycshang.contentservice.domain.dto;


import com.ycshang.contentservice.enmus.AuditStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShareDTO {
    private Integer id;
    private AuditStatusEnum auditStatusEnum;
    private String reason;
    private Boolean showFlag;
}
