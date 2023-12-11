package com.hackathon.recengine.service;

import com.hackathon.recengine.repository.DynamoDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.*;

@Service
public class FeatureService implements IFeatureService{

    private DynamoDBRepository dynamoDBRepository;

    @Autowired
    public FeatureService(DynamoDBRepository dynamoDBRepository) {
        this.dynamoDBRepository = dynamoDBRepository;
    }

    @Override
    public List<String> getUserFeatures(String userId) {

        Map<String, AttributeValue> res = dynamoDBRepository.getValueByKey("user_feature_table",
                Map.of("user_id", AttributeValue.builder().s(userId).build()));

        if (res != null && !res.isEmpty()) {
            AttributeValue itemListAttribute = res.get("features");
            if (itemListAttribute != null) {
                return Arrays.asList(itemListAttribute.s().split(","));
            } else {
                System.out.println("User features not found or not a list for the User ID: " + userId);
            }
        } else {
            System.out.println("User ID: " + userId + " not found in the table.");
        }

        return new ArrayList<>();
    }

    @Override
    public Map<String, List<String>> getItemFeatures(List<String> itemList) {

        Map<String, List<String>> mapRes = new HashMap<>();
        List<Map<String, AttributeValue>> attrList = new ArrayList<>();
        for (String item : itemList) {
            attrList.add(Map.of("item_id", AttributeValue.builder().s(item).build()));
        }

        List<Map<String, AttributeValue>> resList = dynamoDBRepository.batchGetValueByKey("item_feature_table",
                attrList);

        if (resList != null && !resList.isEmpty()) {
            for(Map<String, AttributeValue> res : resList){
                AttributeValue itemListAttribute = res.get("features");
                if (itemListAttribute != null) {
                    mapRes.put(res.get("item_id").s(), Arrays.asList(itemListAttribute.s().split(",")));
                } else {
                    System.out.println("Item features not found or not a list for the Item IDs: " + itemList.toString());
                }
            }
        } else {
            System.out.println("Item IDs: " + itemList.toString() + " not found in the table.");
        }
        return mapRes;
    }
}
