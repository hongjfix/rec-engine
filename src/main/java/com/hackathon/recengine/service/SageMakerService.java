package com.hackathon.recengine.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.sagemakerruntime.SageMakerRuntimeClient;
import software.amazon.awssdk.services.sagemakerruntime.model.InvokeEndpointRequest;
import software.amazon.awssdk.services.sagemakerruntime.model.InvokeEndpointResponse;

import java.util.ArrayList;
import java.util.List;

@Service("SageMakerService")
public class SageMakerService implements IRankService{

    private final SageMakerRuntimeClient sageMakerRuntimeClient;

    private final String endpointName;

    @Autowired
    public SageMakerService(SageMakerRuntimeClient sageMakerRuntimeClient, @Value("${sagemaker.endpoint.name}") String endpointName) {
        this.sageMakerRuntimeClient = sageMakerRuntimeClient;
        this.endpointName = endpointName;
    }

    @Override
    public List<Double> batchInference(List<List<String>> featureSets) throws JsonProcessingException {
        InvokeEndpointRequest request = InvokeEndpointRequest.builder()
                .endpointName(endpointName)
                .contentType("application/json")
                .body(SdkBytes.fromUtf8String(featureSetsToJson(featureSets)))
                .build();

        InvokeEndpointResponse response = sageMakerRuntimeClient.invokeEndpoint(request);
        System.out.println(response.body().toString());
        return extractResults(response);
    }

    private String featureSetsToJson(List<List<String>> featureSets) {
        StringBuilder jsonBuilder = new StringBuilder("{\"instances\": [");

        for (List<String> features : featureSets) {
            String featureString = String.join("\", \"", features);
            jsonBuilder.append("{\"features\": [\"").append(featureString).append("\"]},");
        }

        jsonBuilder.deleteCharAt(jsonBuilder.length() - 1);
        jsonBuilder.append("]}");
        System.out.println(jsonBuilder.toString());
        return jsonBuilder.toString();
    }

    private List<Double> extractResults(InvokeEndpointResponse response) throws JsonProcessingException {
        SdkBytes responseBody = response.body();
        String resultJson = responseBody.asUtf8String();
        System.out.println(resultJson);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(resultJson);

        JsonNode predictionsArray = jsonNode.path("predictions");

        List<Double> scoreList = new ArrayList<>();
        for (JsonNode predictionNode : predictionsArray) {
            double score = predictionNode.path("score").asDouble();
            scoreList.add(score);
        }
        return scoreList;
    }
}
