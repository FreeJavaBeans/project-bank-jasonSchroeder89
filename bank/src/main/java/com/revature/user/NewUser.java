package com.revature.user;

import java.util.Scanner;

import com.revature.data.BankDAO;

public class NewUser extends User{
	
	public NewUser() {
		super();
	}
	
	@Override
	public void prompt(String bankName, BankDAO dao, Scanner input) {
		return;
	}

	@Override
	public String getType() {
		return "New";
	}
}