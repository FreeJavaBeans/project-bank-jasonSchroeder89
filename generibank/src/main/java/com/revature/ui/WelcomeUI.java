package com.revature.ui;

public class WelcomeUI extends UI implements Display{
	
	public WelcomeUI(String bankName) {
		super(bankName);
	}
	
	public void displayUI() {
		System.out.println("Welcome to GeneriBank!\n\nDigital Banking System\n"
			+ "----------------------\n");
	}
}
