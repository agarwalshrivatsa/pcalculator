package com.pmspod.pcalculator.repo;

import com.pmspod.pcalculator.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepo extends JpaRepository<Account, String> {

}
