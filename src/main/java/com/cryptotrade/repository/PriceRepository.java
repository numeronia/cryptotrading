package main.java.com.cryptotrade.repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import main.java.com.cryptotrade.model.Price;
import main.java.com.cryptotrade.model.WalletBalance;

public interface PriceRepository extends JpaRepository<Price, BigDecimal> {
    // Find a wallet balance by symbol
    Optional<Price> findBySymbol(String symbol);

}
