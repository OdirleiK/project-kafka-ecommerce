package br.com.kmpx.projectkafkaecommerce;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

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
	private final KafkaDispatcher<Order> orderDispathcer = new KafkaDispatcher<>();
	
	private void parse(ConsumerRecord<String, Order> record) throws InterruptedException, ExecutionException {
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
		
		var order = record.value();
		if(isFraud(order)) {
			//pretending that the fraud happens when the amount is >=4500
			System.out.println("Order is a fraud!");	
			orderDispathcer.send("ECOMMERCE_ORDER_REJECTED", order.getUserId(), order);
		}else {
			System.out.println("Approved: " + order);
			orderDispathcer.send("ECOMMERCE_ORDER_APPROVED", order.getUserId(), order);

		}
	}
	
	private boolean isFraud(Order order) {
		return order.getAmount().compareTo(new BigDecimal("200")) >= 0;
	}
}
