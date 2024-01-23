package br.com.kmpx.projectkafkaecommerce;

import java.math.BigDecimal;

public class Order {

	private final String userId;
	private final String orderId;
	private final BigDecimal amount;
	
	public Order(String userId, String orderId, BigDecimal amount) {
		super();
		this.userId = userId;
		this.orderId = orderId;
		this.amount = amount;
	}
	
	
}