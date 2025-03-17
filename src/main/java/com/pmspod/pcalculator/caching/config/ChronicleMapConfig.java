package com.pmspod.pcalculator.caching.config;

import com.pmspod.pcalculator.dto.PositionDto;
import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;

@Configuration
public class ChronicleMapConfig {

    @Bean
    public ChronicleMap<String, PositionDto> chronicleMap() throws IOException {
        File file = new File("./position-map.dat");
        return ChronicleMapBuilder
                .of(String.class, PositionDto.class)
                .name("my-chronicle-map")
                .entries(100)
                .averageKeySize(90)
                .averageValueSize(500)
                .createOrRecoverPersistedTo(file);
    }
}
