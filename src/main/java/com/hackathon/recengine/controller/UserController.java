package com.hackathon.recengine.controller;

import com.hackathon.recengine.model.Item;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class UserController {

    @GetMapping("/api/item_list")
    @Operation(summary = "Get a list of rec item list by userId", description = "Returns a list of items based on the provided userId.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list"),
    })
    public List<Item> getUserList(@RequestParam String userId) {
        List<Item> itemList = getItemListList(userId);
        return itemList;
    }

    private List<Item> getItemListList(String userId) {
        return List.of(
                new Item("1", "item1"),
                new Item("2", "item2"),
                new Item("3", "item3")
        );
    }
}