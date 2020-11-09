package com.revature.banksystem;

import java.io.IOException;
import java.util.Scanner;

import com.revature.data.DataRequest;
import com.revature.enums.State;
import com.revature.ui.Display;
import com.revature.ui.WelcomeUI;
import com.revature.user.Customer;
import com.revature.user.Employee;
import com.revature.user.User;

public class BankSystem {
	
	private final String bankName;
	private Display ui;
	private State state;
	private User user;
	private DataRequest request;
	
	public BankSystem(String bankName) {
		this.bankName = bankName;
		this.setSystemState(State.WELCOME);
	}
	
	public void run() {
		do {
			setSystemState(ui.prompt());
			
			switch(this.state) {
				case END_PROGRAM:
					System.out.println("\nGoodbye!");
					return;
					
				case REGISTER_USER:
					
			}
			
			try {
				Runtime.getRuntime().exec("clear");
			}
			catch (IOException e) {
				System.out.println("It's not executing");
			}			
		}
		while(true);	
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
}