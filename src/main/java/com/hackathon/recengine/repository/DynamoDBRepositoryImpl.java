package com.hackathon.recengine.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DynamoDBRepositoryImpl implements DynamoDBRepository{

    private final DynamoDbClient dynamoDbClient;

    @Autowired
    public DynamoDBRepositoryImpl(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    @Override
    public Map<String, AttributeValue> getValueByKey(String tableName, Map<String, AttributeValue> keyMap) {
        GetItemRequest getItemRequest = GetItemRequest.builder()
                .tableName(tableName)
                .key(keyMap)
                .build();
        GetItemResponse response = dynamoDbClient.getItem(getItemRequest);
        return response.item();
    }

    @Override
    public List<Map<String, AttributeValue>> batchGetValueByKey(String tableName, List<Map<String, AttributeValue>> keyMapList) {

        Map<String, KeysAndAttributes> requestItems = new HashMap<>();

        requestItems.put(tableName, KeysAndAttributes.builder().keys(keyMapList).build());

        BatchGetItemRequest batchGetItemRequest = BatchGetItemRequest.builder()
                .requestItems(requestItems)
                .build();

        BatchGetItemResponse batchGetItemResponse = dynamoDbClient.batchGetItem(batchGetItemRequest);

        return batchGetItemResponse.responses().get(tableName);
    }
}
