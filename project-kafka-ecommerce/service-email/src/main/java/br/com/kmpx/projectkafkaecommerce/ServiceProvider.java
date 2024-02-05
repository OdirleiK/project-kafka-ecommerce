package br.com.kmpx.projectkafkaecommerce;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import br.com.kmpx.projectkafkaecommerce.consumer.KafkaService;

public class ServiceProvider {

	public <T> void run(ServiceFactory<T> factory) throws ExecutionException, InterruptedException {
		var emailService = factory.create();
		
		try (var service = new KafkaService<>(emailService.getConsumerGroup(), 
											  emailService.getTopic(), 
											  emailService::parse,
									   new HashMap<>())) {
			service.run();
		}
	}


}
