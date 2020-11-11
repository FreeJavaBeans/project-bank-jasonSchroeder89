package com.revature.user;

import java.sql.ResultSet;
import java.util.Scanner;

import com.revature.data.BankDAO;

public class NewUser extends User{
	
	public NewUser() {
		super();
	}
	
	@Override
	public boolean login(ResultSet results, Scanner input) {
		return false;
	}

	@Override
	public boolean prompt(BankDAO dao, Scanner input) {
		return false;
	}

	@Override
	public String getType() {
		return "New";
	}
}