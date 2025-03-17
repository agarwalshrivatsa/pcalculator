package com.pmspod.pcalculator.util;

import java.util.UUID;

public class PositionIdUtil {

    public static String generatePositionId() {
        return String.valueOf(Math.abs(UUID.randomUUID().getMostSignificantBits())).substring(0, 10);
    }
}
