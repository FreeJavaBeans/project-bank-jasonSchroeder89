package com.revature.data;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.revature.user.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;


public class BankDAO {
	
	private Connection conn;
	
	public BankDAO() {
		
	}
	
	public ResultSet getUserNames() throws SQLException {
		ResultSet results;
		
		try {
			conn = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/postgres", 
					System.getenv("DB_User"), 
					System.getenv("DB_Pass"));
			
			conn.setSchema("bank");
			
			PreparedStatement statement = conn.prepareStatement(
					"select \"UserName\" from \"User\"");
			
			results = statement.executeQuery();
			
			statement.close();
			
			conn.close();
		}
		
		catch (SQLException e) {
			results = null;
			
			throw e;
		}
				
		return results;
	}
	
	public String getPassword(String userName) throws SQLException {
		String password = null;
		
		try {
			conn = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/postgres", 
					System.getenv("DB_User"), 
					System.getenv("DB_Pass"));
			
			conn.setSchema("bank");
			
			PreparedStatement statement1 = conn.prepareStatement(
					"select \"Password\" from \"User\" where \"UserName\" = ?"
					);
			
			statement1.setString(1, userName);
			
			ResultSet result = statement1.executeQuery();
			
			statement1.close();
			
			conn.close();
			
			result.next();
			
			password = result.getString(1);
		}
		
		catch (SQLException e) {
			throw e;
		}
		
		return password;
	}
	
	public void createNewCustomer(User user) throws SQLException {
		int newUserID = genNewUserID(user.getUserName());
		
		try {
			conn = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/postgres", 
					System.getenv("DB_User"), 
					System.getenv("DB_Pass"));
			
			conn.setSchema("bank");
			
			PreparedStatement statement1 = conn.prepareStatement(
					"insert into \"User\" (\"UserID\", \"UserName\", "
					+ "\"Password\", \"UserTypeID\") values (?, ?, ?, 1)");
			
			statement1.setInt(1, newUserID);
			
			statement1.setString(2, user.getUserName());
			
			statement1.setString(3, user.getPassword());
			
			statement1.execute();
			
			statement1.close();
			
			PreparedStatement statement2 = conn.prepareStatement(
					"insert into \"Customer\" (\"CustomerID\", \"UserID\", "
					+ "\"FirstName\", \"LastName\") values (?, ?, ?, ?)");			
			
			statement2.setInt(1, newUserID + user.hashCode());
			
			statement2.setInt(2, newUserID);
			
			statement2.setString(3, user.getFirstName());
			
			statement2.setString(4, user.getLastName());
			
			statement2.execute();
			
			statement2.close();			
			
			conn.close();
		}
		
		catch (SQLException e) {
			throw e;
		}
		
		return;
	}
	
	private int genNewUserID(String userName) {
		int newUserID = userName.hashCode() + userName.length();
		
		return newUserID;
	}
}
