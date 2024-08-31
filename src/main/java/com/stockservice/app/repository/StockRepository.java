package com.stockservice.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stockservice.app.model.Stock;

public interface StockRepository extends JpaRepository<Stock, Long> {

}