package com.stockservice.app.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.stockservice.app.event.OrderCreatedEvent;
import com.stockservice.app.event.OrderDeletedEvent;
import com.stockservice.app.handler.OrderEventHandler;

@Component
public class OrderEventConsumer {

	private final OrderEventHandler orderEventHandler;

	public OrderEventConsumer(OrderEventHandler orderEventHandler) {
		this.orderEventHandler = orderEventHandler;
	}

	@KafkaListener(topics = "order-created", groupId = "stock-service-group")
	public void listenOrderCreated(OrderCreatedEvent orderCreatedEvent) {
		orderEventHandler.handleOrderCreated(orderCreatedEvent);
	}

	@KafkaListener(topics = "order-deleted", groupId = "stock-service-group")
	public void listenOrderDeleted(OrderDeletedEvent orderDeletedEvent) {
		orderEventHandler.handleOrderDeleted(orderDeletedEvent);
	}

}