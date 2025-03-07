package com.pmspod.pcalculator.controller;

import com.pmspod.pcalculator.dto.PositionDto;
import com.pmspod.pcalculator.dto.PositionResponse;
import com.pmspod.pcalculator.service.PositionService;
import com.pmspod.pcalculator.service.impl.PositionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/positions")
public class PositionController {

    @Autowired
    private PositionService positionService;

    @GetMapping("/download")
    public ResponseEntity<PositionResponse> downloadPositions() {
        List<PositionDto> positionData = positionService.getAllPositions();

        PositionResponse response = new PositionResponse();
        response.setPositionData(positionData);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/create-test")
    public ResponseEntity<PositionResponse> createTestPositions() {
        System.out.println("Starting create-test endpoint");

        // Create test positions for tickers we know exist in the MDS feed
        PositionDto applePosition = new PositionDto();
        applePosition.setPositionId("pos-aapl-001");
        applePosition.setTicker("AAPL");
        applePosition.setTotalQty("100");
        applePosition.setAvgPrice("350.00");
        applePosition.setCurrency("USD");

        System.out.println("Created AAPL position");

        PositionDto ibmPosition = new PositionDto();
        ibmPosition.setPositionId("pos-ibm-001");
        ibmPosition.setTicker("IBM");
        ibmPosition.setTotalQty("50");
        ibmPosition.setAvgPrice("80.00");
        ibmPosition.setCurrency("USD");

        System.out.println("Created IBM position");

        // Try/catch for each reflection step to isolate where the error occurs
        try {
            System.out.println("Getting positionCacheService field");
            Field field = positionService.getClass().getDeclaredField("positionCacheService");
            field.setAccessible(true);

            System.out.println("Getting positionCacheService instance");
            Object positionCacheService = field.get(positionService);

            System.out.println("PositionCacheService class: " + positionCacheService.getClass().getName());

            System.out.println("Getting cache field");
            Field cacheField = positionCacheService.getClass().getDeclaredField("positionCache");
            cacheField.setAccessible(true);

            System.out.println("Getting cache instance");
            Map<String, PositionDto> cache = (Map<String, PositionDto>) cacheField.get(positionCacheService);

            System.out.println("Adding positions to cache");
            cache.put(applePosition.getPositionId(), applePosition);
            cache.put(ibmPosition.getPositionId(), ibmPosition);

            System.out.println("Added test positions: AAPL and IBM");
        } catch (Exception e) {
            System.out.println("Error in reflection: " + e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }

        // Return the positions
        System.out.println("Getting all positions");
        List<PositionDto> positionData = positionService.getAllPositions();

        System.out.println("Creating response");
        PositionResponse response = new PositionResponse();
        response.setPositionData(positionData);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/test-simple")
    public ResponseEntity<PositionResponse> testSimple() {
        // Create test positions
        PositionDto applePosition = new PositionDto();
        applePosition.setPositionId("pos-aapl-001");
        applePosition.setTicker("AAPL");
        applePosition.setTotalQty("100");
        applePosition.setAvgPrice("350.00");
        applePosition.setCurrency("USD");

        // Print some info for debugging
        System.out.println("Created test position: " + applePosition.getTicker());

        // Return a simple response with just this position
        List<PositionDto> positionList = new ArrayList<>();
        positionList.add(applePosition);

        PositionResponse response = new PositionResponse();
        response.setPositionData(positionList);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/test-calc")
    public ResponseEntity<PositionResponse> testCalculations() {
        // Create test position
        PositionDto applePosition = new PositionDto();
        applePosition.setPositionId("pos-aapl-001");
        applePosition.setTicker("AAPL");
        applePosition.setTotalQty("100");
        applePosition.setAvgPrice("350.00");
        applePosition.setCurrency("USD");

        // Cast to implementation class to access the method
        ((PositionServiceImpl) positionService).updatePosition(applePosition);

        // Create response with the updated position
        List<PositionDto> positionList = new ArrayList<>();
        positionList.add(applePosition);

        PositionResponse response = new PositionResponse();
        response.setPositionData(positionList);

        return ResponseEntity.ok(response);
    }


}
