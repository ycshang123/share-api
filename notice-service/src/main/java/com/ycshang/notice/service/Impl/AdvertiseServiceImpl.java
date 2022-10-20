package com.share.advertice.service.impl;


import com.share.advertice.common.ResponseResult;
import com.share.advertice.entity.Advertise;
import com.share.advertice.repository.AdvertiseRepository;
import com.share.advertice.service.AdvertiseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdvertiseServiceImpl implements AdvertiseService {
    private final AdvertiseRepository advertiseRepository;
    @Override
    public ResponseResult findAll() {
        List<Advertise> list = advertiseRepository.findAll();
        return ResponseResult.success(list);
    }
}
