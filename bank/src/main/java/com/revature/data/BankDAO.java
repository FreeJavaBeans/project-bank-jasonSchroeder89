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
		try {
			conn = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/postgres", 
					System.getenv("DB_User"), 
					System.getenv("DB_Pass"));
			
			conn.setSchema("bank");
			
			PreparedStatement statement1 = conn.prepareStatement(
					"insert into \"User\" (\"UserName\", \"Password\", "
					+ "\"UserTypeID\") values (?, ?, 1)");
			
			statement1.setString(1, user.getUserName());
			
			statement1.setString(2, user.getPassword());
			
			statement1.execute();
			
			statement1.close();
			
			System.out.println("Statement 1 ran");
			
			PreparedStatement statement2 = conn.prepareStatement(
					"select \"UserID\" from \"User\" where \"UserName\" = ?");
			
			statement2.setString(1, user.getUserName());
			
			ResultSet results = statement2.executeQuery();
			
			statement2.close();
			
			System.out.println("Statement 2 ran");
			
			results.next();
			
			int userID = Integer.parseInt(results.getString(1));
			
			PreparedStatement statement3 = conn.prepareStatement(
					"insert into \"Customer\" (\"UserID\", \"FirstName\", "
					+ "\"LastName\") values (?, ?, ?)");
			
			statement3.setInt(1, userID);
			
			statement3.setString(2, user.getFirstName());
			
			statement3.setString(3, user.getLastName());
			
			statement3.execute();
			
			statement3.close();
			
			System.out.println("Statement 3 ran");
			
			conn.close();
		}
		
		catch (SQLException e) {
			e.printStackTrace();
			
			throw e;
		}
		
		return;
	}

	public ResultSet getAccounts(int customerID) throws SQLException {
		ResultSet results;
		
		try {
			conn = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/postgres", 
					System.getenv("DB_User"), 
					System.getenv("DB_Pass"));
			
			conn.setSchema("bank");
			
			PreparedStatement statement = conn.prepareStatement(
					"select \"AccountID\", \"Type\", \"Balance\" from "
					+ "\"Account\" a , \"AccountType\" at1 where a.\"TypeID\" "
					+ "= at1.\"TypeID\" and \"CustomerID\" = ?");
			
			statement.setInt(1, customerID);
			
			results = statement.executeQuery();
			
			statement.close();
			
			conn.close();
			
			return results;
		}
		
		catch (SQLException e) {
			results = null;
			
			throw e;
		}
	}

	public int getCustomerID(String userName) throws SQLException {
		ResultSet results;
		
		int customerID = 0;
		
		try {
			conn = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/postgres", 
					System.getenv("DB_User"), 
					System.getenv("DB_Pass"));
			
			conn.setSchema("bank");
			
			PreparedStatement statement = conn.prepareStatement(
					"select \"CustomerID\" from \"Customer\" where \"UserID\" "
					+ "in (select \"UserID\" from \"User\" where "
					+ "\"UserName\" = ?)");
			
			statement.setString(1, userName);
			
			results = statement.executeQuery();
			
			statement.close();
			
			conn.close();
			
			results.next();
			
			customerID = results.getInt(1);
		}
		
		catch (SQLException e) {
			results = null;
			
			throw e;
		}
		
		return customerID;
	}	
}