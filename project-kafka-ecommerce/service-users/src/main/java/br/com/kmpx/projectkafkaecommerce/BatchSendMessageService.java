package br.com.kmpx.projectkafkaecommerce;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import br.com.kmpx.projectkafkaecommerce.consumer.KafkaService;
import br.com.kmpx.projectkafkaecommerce.dispatcher.KafkaDispatcher;

public class BatchSendMessageService {

private final Connection connection;
	
	BatchSendMessageService() throws SQLException {
		String url = "jdbc:sqlite:target/users_database.db";
		connection = DriverManager.getConnection(url);
		
		try {
			connection.createStatement().execute("create table Users (" +
											     "uuid varchar(200) primary key," +
												 "email varchar(200))");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws SQLException, ExecutionException, InterruptedException {
		var batchService = new BatchSendMessageService();
		try(var service = new KafkaService<>(BatchSendMessageService.class.getSimpleName(), 
									  	   "ECOMMERCE_SEND_MESSAGE_TO_ALL_USERS", 
									  	    batchService::parse,
									        new HashMap<>())) {
			service.run();
		}
	}
	
	private final KafkaDispatcher<User> userDispatcher = new KafkaDispatcher<>();
	
	private void parse(ConsumerRecord<String, Message<String>> record) throws SQLException, InterruptedException, ExecutionException {
		System.out.println("=========================================");
		System.out.println("Processing new batch");
		
		var message = record.value();
		System.out.println("Topic:" + message.getPayLoad());
		
		
		for(User user: getAllUsers()) {
    		userDispatcher.sendAsync(message.getPayLoad(), 
    							user.getUuid(), 
    							message.getId().continueWith((BatchSendMessageService.class.getSimpleName())), 
    							user);
    	}

	}
	
	private List<User> getAllUsers() throws SQLException {
		var results = connection.prepareStatement("select uuid from Users").executeQuery();
		List<User> users = new ArrayList<>();
		while(results.next()) {
			users.add(new User(results.getString(1)));
		}
		return users;
	}
}
