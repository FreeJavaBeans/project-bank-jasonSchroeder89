package com.revature.ui;

import java.sql.ResultSet;

public class RegistrationUI extends UI {
	
	private ResultSet data;
	
	public RegistrationUI(String bankName, ResultSet data) {
		super(bankName);
		
		this.data = data;
	}
}
