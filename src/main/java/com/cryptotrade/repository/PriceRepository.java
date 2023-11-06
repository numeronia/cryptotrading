package main.java.com.cryptotrade.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceRepository extends JpaRepository<AggregatedPrice, Long> {
    // Custom database operations if needed
}
