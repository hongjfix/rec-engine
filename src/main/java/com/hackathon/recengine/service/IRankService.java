package com.hackathon.recengine.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface IRankService {

    List<Double> batchInference(List<List<String>> featureSets) throws JsonProcessingException;

}
