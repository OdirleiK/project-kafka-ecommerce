package br.com.kmpx.projectkafkaecommerce;

import java.util.UUID;

public class CorrelationId {

	private final String id;
	
	public CorrelationId() {
		id =  UUID.randomUUID().toString();
	}

	@Override
	public String toString() {
		return "CorrelationId [id=" + id + "]";
	}
	
}
