package br.com.kmpx.projectkafkaecommerce;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import br.com.kmpx.projectkafkaecommerce.consumer.ConsumerService;
import br.com.kmpx.projectkafkaecommerce.consumer.ServiceRunner;
import br.com.kmpx.projectkafkaecommerce.database.LocalDataBase;
import br.com.kmpx.projectkafkaecommerce.dispatcher.KafkaDispatcher;

public class FraudDectectorService implements ConsumerService<Order>{
	
	private static final String NAME_CLASS = FraudDectectorService.class.getSimpleName();

	
	public static void main(String[] args) {
		new ServiceRunner<>(FraudDectectorService::new).start(1);
	}
	
	private final KafkaDispatcher<Order> orderDispathcer = new KafkaDispatcher<>();
	
	private LocalDataBase dataBase;
	
	private FraudDectectorService() throws SQLException {
		this.dataBase = new LocalDataBase("frauds_database");
		this.dataBase.createIfNotExists("create table Orders (uuid varchar(200) primary key, is_fraud boolean)");
	}
	
	@Override
	public String getConsumerGroup() {
		return FraudDectectorService.class.getSimpleName();
	}

	@Override
	public String getTopic() {
		return "ECOMMERCE_NEW_ORDER";
	}
	
	public void parse(ConsumerRecord<String, Message<Order>> record) throws InterruptedException, ExecutionException, SQLException {
		System.out.println("=========================================");
		System.out.println("Processing new order, checking for fraud");
		System.out.println(record.key());
		System.out.println(record.value());
		System.out.println(record.partition());
		System.out.println(record.offset());
		
		var message = record.value();
		var order = message.getPayLoad();

		if(wasProcessed(order)) {
			System.out.println("Order was already processed");
			return;
		}	
		
		try {
			Thread.sleep(3000);
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		
		if(isFraud(order)) {
			dataBase.update("insert into Orders (uuid, is_fraud) values (?,true)", order.getOrderId());
			
			//pretending that the fraud happens when the amount is >=4500
			System.out.println("Order is a fraud!");	
			orderDispathcer.send("ECOMMERCE_ORDER_REJECTED", 
								order.getEmail(), 
								message.getId().continueWith(NAME_CLASS), 
								order);
		}else {
			dataBase.update("insert into Orders (uuid, is_fraud) values (?,false)", order.getOrderId());
			System.out.println("Approved: " + order);
			orderDispathcer.send("ECOMMERCE_ORDER_APPROVED", 
								order.getEmail(), 
								message.getId().continueWith(NAME_CLASS),
								order);
		}
	}
	
	private boolean isFraud(Order order) {
		return order.getAmount().compareTo(new BigDecimal("200")) >= 0;
	}

	private boolean wasProcessed(Order order) throws SQLException {
		var results = dataBase.query("select uuid from Orders where uuid = ? limit 1", order.getOrderId());
		return results.next();
	}

}
