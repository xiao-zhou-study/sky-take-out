package com.sky.config;

import com.sky.properties.UploadProperties;
import com.sky.utils.UploadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class OSSConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public UploadUtil uploadUtil(UploadProperties uploadProperties) {
        log.info("开始创建UploadUtil对象，参数：{}", uploadProperties);
        return new UploadUtil(
                uploadProperties.getAllowedExtensions(),
                uploadProperties.getDefaultUploadDir(),
                uploadProperties.getDefaultWebUrlPrefix(),
                uploadProperties.getMaxFileSize()
        );
    }
}
