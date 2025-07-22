package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.utils.UploadUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@Slf4j
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
public class CommonController {

    private final UploadUtil uploadUtil;

    public CommonController(UploadUtil uploadUtil) {
        this.uploadUtil = uploadUtil;
    }

    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file) throws IOException {
        log.info("文件上传：{}", file);
        return Result.success(uploadUtil.upload(file));
    }
}
