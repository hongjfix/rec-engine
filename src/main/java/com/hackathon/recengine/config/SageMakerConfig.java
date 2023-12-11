package com.hackathon.recengine.config;

import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sagemakerruntime.SageMakerRuntimeClient;

@Configuration
public class SageMakerConfig {

    private DefaultCredentialsProvider credentialsProvider = DefaultCredentialsProvider.create();

    private SageMakerRuntimeClient sageMakerRuntimeClient;

    @Bean
    public SageMakerRuntimeClient sageMakerRuntimeClient() {
        sageMakerRuntimeClient = SageMakerRuntimeClient.builder().
                region(Region.US_WEST_1).
                credentialsProvider(credentialsProvider).
                build();

        return sageMakerRuntimeClient;
    }

    @PreDestroy
    public void onDestroy() {
        if (sageMakerRuntimeClient != null) {
            sageMakerRuntimeClient.close();
        }
    }
}
