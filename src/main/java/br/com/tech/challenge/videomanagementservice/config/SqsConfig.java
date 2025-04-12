package br.com.tech.challenge.videomanagementservice.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

@RequiredArgsConstructor
@Configuration
public class SqsConfig {

    private final AwsProperties awsProperties;

    @Bean
    public SqsClient sqsClientLocal() {
        return SqsClient.builder()
                .region(Region.of(awsProperties.getRegion()))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
}