package com.revature.bank;

import com.revature.banksystem.BankSystem;

public class Bank {

	public static void main(String[] args) {
		final BankSystem system = new BankSystem("Goliath National Bank");
		
		system.run();
	}

}