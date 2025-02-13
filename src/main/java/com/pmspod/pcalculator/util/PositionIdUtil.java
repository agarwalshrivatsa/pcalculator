package com.pmspod.pcalculator.util;

import java.util.UUID;

public class PositionIdUtil {

    public static String generatePositionId() {
        return UUID.randomUUID().toString();
    }
}
