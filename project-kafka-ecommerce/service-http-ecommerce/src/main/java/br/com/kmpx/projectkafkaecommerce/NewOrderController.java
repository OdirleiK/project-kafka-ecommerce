package br.com.kmpx.projectkafkaecommerce;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.kmpx.projectkafkaecommerce.dispatcher.KafkaDispatcher;

@RestController
@RequestMapping(value = "new")
public class NewOrderController {
	
	@Autowired
	public KafkaDispatcher<Order> orderDispatcher;

	@PostMapping("/new-order")
	public ResponseEntity<String> newOrder(@RequestParam String emailParam, @RequestParam String amountValue, @RequestParam String uuid) throws ServletException, IOException {
	    try (var orderDispatcherLocal = orderDispatcher) {
	    	//we are not caring about any security issues, we are only showing how to use as a starting point 
	        var orderId = uuid;
	        var amount = new BigDecimal(amountValue);
	        var email = emailParam;
	        var order = new Order(orderId, amount, email);	      
	        var id = new CorrelationId(NewOrderController.class.getSimpleName());

	       
	        try (var database = new OrdersDatabase()) {
	        	if(database.saveNew(order)) {
			        orderDispatcherLocal.send("ECOMMERCE_NEW_ORDER", email, id, order);
			        System.out.println("New order sent successfully");
			        return ResponseEntity.status(HttpStatus.OK).body("New order sent successfully");
		        } else {
			        System.out.println("Old order received");
			        return ResponseEntity.status(HttpStatus.OK).body("Old order received");
		        }
	        } 
	        
	        
	        

	    } catch (InterruptedException | ExecutionException | SQLException e) {
	        throw new ServletException(e);
	    }
	}
	
	
}
