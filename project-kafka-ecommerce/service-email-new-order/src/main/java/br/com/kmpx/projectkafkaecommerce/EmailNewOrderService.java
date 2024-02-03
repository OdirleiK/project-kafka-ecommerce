package br.com.kmpx.projectkafkaecommerce;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import br.com.kmpx.projectkafkaecommerce.consumer.KafkaService;
import br.com.kmpx.projectkafkaecommerce.dispatcher.KafkaDispatcher;

public class EmailNewOrderService {

	public static void main(String[] args) throws ExecutionException, InterruptedException {
		var emailService = new EmailNewOrderService();
		try(var service = new KafkaService<>(EmailNewOrderService.class.getSimpleName(), 
									  	   "ECOMMERCE_NEW_ORDER", 
									  	    emailService::parse,
									        new HashMap<>())) {
			service.run();
		}
	}
	
	private final KafkaDispatcher<String> emailNewOrderDispathcer = new KafkaDispatcher<>();
	
	private void parse(ConsumerRecord<String, Message<Order>> record) throws InterruptedException, ExecutionException {
		System.out.println("=========================================");
		System.out.println("Processing new order, preparing email");
		System.out.println(record.key());
		System.out.println(record.value());
		
		var message = record.value();
		
		var order = message.getPayLoad();
		var id = message.getId().continueWith(EmailNewOrderService.class.getSimpleName());
		var emailCode = "processing your order Thank you for your order! We are processing your order!";
		emailNewOrderDispathcer.send("ECOMMERCE_SEND_EMAIL", 
									 order.getEmail(), 
									 id, 
									 emailCode);
		
	}
}
