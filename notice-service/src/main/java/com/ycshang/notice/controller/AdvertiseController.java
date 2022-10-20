package com.share.advertice.controller;


import com.share.advertice.common.ResponseResult;
import com.share.advertice.service.AdvertiseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/advertisement")
@Slf4j
@RefreshScope
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdvertiseController {

    private final AdvertiseService advertiseService;

    @GetMapping("/all")
    public ResponseResult getAdvertise(){
        return  advertiseService.findAll();
    }
}
