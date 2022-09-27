package com.ycshang.notice.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: share-project
 * @description:
 * @author: ycshang
 * @create: 2022-09-08 08:17
 **/
@Configuration
public class LogConfig {
    @Bean
    Logger.Level feignLogger() {
        return Logger.Level.FULL;
    }
}
