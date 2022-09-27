package com.ycshang.fileservice.controller;



import cn.hutool.core.util.IdUtil;
import com.ycshang.fileservice.common.ResponseResult;
import com.ycshang.fileservice.utils.MinIoTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping("/file")
@RestController
public class FileController {

    @Resource
    private MinIoTemplate minIoTemplate;

    @PostMapping("/upload")
    public ResponseResult uploadAdvertising(@RequestParam("file") MultipartFile file, HttpServletResponse response, HttpServletRequest requset) throws Exception {
        // 获取原始文件名
        String oldName = file.getOriginalFilename();
        assert oldName != null;
        // 重命名文件为UUID + 原始文件后缀
        String newName = IdUtil.fastUUID() + oldName.substring(oldName.lastIndexOf("."));
        //上传文件到minIO
        minIoTemplate.putObject("share-api", newName, file.getInputStream());
        // 给前端返回重命名后的文件名
        return ResponseResult.success(newName);

    }
}
