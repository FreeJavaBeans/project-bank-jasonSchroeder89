package com.revature.user;

import java.util.Scanner;

import com.revature.data.BankDAO;

public abstract class User {
	
	private String firstName;
	private String lastName;
	//private String address;
	private String userName;
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

	/*public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}*/

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public abstract void prompt(String bankName, BankDAO dao, Scanner input);
	
	public abstract String getType();
}