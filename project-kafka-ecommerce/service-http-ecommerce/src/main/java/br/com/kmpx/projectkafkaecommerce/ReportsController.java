package br.com.kmpx.projectkafkaecommerce;

import java.util.concurrent.ExecutionException;

import javax.servlet.ServletException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "order")
public class ReportsController {

	private final KafkaDispatcher<String> batchDispatcher = new KafkaDispatcher<>();
	
	@PostMapping("/generate-all-reports")
	public ResponseEntity<String> generateAllReports() throws ServletException {
		
	    try (var batchDispatcherLocal = batchDispatcher) {
	    	
	    	batchDispatcher.send("ECOMMERCE_SEND_MESSAGE_TO_ALL_USERS",
	    						"ECOMMERCE_USER_GENERATE_READING_REPORT",
	    						new CorrelationId(ReportsController.class.getSimpleName()),
	    						"ECOMMERCE_USER_GENERATE_READING_REPORT");
	    	
	    	
	    	System.out.println("Sent generate report to all users!");
	        return ResponseEntity.status(HttpStatus.OK).body("Report requests generated");
	    } catch (InterruptedException | ExecutionException  e) {
	        throw new ServletException(e);
	    }
	}

}
