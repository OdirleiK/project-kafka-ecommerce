package br.com.kmpx.projectkafkaecommerce;

import java.util.HashMap;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class EmailService {
	
	public static void main(String[] args) {
		var emailService = new EmailService();
		try (var service = new KafkaService<>(String.class.getSimpleName(), 
									   "ECOMMERCE_SEND_EMAIL", 
									   emailService::parse,
									   Email.class,
									   new HashMap<>())) {
			service.run();
		}

	}
	
	private void parse(ConsumerRecord<String, Message<Email>> record) {
		System.out.println("=========================================");
		System.out.println("Send email");
		System.out.println(record.key());
		System.out.println(record.value());
		System.out.println(record.partition());
		System.out.println(record.offset());
		try {
			Thread.sleep(1000);
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Email sent");	
	}
		

	

}
