package com.revature.user;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.revature.data.BankDAO;
import com.revature.util.Util;

public class Employee extends User{
	
	private int employeeID;
	private String header;
	
	private final String menuPrompt = 
			"Please select a choice from the menu below:\n";
	
	public Employee() {
		super();
	}

	public int getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(BankDAO dao) {
		try {
			this.employeeID = dao.getEmployeeID(this.getUserName());
		} 
		
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void prompt(String bankName, BankDAO dao, Scanner input) {
		setEmployeeID(dao);
		
		final String header = bankName + "\n\nEmployee Portal" 
				+ "\n---------------\n\n";
		
		this.header = header;
		
		int choice = 0;
		
		do {
			System.out.println(header + menuPrompt);			
			
			System.out.println("1) View customer accounts");
			System.out.println("2) Approve pending accounts");
			System.out.println("3) View transaction logs");
			System.out.println("4) Logout");
			
			System.out.print("\nPlease enter your choice (1-4) "
					+ "then press 'Enter': ");
			
			try {
				choice = input.nextInt();
				
				if (choice < 1 || choice > 4) {
					throw new InputMismatchException();
				}
				
				switch (choice) {
					case 1:
						viewAccounts(dao);
						choice = 0;
						break;
						
					case 2:
						approveAccounts(dao, input);
						choice = 0;
						break;
						
					case 3:
						viewLogs();
						choice = 0;
						break;
						
					case 4:
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
		while (choice != 4);
		
		return;
	}

	private void viewAccounts(BankDAO dao) {
		try {
			Util.clearScreen(false);
			
			NumberFormat formatter = NumberFormat.getCurrencyInstance();
			
			System.out.println(header + "Customer Accounts:\n");
			
			System.out.println("AccountID: CustomerID: AccountType: Balance:");
			
			ResultSet results = dao.getCustomerAccounts();
			
			while (results.next()) {
				System.out.println("    " + results.getInt(1) + "           "
						+ results.getInt(2) + "           " + results.getInt(3)
						+ "       " + formatter.format(results.getDouble(4)));
			}
			
			Util.clearScreen(true);
		} 
		
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	private void viewLogs() {
		try {
			Util.clearScreen(false);
			
			System.out.println(header + "Transaction log\n");
			
			File log = new File("/home/jason/data/repos/revature/project/"
					+ "project-bank-jasonSchroeder89/bank/system.log");
			
			Scanner logReader = new Scanner(log);
			
			while(logReader.hasNextLine()) {
				String logLine = logReader.nextLine();
				
				System.out.print("\n" + logLine);
			}
			
			logReader.close();
			
			Util.clearScreen(true);
		}
		
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return;
	}

	private void approveAccounts(BankDAO dao, Scanner input) {
		boolean run = true;
		
		do {
			try {
				Util.clearScreen(false);
				
				NumberFormat formatter = NumberFormat.getCurrencyInstance();
				
				ResultSet requests = dao.getRequestedAccounts();
				
				HashSet<Integer> requestIDs = new HashSet<Integer>();
				
				System.out.println(header + "Requested Accounts:\n");
				
				System.out.println("RequestID: CustomerID: AccountType: "
						+ "Initial Balance:");
				
				while(requests.next()) {
					requestIDs.add(requests.getInt(1));
					
					System.out.println("    " + requests.getInt(1) + "         "
							+ "   " + requests.getInt(2) + "             " + 
							requests.getInt(3) + "     " + 
							formatter.format(requests.getDouble(4)));
				}
				
				System.out.println();
				
				int choice = -1;
				
				while (choice < 0) {
					System.out.print("Please enter the request ID to approve "
							+ "(1 - " + requestIDs.size() 
							+ " or '0' to exit): ");
					
					try {
						choice = input.nextInt();
						
						if (choice < 0 || choice > requestIDs.size()) {
							throw new InputMismatchException();
						}
						
						if (choice == 0) {
							run = false;
							
							continue;
						}
						
						dao.createAccount(choice);
					}
					
					catch (InputMismatchException e) {
						System.out.println("Error: Please enter a valid choice "
								+ "or '0' to exit.");
						
						input.nextLine();
						
						choice = -1;
						
						Util.clearScreen(true);
					}
				}
						
			} 
			
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		while (run);
		
		return;
	}	

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return "Employee";
	}
}