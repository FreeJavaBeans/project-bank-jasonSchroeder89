package com.revature.user;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.revature.data.BankDAO;
import com.revature.util.Util;

public class Customer extends User {

	private int customerID;
	
	private final String menuPrompt = 
			"Please select a choice from the menu below:\n";
	
	public Customer() {
		super();
	}

	public int getCustomerID() {
		return customerID;
	}

	public void setCustomerID(BankDAO dao) {
		try {
			this.customerID = dao.getCustomerID(this.getUserName());
		} 
		
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void prompt(String bankName, BankDAO dao, Scanner input) {
		setCustomerID(dao);
		
		final String header = bankName + "\n\nCustomer Portal" 
				+ "\n---------------\n\n";
		
		int choice = 0;
		
		do {
			System.out.println(header + menuPrompt);			
			
			System.out.println("1) View accounts");
			System.out.println("2) Apply for a new account");
			System.out.println("3) Withdraw funds");
			System.out.println("4) Deposit funds");
			System.out.println("5) Transfer funds");
			System.out.println("6) Accept fund transfer");
			System.out.println("7) Logout");
			
			System.out.print("\nPlease enter your choice (1-7) "
					+ "then press 'Enter': ");
			
			try {
				choice = input.nextInt();
				
				if (choice < 1 || choice > 7) {
					throw new InputMismatchException();
				}
				
				switch (choice) {
					case 1:
						showAccounts(dao);
						choice = 0;
						break;
						
					case 2:
						applyForAccount(dao, input);
						choice = 0;
						break;
						
					case 3:
						withdrawFunds(dao, input);
						choice = 0;
						break;
						
					case 4:
						depositFunds(dao, input);
						choice = 0;
						break;
						
					case 5:
						transferFunds(dao, input);
						choice = 0;
						break;
						
					case 6:
						acceptFundTransfer(dao, input);
						choice = 0;
						break;
						
					case 7:
						break;
						
					default:
						break;
				}
			}
			
			catch (InputMismatchException e) {
				System.out.println("\nError: Please enter a valid "
						+ "integer choice (1-7)");
				
				input.nextLine();
				
				choice = 0;
				
				Util.clearScreen(true);
			}
		}
		while (choice != 7);
		
		return;
	}
	
	private void acceptFundTransfer(BankDAO dao, Scanner input) {
		
		
	}

	private void transferFunds(BankDAO dao, Scanner input) {
		
		
	}

	private void depositFunds(BankDAO dao, Scanner input) {
		
		
	}

	private void withdrawFunds(BankDAO dao, Scanner input) {
		
		
	}

	private void applyForAccount(BankDAO dao, Scanner input) {
		
		
	}

	private void showAccounts(BankDAO dao) {
		Util.clearScreen(false);
		
		ResultSet results;
		
		try {
			results = dao.getAccounts(this.getCustomerID());
			
			System.out.println("Account ID:  Account Type:  Balance:");
			
			while(results.next()) {
				
				int accountID = results.getInt(1);
				String type = results.getString(2);
				double balance = results.getDouble(3);
				
				System.out.println("     " + accountID + "       " 
						+ type + "      " + balance);
			}
			
			Util.clearScreen(true);
		} 
		
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	@Override
	public String getType() {
		return "Customer";
	}
}