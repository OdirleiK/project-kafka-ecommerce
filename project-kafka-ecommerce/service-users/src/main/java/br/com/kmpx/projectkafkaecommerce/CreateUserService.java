package br.com.kmpx.projectkafkaecommerce;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import br.com.kmpx.projectkafkaecommerce.consumer.ConsumerService;
import br.com.kmpx.projectkafkaecommerce.consumer.ServiceRunner;
import br.com.kmpx.projectkafkaecommerce.database.LocalDataBase;

public class CreateUserService implements ConsumerService<Order>{
	
	private LocalDataBase dataBase;
	
	private CreateUserService() throws SQLException {
		this.dataBase = new LocalDataBase("users_database");
		this.dataBase.createIfNotExists("create table Users (uuid varchar(200) primary key, email varchar(200))");
	}
	
	public static void main(String[] args){
		new ServiceRunner<>(CreateUserService::new).start(1);
	}
	
	@Override
	public String getConsumerGroup() {
		return CreateUserService.class.getSimpleName().toString();
	}

	@Override
	public String getTopic() {
		return "ECOMMERCE_NEW_ORDER";
	}
	
	public void parse(ConsumerRecord<String, Message<Order>> record) throws SQLException  {
		System.out.println("=========================================");
		System.out.println("Processing new order, checking for new user");
		System.out.println(record.value());
		
		var order = record.value().getPayLoad();
		
		if(isNewUser(order.getEmail())) {
			insertNewUser(order.getEmail());
		}
	}

	private void insertNewUser(String email) throws SQLException {
		var uuid = UUID.randomUUID().toString();		
		dataBase.update("insert into Users (uuid, email) values (?, ?)", uuid, email);
		
		System.out.println("Usuário" + uuid + "e" + email + "adicionado");
	}

	private boolean isNewUser(String email) throws SQLException {
		var results = dataBase.query("select uuid from Users where email = ? limit 1");

		return !results.next();
	}


}
