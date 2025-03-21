package com.pmspod.pcalculator.caching.service.impl;

import com.pmspod.pcalculator.caching.service.PositionCacheService;
import com.pmspod.pcalculator.dto.PositionDto;
import lombok.extern.slf4j.Slf4j;
import net.openhft.chronicle.map.ChronicleMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
@Slf4j
public class PositionCacheServiceImpl2 implements PositionCacheService {

    @Autowired
    private ChronicleMap<String, PositionDto> positionChronicleMap;

    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

    private String getKey(String accountId, String ticker){
        return accountId + "-" + ticker;
    }

    @Override
    public PositionDto getPositionByTicker(String accountId, String ticker) {
        rwLock.readLock().lock();
        try{
            return positionChronicleMap.get(getKey(accountId, ticker));
        } catch (Exception e){
            log.error("Error in fetching position from map!");
            throw new RuntimeException("Error in fetching position from map!");
        } finally {
            rwLock.readLock().unlock();
        }
    }

    @Override
    public PositionDto updatePosition(String accountId, String ticker, PositionDto position) {
        rwLock.writeLock().lock();
        try{
            positionChronicleMap.put(getKey(accountId, ticker), position);
            return position;
        } catch (Exception e){
            log.error("Error in updating position in map!");
            throw new RuntimeException("Error in updating position in map: {}", e);
        } finally {
            rwLock.writeLock().unlock();
        }

    }

    @Override
    public List<PositionDto> getAllPositions() {

        rwLock.readLock().lock();
        try{
            return List.copyOf(positionChronicleMap.values());
        } finally {
            rwLock.readLock().unlock();
        }
    }
}
