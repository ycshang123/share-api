package com.ycshang.contentservice.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * @program: share-project
 * @description:
 * @author: ycshang
 * @create: 2022-09-06 15:40
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String content;
    private Boolean showFlag;
    private Date createTime;
}
