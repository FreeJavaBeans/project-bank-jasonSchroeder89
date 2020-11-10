package com.revature.ui;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.revature.data.BankDAO;
import com.revature.user.Customer;
import com.revature.user.Employee;
import com.revature.user.NewUser;
import com.revature.user.User;

public class SystemUI {
	
	private final Scanner input;
	private final static String clearScreenPrompt = 
			"\n\nPress 'Enter' to continue.... ";
	private final String header;
	private final String menuPrompt = 
			"Please select a choice from the menu below:\n\n";
	private final String promptChoice = 
			"Please enter your choice (1-4) then press 'Enter': ";
	
	public SystemUI(String bankName, Scanner scanner) {
		this.header = "Welcome to " + bankName + 
				"!\n\nDigital Banking System\n----------------------\n\n";
		this.input = scanner;
	}
	
	public void welcomePrompt(User user) {
		int choice = 0;
		
		do {
			System.out.print(header	+ menuPrompt
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
		
		switch(choice) {
			case 1:
				user = new NewUser();
				
			case 2:
				user = new Customer();
				
			case 3:
				user = new Employee();
				
			case 4:
				user = null;
				
			default:
				break;	
		}
		
		return;
	}

	public boolean regPrompt(ResultSet results, User user) {
		return false;
		
	}
	
	public void regSuccessMessage() {
		System.out.print("Registration successful!\nPlease login to create an"
				+ "account.\n");
	}
	
	public static void clearScreen() {
		System.out.println(clearScreenPrompt);
		
		try {
		      final String os = System.getProperty("os.name");
		      if (os.contains("Windows")) {
		          Runtime.getRuntime().exec("cls");
		      }
		      
		      else {
		          Runtime.getRuntime().exec("clear");
		      }
		} 
		
		catch (final IOException e) {
			System.out.println("Error clearing conole");
		}
	}
}