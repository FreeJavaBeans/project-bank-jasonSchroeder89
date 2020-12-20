package com.revature.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.logging.log4j.Logger;

import com.revature.user.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;


public class BankDAO {
	
	private Connection conn;
	private final Logger logger;
	private ArrayList<String> logs = new ArrayList<String>();
	
	public BankDAO(Logger logger) {
		this.logger = logger;
	}
	
	public ArrayList<String> getLogs() {
		return logs;
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
		
		logger.info("Usernames queried");
				
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
		
		logger.info("Password for user: " + userName + " queried");
		
		return password;
	}
	
	public void createNewCustomer(User user) throws SQLException {
		try {
			conn = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/postgres", 
					System.getenv("DB_User"), 
					System.getenv("DB_Pass"));
			
			conn.setSchema("bank");
			
			conn.setAutoCommit(false);
			
			PreparedStatement statement1 = conn.prepareStatement(
					"insert into \"User\" (\"UserName\", \"Password\", "
					+ "\"UserTypeID\") values (?, ?, 1)");
			
			statement1.setString(1, user.getUserName());
			
			statement1.setString(2, user.getPassword());
			
			statement1.execute();
			
			statement1.close();
			
			PreparedStatement statement2 = conn.prepareStatement(
					"select \"UserID\" from \"User\" where \"UserName\" = ?");
			
			statement2.setString(1, user.getUserName());
			
			ResultSet results = statement2.executeQuery();
			
			statement2.close();
			
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
			
			conn.commit();
			
			conn.setAutoCommit(true);
			
			conn.close();
		}
		
		catch (SQLException e) {
			e.printStackTrace();
			
			throw e;
		}
		
		logger.info("New Customer: " + user.getUserName() + " created");
		
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
			
			logger.info("Accounts for Customer with id: " + customerID 
					+ " were queried");
			
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
		
		logger.info("Customer ID for user: " + userName + " was queried");
		
		return customerID;
	}

	public void requestAccount(int customerID, int accountType, 
			double newBalance) throws SQLException {
		
		try {
			conn = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/postgres", 
					System.getenv("DB_User"), 
					System.getenv("DB_Pass"));
			
			conn.setSchema("bank");
			
			PreparedStatement statement = conn.prepareStatement(
					"insert into \"PendingAccount\" (\"CustomerID\", "
					+ "\"TypeID\", \"Balance\") values (?, ?, ?)");
			
			statement.setInt(1, customerID);
			statement.setInt(2, accountType);
			statement.setDouble(3, newBalance);
			
			statement.executeUpdate();
			
			statement.close();
			
			conn.close();
			
			logger.info("New Account request added");
		}
		
		catch (SQLException e) {
			throw e;
		}
	}

	public void setNewAccountBalance(int accountNum, double newBalance) 
			throws SQLException {
		
		try {
			conn = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/postgres", 
					System.getenv("DB_User"), 
					System.getenv("DB_Pass"));
			
			conn.setSchema("bank");
			
			PreparedStatement statement = conn.prepareStatement(
					"update \"Account\" set \"Balance\" = ? where "
					+ "\"AccountID\" = ?");
			
			statement.setDouble(1, newBalance);
			
			statement.setInt(2, accountNum);
			
			statement.executeUpdate();
			
			statement.close();
			
			conn.close();
			
			logger.info("Balance for Account: " + accountNum + " changed to: "
					+ newBalance);
		}
		
		catch (SQLException e) {
			throw e;
		}
		
	}

	public ResultSet getAccountBalances() throws SQLException {
		ResultSet results;
		
		try {
			conn = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/postgres", 
					System.getenv("DB_User"), 
					System.getenv("DB_Pass"));
			
			conn.setSchema("bank");
			
			PreparedStatement statement = conn.prepareStatement(
					"select \"AccountID\", \"Balance\", \"CustomerID\" "
					+ "from \"Account\"");
			
			results = statement.executeQuery();
			
			statement.close();
			
			conn.close();
			
			logger.info("Customer account balances queried");
		}
		
		catch (SQLException e) {
			throw e;
		}		
		
		return results;
	}

	public void transferFunds(int sourceAccountNum, double newSourceBalance, 
			int targetAccountNum, double newTargetBalance) throws SQLException {
		
		try {
			conn = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/postgres", 
					System.getenv("DB_User"), 
					System.getenv("DB_Pass"));
			
			conn.setSchema("bank");
			
			conn.setAutoCommit(false);
			
			PreparedStatement statement1 = conn.prepareStatement(
					"update \"Account\" set \"Balance\" = ? where "
					+ "\"AccountID\" = ?");
			
			statement1.setDouble(1, newSourceBalance);
			statement1.setInt(2, sourceAccountNum);			
			statement1.executeUpdate();			
			statement1.close();
			
			PreparedStatement statement2 = conn.prepareStatement(
					"update \"Account\" set \"Balance\" = ? where "
					+ "\"AccountID\" = ?");
			
			statement2.setDouble(1, newTargetBalance);
			statement2.setInt(2, targetAccountNum);			
			statement2.executeUpdate();			
			statement2.close();
			
			conn.commit();
			
			conn.setAutoCommit(true);
			
			conn.close();
			
			logger.info("Successful transfer from Account " + sourceAccountNum
					+ " to " + targetAccountNum);
		}
		
		catch (SQLException e) {
			throw e;
		}
	}

	public int getEmployeeID(String userName) throws SQLException {
		ResultSet results;
		
		int employeeID = 0;
		
		try {
			conn = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/postgres", 
					System.getenv("DB_User"), 
					System.getenv("DB_Pass"));
			
			conn.setSchema("bank");
			
			PreparedStatement statement = conn.prepareStatement(
					"select \"EmployeeID\" from \"Employee\" where \"UserID\" "
					+ "in (select \"UserID\" from \"User\" where "
					+ "\"UserName\" = ?)");
			
			statement.setString(1, userName);
			
			results = statement.executeQuery();
			
			statement.close();
			
			conn.close();
			
			logger.info("EmployeeID for user: " + userName + " was queried");
			
			results.next();
			
			employeeID = results.getInt(1);
		}
		
		catch (SQLException e) {
			results = null;
			
			throw e;
		}
		
		return employeeID;
	}

	public ResultSet getRequestedAccounts() throws SQLException {
		ResultSet results;
		
		try {
			conn = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/postgres", 
					System.getenv("DB_User"), 
					System.getenv("DB_Pass"));
			
			conn.setSchema("bank");
			
			PreparedStatement statement = conn.prepareStatement(
					"select * from \"PendingAccount\"");
			
			results = statement.executeQuery();
			
			statement.close();
			
			conn.close();
			
			logger.info("Pending accounts queried");
		}
		
		catch (SQLException e) {
			results = null;
			
			throw e;
		}
		
		return results;
	}

	public void createAccount(int employeeID, int requestID) throws SQLException {
		try {
			conn = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/postgres", 
					System.getenv("DB_User"), 
					System.getenv("DB_Pass"));
			
			conn.setSchema("bank");
			
			conn.setAutoCommit(false);
			
			PreparedStatement statement1 = conn.prepareStatement(
					"select \"CustomerID\", \"TypeID\", \"Balance\" from "
					+ "\"PendingAccount\" where \"PendingID\" = ?");
			
			statement1.setInt(1, requestID);
			
			ResultSet result = statement1.executeQuery();
			
			statement1.close();
			
			PreparedStatement statement2 = conn.prepareStatement(
					"delete from \"PendingAccount\" where \"PendingID\" = ?");
			
			statement2.setInt(1, requestID);
			
			statement2.executeUpdate();
			
			statement2.close();
			
			PreparedStatement statement3 = conn.prepareStatement(
					"insert into \"Account\" (\"CustomerID\", \"TypeID\", "
					+ "\"Balance\") values (?, ?, ?)");
			
			result.next();
			
			statement3.setInt(1, result.getInt(1));
			
			statement3.setInt(2, result.getInt(2));
			
			statement3.setDouble(3, result.getDouble(3));
			
			statement3.executeUpdate();
			
			statement3.close();
			
			conn.commit();
			
			conn.setAutoCommit(true);
			
			conn.close();
			
			logger.info("Pending Account " + requestID + " was approved by "
					+ "Employee " + employeeID + " and created");
		}
		
		catch (SQLException e) {
			throw e;
		}		
	}

	public ResultSet getCustomerAccounts() throws SQLException {
		ResultSet results;
		
		try {
			conn = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/postgres", 
					System.getenv("DB_User"), 
					System.getenv("DB_Pass"));
			
			conn.setSchema("bank");
			
			PreparedStatement statement = conn.prepareStatement(
					"select * from \"Account\"");
			
			results = statement.executeQuery();
			
			statement.close();
			
			conn.close();
			
			logger.info("Customer accounts were queried");
		}
		
		catch (SQLException e) {
			results = null;
			
			throw e;
		}
		
		return results;
	}
}