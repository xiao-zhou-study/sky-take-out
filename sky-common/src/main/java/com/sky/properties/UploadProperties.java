package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sky.upload")
@Data
public class UploadProperties {
    // 默认的上传目录
    private String defaultUploadDir;
    // 返回的服务器路径
    private String defaultWebUrlPrefix;
    // 允许上传的文件最大大小
    private Long maxFileSize;
    // 允许上传的文件后缀
    private String[] allowedExtensions;

}
