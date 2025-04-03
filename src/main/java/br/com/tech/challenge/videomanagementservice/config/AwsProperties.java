package br.com.tech.challenge.videomanagementservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.cloud.aws")
public class AwsProperties {
    private String accessKey;
    private String secretKey;
    private String sessionToken;
    private String region;
}
