package br.com.kmpx.projectkafkaecommerce;

import java.io.Closeable;
import java.io.IOException;
import java.sql.SQLException;

import br.com.kmpx.projectkafkaecommerce.database.LocalDataBase;


public class OrdersDatabase implements Closeable{

	private LocalDataBase database;
	
	public OrdersDatabase() throws SQLException {
		this.database = new LocalDataBase("orders_database");
		// you might want to sava all data
		this.database.createIfNotExists("create table Orders (uuid varchar(200) primary key)");
	}

	public boolean saveNew(Order order) throws SQLException {
		if(wasProcessed(order)) {
			return false;
		}
		database.update("insert into Orders (uuid) values (?)", order.getOrderId());			
		return true;
	}
	
	private boolean wasProcessed(Order order) throws SQLException {
		var results = database.query("select uuid from Orders where uuid = ? limit 1", order.getOrderId());
		return results.next();
	}

	@Override
	public void close() throws IOException {
		try {
			database.close();
		} catch (SQLException e) {
			throw new IOException(e);
		}
	}
}
