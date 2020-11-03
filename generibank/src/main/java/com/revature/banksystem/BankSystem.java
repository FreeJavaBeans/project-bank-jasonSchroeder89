package com.revature.banksystem;

import com.revature.enums.State;
import com.revature.ui.Display;
import com.revature.ui.WelcomeUI;
import com.revature.user.Customer;
import com.revature.user.Employee;
import com.revature.user.User;

public class BankSystem {
	
	private final String bankName;
	private boolean validLogin;
	private Display ui;
	private State state;
	private User u;
	
	public BankSystem(String bankName) {
		this.bankName = bankName;
		this.validLogin = false;
		this.setSystemState(State.WELCOME);
	}
	
	public void run() {
		while(this.state != State.END_PROGRAM) {
			ui.displayUI();
			//int choice = prompt();
			
			System.out.println("Goodbye!");
			
			setSystemState(State.END_PROGRAM);
				
			return;
		}	
	}
	
	private void setSystemState(State state) {
		this.state = state;
		
		switch (state) {
			case WELCOME:
				ui = new WelcomeUI(bankName);
				
			case LOGIN_CUSTOMER:
				
			default:
				break;
				
			
		}
	}
	
	private boolean logIn() {
		
		
		return true;
	}
}
