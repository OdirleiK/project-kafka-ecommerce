package br.com.kmpx.projectkafkaecommerce;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjectKafkaEcommerceApplication {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		try(var dispatcher = new KafkaDispatcher()) {
			var key = UUID.randomUUID().toString();
			var value = key + ",8989,45321";
			
			dispatcher.send("ECOMMERCE_NEW_ORDER", key, value);
	
			var email = "Thank you for your order! We are processing your order!";
			dispatcher.send("ECOMMERCE_SEND_EMAIL", key, email);
		}
		
	}

}
