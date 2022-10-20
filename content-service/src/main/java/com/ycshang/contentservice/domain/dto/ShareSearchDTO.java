package com.ycshang.contentservice.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShareSearchDTO {
    private String name;
    private String title;
    private String content;
}
