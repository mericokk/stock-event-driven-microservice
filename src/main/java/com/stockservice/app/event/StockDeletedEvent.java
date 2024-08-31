package com.stockservice.app.event;

public class StockDeletedEvent extends BaseEvent {

	private String name;

	public StockDeletedEvent(String name) {
		super();
		this.name = name;
	}

	public StockDeletedEvent() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
