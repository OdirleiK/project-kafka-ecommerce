package br.com.kmpx.projectkafkaecommerce;

import java.math.BigDecimal;

public class Order {

	private final String orderId;
	private final BigDecimal amount;
	private final String email;
	
	public Order(String orderId, BigDecimal amount, String email) {
		super();
		this.orderId = orderId;
		this.amount = amount;
		this.email = email;
	}
	
	
}
