package com.pmspod.pcalculator.controller;

import com.pmspod.pcalculator.dto.PositionDto;
import com.pmspod.pcalculator.dto.PositionResponse;
import com.pmspod.pcalculator.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
