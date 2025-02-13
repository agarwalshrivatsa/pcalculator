package com.pmspod.pcalculator.util;

import com.pmspod.pcalculator.dto.PositionDto;
import com.pmspod.pcalculator.dto.TradeDto;
import com.pmspod.pcalculator.enums.OrderType;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class PositionUtil {

    private final static MathContext MATH_CONTEXT = new MathContext(8, RoundingMode.HALF_UP);

    public static PositionDto getPositionFromTrade(TradeDto trade) {
        PositionDto position = new PositionDto();
        position.setTicker(trade.getTicker());
        position.setPositionId(PositionIdUtil.generatePositionId());
        position.setTotalQty(getTotalQty(new BigDecimal(0), new BigDecimal(trade.getQuantity()), OrderType.valueOf(trade.getOrderType())));
        position.setAvgPrice(trade.getPrice());
        position.setCurrency(trade.getCurrency());
        position.setLastPrice(trade.getPrice());
        return position;
    }

    public static String getTotalQty(BigDecimal oldQty, BigDecimal tradeQty, OrderType orderType){
        return getTotalQtyDecimal(oldQty, tradeQty, orderType).setScale(4, RoundingMode.HALF_UP).toString();
    }

    public static BigDecimal getTotalQtyDecimal(BigDecimal oldQty, BigDecimal tradeQty, OrderType orderType){
        if(orderType.equals(OrderType.BUY)){
            return oldQty.add(tradeQty);
        }
        return oldQty.subtract(tradeQty);
    }

    //Todo: create new object or update existing?
    public static PositionDto getNewPosition(PositionDto existingPosition, TradeDto trade){
        PositionDto position = new PositionDto();
        position.setTicker(existingPosition.getTicker());
        position.setPositionId(existingPosition.getPositionId());
        position.setTotalQty(getTotalQty(new BigDecimal(existingPosition.getTotalQty()), new BigDecimal(trade.getQuantity()), OrderType.valueOf(trade.getOrderType())));
        position.setAvgPrice(getNewAvgPrice(existingPosition, trade));
        position.setCurrency(existingPosition.getCurrency());
        position.setLastPrice(trade.getPrice());
        return position;
    }

    public static String getNewAvgPrice(PositionDto existingPosition, TradeDto trade){
        BigDecimal oldQty = new BigDecimal(existingPosition.getTotalQty());
        OrderType orderType = OrderType.valueOf(trade.getOrderType());

        if(orderType.equals(OrderType.SELL)){
            return oldQty.setScale(4, RoundingMode.HALF_UP).toString();
        }
        BigDecimal tradeQty = new BigDecimal(trade.getQuantity());
        BigDecimal oldPrice = new BigDecimal(existingPosition.getAvgPrice());
        BigDecimal newPrice = new BigDecimal(trade.getPrice());

        BigDecimal totalQty = getTotalQtyDecimal(oldQty, tradeQty, orderType);
        if(totalQty.compareTo(new BigDecimal(0)) == 0) return "0.0000";
        BigDecimal result = (oldPrice.multiply(oldQty).add(newPrice.multiply(tradeQty))).divide(oldQty.add(tradeQty), MATH_CONTEXT);
        return result.setScale(4, RoundingMode.HALF_UP).toString();
    }

}
