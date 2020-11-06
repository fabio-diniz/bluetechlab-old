package com.ciandt.techlab.order.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;

@Configuration
public class AWSClientConfig {

    @Value("${amazon.dynamodb.endpoint}")
    private String amazonDynamoDBEndpoint;

    @Value("${amazon.defaultRegion}")
    private String defaultRegion;

    @Value("${featureToggle.useLocalEndpoint}")
    private Boolean useLocalEndpoint;

    @Bean
    public DynamoDbClient dynamoDbClient() {
        if (useLocalEndpoint) {
            return DynamoDbClient.builder()
                    .endpointOverride(URI.create(amazonDynamoDBEndpoint))
                    .build();
        }
        return DynamoDbClient.builder()
                .region(Region.of(defaultRegion))
                .build();
    }
}