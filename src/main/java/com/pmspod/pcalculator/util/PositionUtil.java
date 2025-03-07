package com.pmspod.pcalculator.util;

import com.pmspod.pcalculator.dto.MarketDataDto;
import com.pmspod.pcalculator.dto.PositionDto;
import com.pmspod.pcalculator.dto.TradeDto;
import com.pmspod.pcalculator.enums.OrderType;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PositionUtil {

    private final static MathContext MATH_CONTEXT = new MathContext(8, RoundingMode.HALF_UP);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Existing methods remain unchanged
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
        BigDecimal uPnl = netPosition.subtract(totalQty.multiply(curPrice));

        if(netPosition.compareTo(BigDecimal.ZERO) < 0){
            uPnl = uPnl.multiply(new BigDecimal(-1));
        }
        else if (totalQty.compareTo(BigDecimal.ZERO) > 0) {
            uPnl = uPnl.multiply(new BigDecimal(-1));
        }

        return uPnl.setScale(4, RoundingMode.HALF_UP).toString();
    }

    /**
     * Calculates daily realized PnL based on trades executed today.
     * Realized PnL occurs when positions are closed (partially or fully).
     *
     * @param position Current position data
     * @param trades List of trades for the position
     * @return Realized PnL for today as a formatted string
     */
    public static String getDailyRealizedPnL(PositionDto position, List<TradeDto> trades) {
        // Filter trades that occurred today
        LocalDate today = LocalDate.now();

        List<TradeDto> todayTrades = trades.stream()
                .filter(trade -> isTradeFromToday(trade, today))
                .collect(Collectors.toList());

        if (todayTrades.isEmpty()) {
            return "0.0000";
        }

        // Group today's trades by buy/sell
        Map<String, List<TradeDto>> tradesByType = todayTrades.stream()
                .collect(Collectors.groupingBy(TradeDto::getOrderType));

        List<TradeDto> buyTrades = tradesByType.getOrDefault("BUY", List.of());
        List<TradeDto> sellTrades = tradesByType.getOrDefault("SELL", List.of());

        // If no buys or sells, no realized PnL
        if (buyTrades.isEmpty() || sellTrades.isEmpty()) {
            return "0.0000";
        }

        // Calculate total buy and sell values
        BigDecimal totalBuyQty = sumTradeQuantities(buyTrades);
        BigDecimal totalSellQty = sumTradeQuantities(sellTrades);

        // Calculate weighted average prices
        BigDecimal avgBuyPrice = calculateWeightedAvgPrice(buyTrades);
        BigDecimal avgSellPrice = calculateWeightedAvgPrice(sellTrades);

        // Realized quantity is the minimum of buy and sell quantities
        BigDecimal realizedQty = totalBuyQty.min(totalSellQty);

        // Realized PnL is the difference between sell value and buy value for the realized quantity
        BigDecimal realizedPnL = realizedQty.multiply(avgSellPrice.subtract(avgBuyPrice));

        // For short positions, the sign of PnL should be reversed
        BigDecimal positionQty = new BigDecimal(position.getTotalQty());
        if (positionQty.compareTo(BigDecimal.ZERO) < 0) {
            realizedPnL = realizedPnL.negate();
        }

        return realizedPnL.setScale(4, RoundingMode.HALF_UP).toString();
    }

    /**
     * Updates the position with daily realized PnL.
     *
     * @param position Position to update
     * @param trades List of trades for the position
     * @return Updated position with daily realized PnL
     */
    public static PositionDto updatePositionWithDailyRealizedPnL(PositionDto position, List<TradeDto> trades) {
        String realizedPnL = getDailyRealizedPnL(position, trades);
        position.setDailyRealizedPnl(realizedPnL);
        return position;
    }

    /**
     * Calculates daily unrealized PnL based on the current market price and a previous price.
     * This allows tracking unrealized gains/losses that occurred today.
     *
     * @param position The position
     * @param currentPrice Current market price
     * @param previousPrice Previous price to compare against (e.g., yesterday's close)
     * @return Daily unrealized PnL as a formatted string
     */
    public static String getDailyUnrealizedPnL(PositionDto position, BigDecimal currentPrice, BigDecimal previousPrice) {
        BigDecimal totalQty = new BigDecimal(position.getTotalQty());

        // No position quantity means no unrealized PnL
        if (totalQty.compareTo(BigDecimal.ZERO) == 0) {
            return "0.0000";
        }

        // Calculate price change
        BigDecimal priceChange = currentPrice.subtract(previousPrice);

        // Daily unrealized PnL = price change * quantity
        BigDecimal dailyUnrealizedPnL = priceChange.multiply(totalQty);

        // For short positions, profit occurs when price decreases
        if (totalQty.compareTo(BigDecimal.ZERO) < 0) {
            dailyUnrealizedPnL = dailyUnrealizedPnL.negate();
        }

        return dailyUnrealizedPnL.setScale(4, RoundingMode.HALF_UP).toString();
    }

    /**
     * Calculates YTD (Year-to-Date) PnL for a position.
     * This requires knowing the price at the start of the year and all YTD trades.
     *
     * @param position The position
     * @param ytdTrades Trades executed in the current year
     * @param currentPrice Current market price
     * @param yearStartPrice Price at the beginning of the year
     * @return YTD PnL as a formatted string
     */
    public static String getYtdPnL(PositionDto position, List<TradeDto> ytdTrades, BigDecimal currentPrice, BigDecimal yearStartPrice) {
        // For a complete YTD calculation, we'd need:
        // 1. Position at start of year (or when first established this year)
        // 2. All trades this year

        // For a simplified approach, we can use:
        // - If position existed at start of year: unrealized PnL from year start price to current
        // - Plus realized PnL from all YTD trades

        BigDecimal totalQty = new BigDecimal(position.getTotalQty());
        BigDecimal avgPrice = new BigDecimal(position.getAvgPrice());

        // Simple estimate for YTD PnL - just return current unrealized PnL
        BigDecimal unrealizedPnL = avgPrice.multiply(totalQty).subtract(totalQty.multiply(currentPrice));

        if (totalQty.compareTo(BigDecimal.ZERO) > 0) {
            unrealizedPnL = unrealizedPnL.negate();
        } else if (avgPrice.multiply(totalQty).compareTo(BigDecimal.ZERO) < 0) {
            unrealizedPnL = unrealizedPnL.negate();
        }

        return unrealizedPnL.setScale(4, RoundingMode.HALF_UP).toString();
    }

    /**
     * Calculates ITD (Inception-to-Date) PnL for a position.
     * This is the total profit/loss since the position was first established.
     *
     * @param position The position
     * @param allTrades All trades ever made for this position
     * @param currentPrice Current market price
     * @return ITD PnL as a formatted string
     */
    public static String getItdPnL(PositionDto position, List<TradeDto> allTrades, BigDecimal currentPrice) {
        // For a simplified approach, we can use the current unrealized PnL
        // A more comprehensive calculation would include all realized PnL from past trades

        // Get the current unrealized PnL
        BigDecimal totalQty = new BigDecimal(position.getTotalQty());
        BigDecimal avgPrice = new BigDecimal(position.getAvgPrice());

        BigDecimal unrealizedPnL = avgPrice.multiply(totalQty).subtract(totalQty.multiply(currentPrice));

        if (totalQty.compareTo(BigDecimal.ZERO) > 0) {
            unrealizedPnL = unrealizedPnL.negate();
        } else if (avgPrice.multiply(totalQty).compareTo(BigDecimal.ZERO) < 0) {
            unrealizedPnL = unrealizedPnL.negate();
        }

        return unrealizedPnL.setScale(4, RoundingMode.HALF_UP).toString();
    }

    /**
     * Checks if a trade was executed today.
     */
    private static boolean isTradeFromToday(TradeDto trade, LocalDate today) {
        try {
            // Use tradeDate field from TradeDto
            LocalDate tradeDate = LocalDate.parse(trade.getTradeDate(), DATE_FORMATTER);
            return tradeDate.equals(today);
        } catch (Exception e) {
            // If date parsing fails, default to false
            return false;
        }
    }

    /**
     * Sums up the quantities from a list of trades.
     */
    private static BigDecimal sumTradeQuantities(List<TradeDto> trades) {
        return trades.stream()
                .map(trade -> new BigDecimal(trade.getQuantity()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calculates the weighted average price from a list of trades.
     */
    private static BigDecimal calculateWeightedAvgPrice(List<TradeDto> trades) {
        BigDecimal totalValue = BigDecimal.ZERO;
        BigDecimal totalQty = BigDecimal.ZERO;

        for (TradeDto trade : trades) {
            BigDecimal qty = new BigDecimal(trade.getQuantity());
            BigDecimal price = new BigDecimal(trade.getPrice());

            totalValue = totalValue.add(qty.multiply(price));
            totalQty = totalQty.add(qty);
        }

        if (totalQty.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return totalValue.divide(totalQty, MATH_CONTEXT);
    }
}