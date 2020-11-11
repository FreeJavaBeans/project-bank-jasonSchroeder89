package com.revature.user;

import java.sql.ResultSet;
import java.util.Scanner;

import com.revature.data.BankDAO;

public class Customer extends User {

	public Customer() {
		super();
	}

	@Override
	public boolean login(ResultSet results, Scanner input) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean prompt(BankDAO dao, Scanner input) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

}
