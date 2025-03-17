package com.pmspod.pcalculator.util;

import com.pmspod.pcalculator.dto.MarketDataDto;
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

        /*
        if new total qty is positive
            trade type = buy:
                if oldPrice is positive, do normal calculation
                else newQty- oldQty * newPrice
            trade type = sell:
                return oldPrice
        else if new total qty is negative t
            trade type = sell
                if oldPrice is negative, do normal calculation
                else newQty - oldQty * newPrice
            trade type = buy:
                return oldPrice
        else
            return 0


         */
        BigDecimal oldQty = new BigDecimal(existingPosition.getTotalQty());
        OrderType orderType = OrderType.valueOf(trade.getOrderType());
        BigDecimal tradeQty = new BigDecimal(trade.getQuantity());
        BigDecimal oldPrice = new BigDecimal(existingPosition.getAvgPrice());
        BigDecimal newPrice = new BigDecimal(trade.getPrice());

        BigDecimal totalQty = getTotalQtyDecimal(oldQty, tradeQty, orderType);
        BigDecimal result = new BigDecimal(0);
        System.out.println("Old qty: " + oldQty.toString() + " orderType: "  + orderType.toString()+ " newQty: " +tradeQty.toString() + " Total qty: " +  totalQty.toString());
        if(totalQty.compareTo(BigDecimal.ZERO) > 0){
            if(orderType.equals(OrderType.BUY)){
                if(oldQty.compareTo(BigDecimal.ZERO) >= 0){
                    result = (oldPrice.multiply(oldQty).add(newPrice.multiply(tradeQty))).divide(oldQty.add(tradeQty), MATH_CONTEXT);
                }
                else {
                    result = newPrice;
                }
            }
            else{
                result = oldPrice;
            }
        }
        else if(totalQty.compareTo(BigDecimal.ZERO) < 0){
            if(orderType.equals(OrderType.SELL)){
                if(oldQty.compareTo(BigDecimal.ZERO) <= 0){
                    result = (oldPrice.multiply(oldQty).subtract(newPrice.multiply(tradeQty))).divide(oldQty.subtract(tradeQty), MATH_CONTEXT);
                }
                else{
                    result = newPrice;
                }

            }
            else{
                result = oldPrice;
            }

        }
        return result.abs().setScale(4, RoundingMode.HALF_UP).toString();

    }

    public static String getUnrealizedPnL(PositionDto positionDto, MarketDataDto md){

        BigDecimal avgPrice = new BigDecimal(positionDto.getAvgPrice());
        BigDecimal totalQty = new BigDecimal(positionDto.getTotalQty());

        BigDecimal netPosition = avgPrice.multiply(totalQty);

        BigDecimal curPrice = BigDecimal.valueOf(md.getTradePrice());

        BigDecimal uPnl =  new BigDecimal(0);
        BigDecimal newPosition = totalQty.multiply(curPrice);
        uPnl = newPosition.subtract(netPosition);
        if(netPosition.compareTo(BigDecimal.ZERO) < 0){
            uPnl = uPnl.multiply(new BigDecimal(-1));
        }

        return uPnl.setScale(4, RoundingMode.HALF_UP).toString();

    }


}
