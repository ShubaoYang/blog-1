package com.minzheng.blog.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 青云配置文件
 * @author shubao
 */
@Data
@Component
@ConfigurationProperties(prefix = "upload.oss")
public class QingYunOssProperties {

    private String host;

    private String access_key_id;

    private String secret_access_key;

    private String zoneKey;

    private String bucketName;

}

