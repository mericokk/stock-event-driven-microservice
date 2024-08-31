package com.stockservice.app.factory;

import org.springframework.stereotype.Component;

import com.stockservice.app.event.StockCreatedEvent;
import com.stockservice.app.event.StockDeletedEvent;
import com.stockservice.app.event.StockUpdatedEvent;
import com.stockservice.app.model.Stock;

@Component
public class StockEventFactory {

	public StockCreatedEvent createStockCreatedEvent(Stock stock) {
		return new StockCreatedEvent(stock.getName(), stock.getPrice(), stock.getQuantity());
	}

	public StockUpdatedEvent createStockUpdatedEvent(Stock stock) {
		return new StockUpdatedEvent(stock.getName(), stock.getPrice(), stock.getQuantity());
	}

	public StockDeletedEvent createStockDeletedEvent(Stock stock) {
		return new StockDeletedEvent(stock.getName());
	}

}
