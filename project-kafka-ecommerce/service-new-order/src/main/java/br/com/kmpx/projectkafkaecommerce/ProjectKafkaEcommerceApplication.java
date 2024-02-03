package br.com.kmpx.projectkafkaecommerce;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.kmpx.projectkafkaecommerce.dispatcher.KafkaDispatcher;

@SpringBootApplication
public class ProjectKafkaEcommerceApplication {

	private static final String NAME_CLASS = ProjectKafkaEcommerceApplication.class.getSimpleName();
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		try (var orderDispatcher = new KafkaDispatcher<Order>()) {
			try (var emailDispatcher = new KafkaDispatcher<String>()) {
				for(var i = 0; i < 5; i++) {
					var orderId = UUID.randomUUID().toString();
					var amount = new BigDecimal(Math.random() * 5000 + 1);
					var email = Math.random() + "@email.com";
					
					var order = new Order(orderId, amount, email);
					var emailCode = "processing your order Thank you for your order! We are processing your order!";
					
					var id = new CorrelationId(ProjectKafkaEcommerceApplication.class.getSimpleName());
					//var email = "Thank you for your order! We are processing your order!";
					orderDispatcher.send("ECOMMERCE_NEW_ORDER", email, id, order);
					emailDispatcher.send("ECOMMERCE_SEND_EMAIL", email, id, emailCode);
				}
			}
		}

	}

}
