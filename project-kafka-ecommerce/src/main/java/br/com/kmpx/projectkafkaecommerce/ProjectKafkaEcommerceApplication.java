package br.com.kmpx.projectkafkaecommerce;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjectKafkaEcommerceApplication {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		try (var orderDispatcher = new KafkaDispatcher<Order>()) {
			try (var emailDispatcher = new KafkaDispatcher<Email>()) {
				for(var i = 0; i < 10; i++) {
					var userId = UUID.randomUUID().toString();
					var orderId = UUID.randomUUID().toString();
					var amount = new BigDecimal(Math.random() * 5000 + 1);
					var order = new Order(userId, orderId, amount);
					var email = new Email("processing your order", "Thank you for your order! We are processing your order!");
					
					//var email = "Thank you for your order! We are processing your order!";
					orderDispatcher.send("ECOMMERCE_NEW_ORDER", userId, order);
					emailDispatcher.send("ECOMMERCE_SEND_EMAIL", userId, email);
				}
			}
		}

	}

}
