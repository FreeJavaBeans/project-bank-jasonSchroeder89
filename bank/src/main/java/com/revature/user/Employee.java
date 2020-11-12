package com.revature.user;

import java.sql.ResultSet;
import java.util.Scanner;

import com.revature.data.BankDAO;

public class Employee extends User{
	
	public Employee() {
		super();
	}

	@Override
	public void prompt(String bankName, BankDAO dao, Scanner input) {
		// TODO Auto-generated method stub
		return;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return "Employee";
	}
}