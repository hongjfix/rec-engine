package com.hackathon.recengine.config;

import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class DynamoDBConfig {

    private DefaultCredentialsProvider credentialsProvider = DefaultCredentialsProvider.create();

    private DynamoDbClient dynamoDbClient;

    @Bean
    public DynamoDbClient dynamoDbClient() {
        dynamoDbClient = DynamoDbClient.builder()
                .region(Region.US_WEST_1) // 替换为您的 AWS 区域
                .credentialsProvider(credentialsProvider)
                .build();
        return dynamoDbClient;
    }

    @PreDestroy
    public void onDestroy() {
        if (dynamoDbClient != null) {
            dynamoDbClient.close();
        }
    }
}