package com.revature.ui;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.revature.data.BankDAO;
import com.revature.user.Customer;
import com.revature.user.Employee;
import com.revature.user.NewUser;
import com.revature.user.User;
import com.revature.util.Util;

public class SystemUI {
	
	private final Scanner input;
	private final String headerWelcome;
	private final String header;
	private final String ribbon;
	private final String menuPrompt = 
			"Please select a choice from the menu below:\n\n";
	private final String promptChoice = 
			"Please enter your choice (1-4) then press 'Enter': ";
	
	public SystemUI(String bankName, Scanner scanner) {
		this.headerWelcome = "Welcome to " + bankName + "!";
		this.header = "Welcome to " + bankName;
		this.ribbon = "\n\nDigital Banking System\n----------------------\n\n";
		this.input = scanner;
	}	
	
	public User welcomePrompt() {
		int choice = 0;
		
		do {
			System.out.print(headerWelcome + ribbon + menuPrompt
					+ "1) Register for a customer account\n"
					+ "2) Customer Login\n"
					+ "3) Employee Login\n" + "4) Exit Program\n\n"
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
				
				Util.clearScreen(true);
				
				continue;
			}
		}
		while (choice == 0);
		
		switch(choice) {
			case 1:
				return new NewUser();
				
			case 2:
				return new Customer();
				
			case 3:
				return new Employee();
				
			case 4:
				return null;
				
			default:
				return null;	
		}
	}
	
	public boolean regPrompt(ResultSet results, User user) {
		HashSet<String> userNames = Util.getUserNameSet(results);
		
		System.out.print(header + ribbon 
				+ "Please enter the requested information"
				+ " or type 'exit' in any field to cancel\n\n");
		
		boolean isValidUserName = false;
		boolean isValidPassword = false;
		
		do {
			System.out.print("Enter desired username (20 character max): ");
		
			String userName = input.next();
			
			if (userName.toLowerCase().equals("exit")) {
				return false;
			}
			
			if (userName.length() > 20) {
				System.out.println("Error: username is too long. "
						+ "Please enter a username no longer than 20 "
						+ "characters.\n\n");
				continue;
			}
			
			if (userNames.contains(userName)) {
				System.out.println("\n\nError: username already in use. "
						+ "Please enter a different username.\n\n");
				
				continue;
			}
			
			else {
				user.setUserName(userName);
				isValidUserName = true;
			}
		}
		while(!isValidUserName);
		
		do {
			System.out.print("Enter desired password (20 character max): ");
			
			String password = input.next();
			
			if (password.toLowerCase().equals("exit")) {
				return false;
			}
			
			if (password.length() > 20) {
				System.out.println("Error: password is too long. "
						+ "Please enter a username no longer than 20 "
						+ "characters.\n\n");
				continue;
			}
			
			else {
				user.setPassword(password);
				isValidPassword = true;
			}
		}
		while(!isValidPassword);
		
		System.out.print("Enter your first name (20 character max): ");
		
		String firstName = input.next();
		
		if (firstName.toLowerCase().equals("exit")) {
			return false;
		}
		
		user.setFirstName(firstName);
		
		System.out.print("Enter your last name (20 character max): ");
		
		String lastName = input.next();
		
		if (lastName.toLowerCase().equals("exit")) {
			return false;
		}
		
		user.setLastName(lastName);
		
		return true;		
	}
	
	public boolean login(BankDAO dao, Scanner input) {
		boolean validLogin = false;
		
		try {
			HashSet<String> userNames = Util.getUserNameSet(
					dao.getUserNames());
			
			final int maxAttempts = 5;
			int currentAttempt = 0;
			
			while (currentAttempt < maxAttempts) {
				System.out.print(header + ribbon + "Remaining attempts:");
				
				for (int i = 0; i < maxAttempts - currentAttempt; i++) {
					System.out.print('*');
				}
				
				System.out.println("\n Please enter your credentials, or type"
						+ " 'exit' in any field to go back.");
				
				System.out.print("Please enter your username: ");
				
				String userName = input.next();
				
				if (userNames.contains(userName)) {
					System.out.print("\n\nPlease enter your password: ");
					
					String password = input.next();
					
					if (password.equals(dao.getPassword(userName))) {
						validLogin = true;
						
						break;
					}
					
					else {
						System.out.println("Error: incorrect password."
								+ "Please enter your proper credentials");
						
						currentAttempt++;
					}
				}
				
				else {
					System.out.println("\n\nError: username not found."
							+ "Please check your spelling and try again.");
					
					currentAttempt++;
					
					Util.clearScreen(true);
				}
			}
			
			if (validLogin) {
				return validLogin;
			}
			
			else {
				return validLogin;
			}
		} 
		
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return validLogin;
	}
	
	public void regSuccessMessage() {
		System.out.print("\n\nRegistration successful!\n\n"
				+ "Please login to create an account.\n\n");
	}
	
	public void exceedLoginAttemptsMessage() {
		System.out.println("You have exceeded the maximum number of allowed"
				+ " login attempts.\n\nPlease restart the program and try"
				+ " again\n\n");
	}
	
	public void exitingMessage() {
		System.out.println("Exiting program, Goodbye!\n\n");
	}	
}