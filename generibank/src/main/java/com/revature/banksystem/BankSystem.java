package com.revature.banksystem;

public class BankSystem {
	private final String bankName;
	private boolean validLogin;
	
	public BankSystem(String bankName) {
		this.bankName = bankName;
		this.validLogin = false;
	}
}
