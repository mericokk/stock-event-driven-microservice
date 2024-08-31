package com.stockservice.app.exception;

public class StockNotFoundException extends RuntimeException {

	public StockNotFoundException(Long stockId) {
		super(String.format("Could not find stock with ID: %d", stockId));
	}

}