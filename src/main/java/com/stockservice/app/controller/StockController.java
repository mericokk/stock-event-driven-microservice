package com.stockservice.app.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stockservice.app.model.Stock;
import com.stockservice.app.service.StockService;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

	private final StockService stockService;
	private static final Logger logger = LoggerFactory.getLogger(StockController.class);

	public StockController(StockService stockService) {
		this.stockService = stockService;
	}

	@GetMapping
	public ResponseEntity<List<Stock>> getAllStocks() {
		logger.info("Received request to get all stocks");
		List<Stock> stocks = stockService.getAllStocks();
		return ResponseEntity.ok(stocks);
	}

	@GetMapping("/{stockId}")
	public ResponseEntity<Stock> getStockById(@PathVariable Long stockId) {
		logger.info("Received request to get stock with ID: {}", stockId);
		Stock stock = stockService.getStockById(stockId);
		return ResponseEntity.ok(stock);
	}

	@PostMapping
	public ResponseEntity<Stock> createStock(@RequestBody Stock newStock) {
		logger.info("Received request to create a new stock: {}", newStock);
		Stock createdStock = stockService.createStock(newStock);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdStock);
	}

	@PutMapping("/{stockId}")
	public ResponseEntity<Stock> updateStock(@RequestBody Stock updatedStock, @PathVariable Long stockId) {
		logger.info("Received request to update stock with ID: {}. New data: {}", stockId, updatedStock);
		Stock stock = stockService.updateStock(stockId, updatedStock);
		return ResponseEntity.ok(stock);
	}

	@DeleteMapping("/{stockId}")
	public ResponseEntity<Void> deleteStock(@PathVariable Long stockId) {
		logger.info("Received request to delete stock with ID: {}", stockId);
		stockService.deleteStock(stockId);
		return ResponseEntity.noContent().build();
	}

}