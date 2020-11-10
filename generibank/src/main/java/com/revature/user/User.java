package com.revature.user;

import java.sql.ResultSet;
import java.util.Scanner;

import com.revature.data.BankDAO;

public abstract class User {
	private String firstName;
	private String lastName;
	private String address;
	private String userID;
	private String password;
	
	public User() {
		
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public abstract boolean login(ResultSet results, Scanner input);
	
	public abstract boolean prompt(BankDAO dao, Scanner input);
}
