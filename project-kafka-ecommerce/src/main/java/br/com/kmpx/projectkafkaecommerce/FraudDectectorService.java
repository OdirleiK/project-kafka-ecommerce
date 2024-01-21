package br.com.kmpx.projectkafkaecommerce;

import java.util.HashMap;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class FraudDectectorService {
	
	public static void main(String[] args) {
		var fraudService = new FraudDectectorService();
		try(var service = new KafkaService<>(FraudDectectorService.class.getSimpleName(), 
									  	   "ECOMMERCE_NEW_ORDER", 
									        fraudService::parse,
									        Order.class,
									        new HashMap<>())) {
			service.run();
		}
	}
	
	private void parse(ConsumerRecord<String, Order> record) {
		System.out.println("=========================================");
		System.out.println("Processing new order, checking for fraud");
		System.out.println(record.key());
		System.out.println(record.value());
		System.out.println(record.partition());
		System.out.println(record.offset());
		try {
			Thread.sleep(3000);
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Order processed");	
	}
}
