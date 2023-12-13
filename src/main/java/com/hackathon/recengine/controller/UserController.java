package com.hackathon.recengine.controller;

import com.hackathon.recengine.model.Item;
import com.hackathon.recengine.service.IFeatureService;
import com.hackathon.recengine.service.IRankService;
import com.hackathon.recengine.service.IRecallService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class UserController {

    private IRecallService recallService;

    private IRankService rankService;

    private IFeatureService featureService;

    @Autowired
    public UserController(@Qualifier("SageMakerService") IRankService rankService,
                          IRecallService recallService,
                          IFeatureService featureService) {
        this.rankService = rankService;
        this.recallService = recallService;
        this.featureService = featureService;
    }

    @GetMapping("/api/item_list")
    @Operation(summary = "Get a list of rec item list by userId", description = "Returns a list of items based on the provided userId.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list"),
    })
    public List<Item> getUserList(@RequestParam String userId) {
        List<Item> resList = new ArrayList<>();
        List<String> itemList = new ArrayList<>();
        List<Double> scoreList = new ArrayList<>();
        try {
            //recall
            itemList = recallService.getAllCandidates("all");
            System.out.println("itemList: " + itemList.toString());

            //get features
            List<String> userFeatures = featureService.getUserFeatures(userId);
            System.out.println("userFeatures: " + userFeatures.toString());
            Map<String, List<String>> itemFeatures = featureService.getItemFeatures(itemList);
            System.out.println("itemFeatures: " + itemFeatures.toString());
            List<List<String>> featureSets = createFeatureSets(userFeatures, itemFeatures);
            System.out.println("featureSets: " + featureSets);

            //rank
            scoreList = rankService.batchInference(featureSets);
            System.out.println(scoreList);

            resList = createItemList(new ArrayList<>(itemFeatures.keySet()), scoreList);
            } catch (Exception e) {
                System.out.println(e);
                return new ArrayList<>();
        }
        System.out.println("resList: " + resList.toString());

        return resList;
    }

    private List<Item> createItemList(List<String> itemIdList, List<Double> scoreList) {
        List<Item> items = new ArrayList<>();
        int minLength = Math.min(itemIdList.size(), scoreList.size());
        for (int i = 0; i < minLength; i++) {
            String itemId = itemIdList.get(i);
            Double score = scoreList.get(i);
            items.add(new Item(itemId, score));
        }
        Collections.sort(items, Comparator.comparing(Item::getScore).reversed());
        return items;
    }

    private List<List<String>> createFeatureSets(List<String> userFeatures,
                                                 Map<String, List<String>> itemFeatures){
        List<List<String>> featureSets = new ArrayList<>();

        for (Map.Entry<String, List<String>> entry : itemFeatures.entrySet()) {
            List<String> itemFeatureList = entry.getValue();
            List<String> featureSet = new ArrayList<>();
            featureSet.addAll(userFeatures);
            featureSet.addAll(itemFeatureList);
            featureSets.add(featureSet);
        }
        return featureSets;
    }
}