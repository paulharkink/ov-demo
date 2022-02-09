package com.example.ovticket.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
@Slf4j
@EnableConfigurationProperties(AWSBaseConfig.BasicAWSCredentialsConfigurationProperties.class)
public class AWSBaseConfig {

    public static final String REGION = Regions.EU_WEST_1.getName();

    @ConfigurationProperties("aws.credentials")
    @Setter
    static class BasicAWSCredentialsConfigurationProperties {

        private String accessKey;

        private String secretKey;

        BasicAWSCredentials toBasicAwsCredentials() {
            return new BasicAWSCredentials(accessKey, secretKey);
        }
    }

    @Bean
    AWSStaticCredentialsProvider legacyCredentialsProvider(BasicAWSCredentialsConfigurationProperties credentialsConfigurationProperties) {
        return new AWSStaticCredentialsProvider(credentialsConfigurationProperties.toBasicAwsCredentials());
    }


}
