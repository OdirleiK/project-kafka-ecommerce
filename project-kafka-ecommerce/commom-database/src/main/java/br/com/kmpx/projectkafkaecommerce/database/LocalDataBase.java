package br.com.kmpx.projectkafkaecommerce.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LocalDataBase {
	
private final Connection connection;
	
	public LocalDataBase(String name) throws SQLException {
		String url = "jdbc:sqlite:target/"+name+".db";
		connection = DriverManager.getConnection(url);
	}
	
	//yes, this is way too generic
	public void createIfNotExists(String sql) {
		try {
			connection.createStatement().execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean update(String statement, String ... params) throws SQLException {
		return prepare(statement, params).execute();
	}

	public ResultSet query(String query, String ... params) throws SQLException {
		var preparedStatement = prepare(query, params);
		return preparedStatement.executeQuery();
	}

	private PreparedStatement prepare(String query, String... params) throws SQLException {
		var preparedStatement = connection.prepareStatement(query);
		
		for(int i = 0; i < params.length; i++) {
			preparedStatement.setString(i+1, params[i]);
		}
		return preparedStatement;
	}

	public void close() throws SQLException {
		connection.close();
	}
}
