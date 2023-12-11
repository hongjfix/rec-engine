package com.hackathon.recengine.repository;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.List;
import java.util.Map;

public interface DynamoDBRepository {

    Map<String, AttributeValue> getValueByKey(String tableName, Map<String, AttributeValue> expressionAttributeValues);

    List<Map<String, AttributeValue>> batchGetValueByKey(String tableName, List<Map<String, AttributeValue>> keyMapList);
}
