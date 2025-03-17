package com.pmspod.pcalculator.caching.initializer;

import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.io.File;

public class CacheInitializer implements ApplicationRunner {

    

    @Override
    public void run(ApplicationArguments args) throws Exception {

        File file = new File("path/to/your/chronicle-map.dat");

        // Create a ChronicleMap instance
        ChronicleMap<String, String> chronicleMap = ChronicleMapBuilder
                .of(String.class, String.class)
                .name("position-map")
                .entries(1000) // Number of entries you expect to store
                .averageKey("key") // Average key size
                .averageValue("value") // Average value size
                .createOrRecoverPersistedTo(file); // Persist to disk

    }
}
