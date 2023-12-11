package com.hackathon.recengine.service;

import com.hackathon.recengine.repository.DynamoDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.sagemakerruntime.SageMakerRuntimeClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class RecallService implements IRecallService{

    private DynamoDBRepository dynamoDBRepository;

    @Autowired
    public RecallService(DynamoDBRepository dynamoDBRepository) {
        this.dynamoDBRepository = dynamoDBRepository;
    }

    @Override
    public List<String> getAllCandidates(String userId){
        Map<String, AttributeValue> res = dynamoDBRepository.getValueByKey("rec_recall_table",
                Map.of("user_id", AttributeValue.builder().s(userId).build()));

        if (res != null && !res.isEmpty()) {
            AttributeValue itemListAttribute = res.get("item_list");
            if (itemListAttribute != null) {
                return Arrays.asList(itemListAttribute.s().split(","));
            } else {
                System.out.println("Item List not found or not a list for the User ID: " + userId);
            }
        } else {
            System.out.println("User ID: " + userId + " not found in the table.");
        }

        return new ArrayList<>();
    }
}
