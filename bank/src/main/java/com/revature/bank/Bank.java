package com.revature.bank;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.banksystem.BankSystem;

public class Bank {

	public static void main(String[] args) {
		Logger logger = LogManager.getLogger("com.revature.bank");
		
		final BankSystem system = 
				new BankSystem("Goliath National Bank", logger);
		
		system.run();
	}

}