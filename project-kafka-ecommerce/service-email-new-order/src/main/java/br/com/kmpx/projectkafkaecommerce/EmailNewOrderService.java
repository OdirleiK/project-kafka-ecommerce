package br.com.kmpx.projectkafkaecommerce;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import br.com.kmpx.projectkafkaecommerce.consumer.ConsumerService;
import br.com.kmpx.projectkafkaecommerce.consumer.KafkaService;
import br.com.kmpx.projectkafkaecommerce.consumer.ServiceRunner;
import br.com.kmpx.projectkafkaecommerce.dispatcher.KafkaDispatcher;

public class EmailNewOrderService implements ConsumerService<Order> {

	public static void main(String[] args)  {
		new ServiceRunner<>(EmailNewOrderService::new).start(1);
	}
	
	private final KafkaDispatcher<String> emailNewOrderDispathcer = new KafkaDispatcher<>();
	
	public void parse(ConsumerRecord<String, Message<Order>> record) throws InterruptedException, ExecutionException {
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
	
	@Override
	public String getConsumerGroup() {
		return EmailNewOrderService.class.getSimpleName();
	}

	@Override
	public String getTopic() {
		return "ECOMMERCE_NEW_ORDER";
	}

}
