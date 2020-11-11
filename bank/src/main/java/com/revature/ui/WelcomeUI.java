package com.revature.ui;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.revature.enums.State;

public class WelcomeUI extends UI implements Display{
	
	private Scanner input = new Scanner(System.in);
	
	public WelcomeUI(String bankName) {
		super(bankName);
		this.bankName = bankName;
	}
	
	public State prompt() {
		int choice = 0;
		
		do {
			System.out.print(header + menuPrompt					
					+ "\t1) Register for an account\n" + "\t2) Customer Login\n"
					+ "\t3) Employee Login\n" + "\t4) Exit Program\n\n"
					+ promptChoice
					);
			try {
				choice = input.nextInt();
				
				if (choice < 1 || choice > 4) {
					throw new InputMismatchException();
				}
			}
			catch (InputMismatchException e){
				input.nextLine();
				
				System.out.println(
						"\nError: please enter a valid integer choice (1-4)"
						+ clearScreenPrompt);
				try {
					System.in.read();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				continue;
			}
		}
		while (choice == 0);
		
		switch (choice) {
			case 1:
				return State.REGISTER_USER;
				
			case 2:
				return State.LOGIN_CUSTOMER;
				
			case 3:
				return State.LOGIN_EMPLOYEE;
				
			case 4:
				return State.END_PROGRAM;
				
			default:
				return State.WELCOME;			
		}
	}
	
	public PreparedStatement requestData() {
		return null;
	}
}