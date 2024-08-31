package com.stockservice.app.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stockservice.app.event.StockCreatedEvent;
import com.stockservice.app.event.StockDeletedEvent;
import com.stockservice.app.event.StockUpdatedEvent;
import com.stockservice.app.exception.StockNotFoundException;
import com.stockservice.app.factory.StockEventFactory;
import com.stockservice.app.model.Stock;
import com.stockservice.app.repository.StockRepository;

@Service
public class StockService {

	private final StockRepository stockRepository;
	private final KafkaTemplate<String, Object> kafkaTemplate;
	private final StockEventFactory stockEventFactory;
	private static final Logger logger = LoggerFactory.getLogger(StockService.class);

	public StockService(StockRepository stockRepository, KafkaTemplate<String, Object> kafkaTemplate,
			StockEventFactory stockEventFactory) {
		this.stockRepository = stockRepository;
		this.kafkaTemplate = kafkaTemplate;
		this.stockEventFactory = stockEventFactory;
	}

	@Transactional(readOnly = true)
	public List<Stock> getAllStocks() {
		logger.debug("Fetching all stocks");
		return stockRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Stock getStockById(Long stockId) {
		logger.debug("Fetching stock with ID: {}", stockId);
		Stock stock = stockRepository.findById(stockId).orElseThrow(() -> {
			logger.error("Stock with ID: {} not found", stockId);
			return new StockNotFoundException(stockId);
		});
		logger.debug("Fetched stock: {}", stock);
		return stock;
	}

	@Transactional
	public Stock createStock(Stock newStock) {
		logger.info("Creating new stock: {}", newStock);

		Stock savedStock = stockRepository.save(newStock);
		logger.info("Stock saved with ID: {}", savedStock.getId());

		StockCreatedEvent event = stockEventFactory.createStockCreatedEvent(savedStock);

		try {
			kafkaTemplate.send("stock-created", event);
			logger.info("StockCreatedEvent sent for stock ID: {}", savedStock.getId());
		} catch (Exception e) {
			logger.error("Failed to send StockCreatedEvent for stock ID: {}", savedStock.getId(), e);
		}

		return savedStock;
	}

	@Transactional
	public Stock updateStock(Long stockId, Stock updatedStock) {
		logger.info("Updating stock with ID: {}", stockId);

		Stock existingStock = stockRepository.findById(stockId).orElseThrow(() -> {
			logger.error("Stock with ID: {} not found for update", stockId);
			return new StockNotFoundException(stockId);
		});

		existingStock.setName(updatedStock.getName());
		existingStock.setPrice(updatedStock.getPrice());
		existingStock.setQuantity(updatedStock.getQuantity());

		Stock savedStock = stockRepository.save(existingStock);
		logger.info("Stock updated with ID: {}", savedStock.getId());

		StockUpdatedEvent event = stockEventFactory.createStockUpdatedEvent(savedStock);

		try {
			kafkaTemplate.send("stock-updated", event);
			logger.info("StockUpdatedEvent sent for stock ID: {}", savedStock.getId());
		} catch (Exception e) {
			logger.error("Failed to send StockUpdatedEvent for stock ID: {}", savedStock.getId(), e);
		}

		return savedStock;
	}

	@Transactional
	public void deleteStock(Long stockId) {
		logger.info("Deleting stock with ID: {}", stockId);

		Stock existingStock = stockRepository.findById(stockId).orElseThrow(() -> {
			logger.error("Stock with ID: {} not found for update", stockId);
			return new StockNotFoundException(stockId);
		});

		stockRepository.delete(existingStock);
		logger.info("Stock deleted with ID: {}", stockId);

		StockDeletedEvent event = stockEventFactory.createStockDeletedEvent(existingStock);

		try {
			kafkaTemplate.send("stock-deleted", event);
			logger.info("StockDeletedEvent sent for stock ID: {}", stockId);
		} catch (Exception e) {
			logger.error("Failed to send StockDeletedEvent for stock ID: {}", stockId, e);
		}
	}

	@Transactional
	public void adjustStock(Long stockId, int quantity) {
		logger.info("Adjusting stock with ID: {} by {}", stockId, quantity);

		Stock stock = stockRepository.findById(stockId).orElseThrow(() -> new StockNotFoundException(stockId));

		int newQuantity = stock.getQuantity() + quantity;
		if (newQuantity < 0) {
			logger.error("Insufficient stock for ID: {}. Available quantity: {}", stockId, stock.getQuantity());
			throw new IllegalArgumentException("Insufficient stock");
		}

		stock.setQuantity(newQuantity);
		stockRepository.save(stock);

		logger.info("Stock adjusted with ID: {}. New quantity: {}", stockId, stock.getQuantity());
	}

}
