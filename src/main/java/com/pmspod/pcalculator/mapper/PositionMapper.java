package com.pmspod.pcalculator.mapper;

import com.pmspod.pcalculator.dto.PositionDto;
import com.pmspod.pcalculator.entity.Position;

public class PositionMapper {

    public static Position map(PositionDto positionDto){

        Position position = new Position();
        position.setTicker(positionDto.getTicker());
        position.setTotalQty(positionDto.getTotalQty());
        position.setAvgPrice(positionDto.getAvgPrice());
        position.setCurrency(positionDto.getCurrency());
        position.setPositionId(positionDto.getPositionId());
        return position;

    }
}
