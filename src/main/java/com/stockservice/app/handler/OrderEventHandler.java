package com.stockservice.app.handler;

import org.springframework.stereotype.Service;

import com.stockservice.app.event.OrderCreatedEvent;
import com.stockservice.app.event.OrderDeletedEvent;
import com.stockservice.app.service.StockService;

@Service
public class OrderEventHandler {

	private final StockService stockService;

	public OrderEventHandler(StockService stockService) {
		this.stockService = stockService;
	}

	public void handleOrderCreated(OrderCreatedEvent orderCreatedEvent) {
		stockService.adjustStock(orderCreatedEvent.getStockId(), -orderCreatedEvent.getQuantity());
	}

	public void handleOrderDeleted(OrderDeletedEvent orderDeletedEvent) {
		stockService.adjustStock(orderDeletedEvent.getStockId(), orderDeletedEvent.getQuantity());
	}

}
