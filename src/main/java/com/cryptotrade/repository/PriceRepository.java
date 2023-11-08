package com.cryptotrade.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cryptotrade.model.Price;

public interface PriceRepository extends JpaRepository<Price, Long> {
    // Find a wallet balance by symbol
    Price findBySymbol(String symbol);

}
