package com.pmspod.pcalculator.service;

import com.pmspod.pcalculator.dto.PositionDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PositionService {

    public List<PositionDto> getAllPositions();

}
