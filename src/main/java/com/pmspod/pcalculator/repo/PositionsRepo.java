package com.pmspod.pcalculator.repo;

import com.pmspod.pcalculator.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionsRepo extends JpaRepository<Position, String> {

}
