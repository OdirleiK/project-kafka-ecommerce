package br.com.kmpx.projectkafkaecommerce;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "new")
public class NewOrderController {

	@Autowired
	public KafkaDispatcher<Order> orderDispatcher;

	@Autowired
	public KafkaDispatcher<String> emailDispatcher;
	
	@Autowired
	public KafkaDispatcher<String> batchDispatcher;

	@PostMapping("/new-order")
	public ResponseEntity<String> newOrder(@RequestParam String emailParam, @RequestParam String amountValue) throws ServletException {
	    try (var orderDispatcherLocal = orderDispatcher; var emailDispatcherLocal = emailDispatcher) {
	    	//we are not caring about any security issues, we are only showing how to use as a starting point 
	        var orderId = UUID.randomUUID().toString();
	        var amount = new BigDecimal(amountValue);
	        var email = emailParam;

	        var order = new Order(orderId, amount, email);
	        var emailCode = "processing your order Thank you for your order! We are processing your order!";

	        orderDispatcherLocal.send("ECOMMERCE_NEW_ORDER", email, order);
	        emailDispatcherLocal.send("ECOMMERCE_SEND_EMAIL", email, emailCode);

	        System.out.println("New order sent successfully");

	        return ResponseEntity.status(HttpStatus.OK).body("New order sent successfully");
	    } catch (InterruptedException | ExecutionException  e) {
	        throw new ServletException(e);
	    }
	}
	
	@PostMapping("/generate-all-reports")
	public ResponseEntity<String> generateAllReports() throws ServletException {
	    try (var batchDispatcherLocal = batchDispatcher) {
	    	
	    	batchDispatcher.send("SEND_MESSAGE_TO_ALL_USERS", "USER_GENERATE_READING_REPORT","USER_GENERATE_READING_REPORT");
	    	
	    	
	    	System.out.println("Sent generate report to all users!");
	        return ResponseEntity.status(HttpStatus.OK).body("Report requests generated");
	    } catch (InterruptedException | ExecutionException  e) {
	        throw new ServletException(e);
	    }
	}
}
