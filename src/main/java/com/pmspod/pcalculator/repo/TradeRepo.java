package com.pmspod.pcalculator.repo;

import com.pmspod.pcalculator.entity.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeRepo extends JpaRepository<Trade, String> {
}
