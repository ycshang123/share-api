package com.ycshang.contentservice.auth;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 自定义鉴权注解
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckAuthorization {
    String value();
}
