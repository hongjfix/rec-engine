package com.hackathon.recengine.service;

import java.util.List;
import java.util.Map;

public interface IFeatureService {

    List<String> getUserFeatures(String userId);

    Map<String, List<String>> getItemFeatures(List<String> itemList);

}
