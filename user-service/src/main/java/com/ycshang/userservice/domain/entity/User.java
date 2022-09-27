package com.ycshang.userservice.domain.entity;

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
 * @create: 2022-09-06 14:11
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String mobile;
    private String password;
    private String nickname;
    private String roles;
    private String avatar;
    private Date createTime;
    private Date updateTime;
    private Integer bonus;
}
