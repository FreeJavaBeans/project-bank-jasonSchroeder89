package com.revature.user;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.revature.data.BankDAO;
import com.revature.util.Util;

public class Customer extends User {

	private int customerID;
	private String header;
	
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
		
		this.header = header;
		
		int choice = 0;
		
		do {
			System.out.println(header + menuPrompt);			
			
			System.out.println("1) View accounts");
			System.out.println("2) Apply for a new account");
			System.out.println("3) Withdraw funds");
			System.out.println("4) Deposit funds");
			System.out.println("5) Transfer funds");
			System.out.println("6) Logout");
			
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
						postTransfer(dao, input);
						choice = 0;
						break;
						
					case 6:
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
	
	private void postTransfer(BankDAO dao, Scanner input) {
		try {
			HashMap<Integer, Double> allAccounts = 
					new HashMap<Integer, Double>();
			
			HashMap<Integer, Double> userAccounts = 
					new HashMap<Integer, Double>();
			
			ResultSet validAccounts = dao.getAccountBalances();
			
			while(validAccounts.next()) {
				allAccounts.put(validAccounts.getInt(1), 
						validAccounts.getDouble(2));
				
				if (validAccounts.getInt(3) == this.getCustomerID()) {
					userAccounts.put(validAccounts.getInt(1), 
							validAccounts.getDouble(2));
				}
			}
			
			System.out.print(header);
			
			System.out.println("Transfer Funds\n");
			
			int targetAccountNum = -1;
			
			while (targetAccountNum < 0) {
				System.out.print("Enter account number in which to "
						+ "transfer funds or '0' to exit: ");
				
				try {
					targetAccountNum = input.nextInt();
					
					if (targetAccountNum < 0 
							|| !allAccounts.containsKey(targetAccountNum)) {
						
						throw new InputMismatchException();
					}
					
					if (targetAccountNum == 0) {
						return;
					}					
				}
				
				catch (InputMismatchException e) {
					System.out.println("\nError: Invalid account number.\n"
							+ "\nPlease enter a valid account number, or '0' to" 
							+ " exit");
					
					targetAccountNum = -1;
				}
			}
			
			System.out.println();
			
			int sourceAccountNum = -1;
			
			while (sourceAccountNum < 0) {
				System.out.print("Enter account number from which to "
						+ "transfer funds or '0' to exit: ");
				
				try {
					sourceAccountNum = input.nextInt();
					
					if (sourceAccountNum < 0 
							|| !userAccounts.containsKey(sourceAccountNum)) {
						
						throw new InputMismatchException();
					}
					
					if (sourceAccountNum == 0) {
						return;
					}					
				}
				
				catch (InputMismatchException e) {
					System.out.println("\nError: Invalid account number.\n"
							+ "\nPlease enter a valid account number, or '0' to" 
							+ " exit");
					
					input.nextLine();
					
					sourceAccountNum = -1;
					
					Util.clearScreen(true);
				}
			}
			
			System.out.println();
			
			final double transferLimit = userAccounts.get(sourceAccountNum);
			
			double transferAmount = -1.0;
			
			while (transferAmount < 0.0) {
				System.out.print("Please enter amount to transfer or -99 to "
						+ "exit (Dollars.Cents): ");
				
				try {
					transferAmount = input.nextDouble();
					
					if (transferAmount == -99.0) {
						Util.clearScreen(false);
						
						return;
					}
					
					transferAmount = roundVal(transferAmount);
					
					if (transferAmount < 0.01 
							|| 	transferAmount > transferLimit) {
						
						throw new InputMismatchException();
					}
					
					if ((allAccounts.get(targetAccountNum) + transferAmount) 
							> 999999999999.99) {
						
						throw new IllegalArgumentException();
					}
				}
				
				catch (InputMismatchException e) {
					System.out.println("\nError: Invalid transaction amount.\n"
							+ "\nPlease enter an amount less than or equal to "
							+ "the transcation limit, or -99 to exit");
					
					input.nextLine();
					
					System.out.println();
					
					transferAmount = -1.0;
				}
				
				catch (IllegalArgumentException e) {
					System.out.println("\nError: transfer amount exceeds "
							+ "transaction limit. Please transfer a smaller "
							+ "amount.");
					
					input.nextLine();
					
					System.out.println();
					
					transferAmount = -1.0;
				}				
			}
			
			System.out.println();
			
			double newSourceBalance = userAccounts.get(sourceAccountNum) -
					transferAmount;
			
			double newTargetBalance = allAccounts.get(targetAccountNum) +
					transferAmount;
			
			dao.transferFunds(sourceAccountNum, newSourceBalance,
					targetAccountNum, newTargetBalance);
			
			System.out.print("Transfer Successful!");
			
			Util.clearScreen(true);			
		}
		
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return;
	}

	private void depositFunds(BankDAO dao, Scanner input) {
		try {
			HashMap<Integer, Double> accountBalances = 
					new HashMap<Integer, Double>();
			
			ResultSet accounts = dao.getAccounts(this.getCustomerID());
			
			Util.clearScreen(false);
			
			System.out.print(header);
			
			System.out.println("Deposit Funds\n");
			
			System.out.println("Account ID:  Account Type:  "
					+ "Balance:\n");
			
			try {
				while(accounts.next()) {
					int accountID = accounts.getInt(1);
					String type = accounts.getString(2);
					double balance = accounts.getDouble(3);
					
					accountBalances.put(accountID, balance);
					
					System.out.println("     " + accountID + "       " 
							+ type + "      " + balance);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println();
			
			int accountNum = 0;
			
			while (accountNum < 1) {
				System.out.print("Please select account to deposit funds, or "
						+ "enter '0' to exit: ");
				
				try {
					accountNum = input.nextInt();
					
					if (accountNum < 0 || accountNum > accountBalances.size()) {
						throw new InputMismatchException();
					}
					
					if (accountNum == 0) {
						Util.clearScreen(false);
						
						return;
					}
					
					System.out.println();
				}
				
				catch (InputMismatchException e) {
					System.out.println("\nError: Please enter a valid choice "
							+ "(1 - " + accountBalances.size() + ") or '0' to "
							+ "exit");
					
					input.nextLine();
					
					System.out.println();
					
					accountNum = -1;
				}	
			}
			
			final double depositLimit = 999999999999.99 - 
					accountBalances.get(accountNum);
			
			double depositAmount = -1.0;
			
			while (depositAmount < 0.0) {
				System.out.print("Please enter amount to deposit or -99 to "
						+ "exit (Dollars.Cents): ");
				
				try {
					depositAmount = input.nextDouble();
					
					if (depositAmount == -99.0) {
						Util.clearScreen(false);
						
						return;
					}
					
					depositAmount = roundVal(depositAmount);
					
					if (depositAmount < 0.01 
							|| 	depositAmount + accountBalances.get(accountNum)
								> depositLimit) {
						
						throw new InputMismatchException();
					}
				}
				
				catch (InputMismatchException e) {
					System.out.println("\nError: Invalid transaction amount.\n"
							+ "\nPlease enter an amount that does not exceed "
							+ "the deposit limit, or -99 to exit");
					
					input.nextLine();
					
					System.out.println();
					
					depositAmount = -1.0;
				}
			}
			
			System.out.println();
			
			double newBalance = accountBalances.get(accountNum) + depositAmount;
			
			dao.setNewAccountBalance(accountNum, newBalance);
			
			System.out.print("Deposit request successful!");
			
			Util.clearScreen(true);
		}
		
		catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return;
		
	}

	private void withdrawFunds(BankDAO dao, Scanner input) {
		try {
			HashMap<Integer, Double> accountBalances = 
					new HashMap<Integer, Double>();
			
			ResultSet accounts = dao.getAccounts(this.getCustomerID());
			
			Util.clearScreen(false);
			
			System.out.print(header);
			
			System.out.println("Withdraw Funds\n");
			
			System.out.println("Account ID:  Account Type:  "
					+ "Balance:\n");
			
			try {
				while(accounts.next()) {
					int accountID = accounts.getInt(1);
					String type = accounts.getString(2);
					double balance = accounts.getDouble(3);
					
					accountBalances.put(accountID, balance);
					
					System.out.println("     " + accountID + "       " 
							+ type + "      " + balance);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println();
			
			int accountNum = 0;
			
			while (accountNum < 1) {
				System.out.print("Please select account to withdraw from, or "
						+ "enter '0' to exit: ");
				
				try {
					accountNum = input.nextInt();
					
					if (accountNum < 0 || accountNum > accountBalances.size()) {
						throw new InputMismatchException();
					}
					
					if (accountNum == 0) {
						Util.clearScreen(false);
						
						return;
					}
					
					System.out.println();
				}
				
				catch (InputMismatchException e) {
					System.out.println("\nError: Please enter a valid choice "
							+ "(1 - " + accountBalances.size() + ") or '0' to "
							+ "exit");
					
					input.nextLine();
					
					System.out.println();
					
					accountNum = -1;
				}	
			}
			
			final double withdrawLimit = accountBalances.get(accountNum);
			
			double withdrawAmount = -1.0;
			
			while (withdrawAmount < 0.0) {
				System.out.print("Please enter amount to withdraw or -99 to "
						+ "exit (Dollars.Cents): ");
				
				try {
					withdrawAmount = input.nextDouble();
					
					if (withdrawAmount == -99.0) {
						Util.clearScreen(false);
						
						return;
					}
					
					withdrawAmount = roundVal(withdrawAmount);
					
					if (withdrawAmount < 0.01 
							|| 	withdrawAmount > withdrawLimit) {
						
						throw new InputMismatchException();
					}
				}
				
				catch (InputMismatchException e) {
					System.out.println("\nError: Invalid transaction amount.\n"
							+ "\nPlease enter an amount less than or equal to "
							+ "the transcation limit, or -99 to exit");
					
					input.nextLine();
					
					System.out.println();
					
					withdrawAmount = -1.0;
				}
			}
			
			System.out.println();
			
			double newBalance = withdrawLimit - withdrawAmount;
			
			dao.setNewAccountBalance(accountNum, newBalance);
			
			System.out.print("Withdraw request successful!");
			
			Util.clearScreen(true);
		}
		
		catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return;
	}

	private void applyForAccount(BankDAO dao, Scanner input) {
		Util.clearScreen(false);
		
		System.out.print(header);
		
		System.out.println("New Account Request\n");
		
		int accountType = -1;
		
		while (accountType < 0) {
			System.out.print("Please select account type (1=Checking/"
					+ "2=Savings/0=Quit): ");
			
			try {
				accountType = input.nextInt();
				
				if (accountType < 0 || accountType > 2) {
					throw new InputMismatchException();
				}			
				
				System.out.println();
			}
			
			catch (InputMismatchException e) {
				System.out.println("\nError: Please enter a valid choice "
						+ "(0,1,2)");
				
				input.nextLine();
				
				System.out.println();
				
				accountType = -1;
			}	
		}
		
		if (accountType == 0) {
			Util.clearScreen(false);
			
			return;
		}
		
		else {
			double newBalance = 0.0;
			
			boolean validBalance = false;
			
			while (!validBalance) {
				System.out.print("Please enter starting balance for new "
						+ "account or -99 to exit (Dollars.Cents): ");
				
				try {
					newBalance = input.nextDouble();
					
					if (newBalance == -99.0) {
						Util.clearScreen(false);
						
						return;
					}
					
					newBalance = roundVal(newBalance);
					
					if (newBalance < 0.01 || newBalance > 999999999999.99) {
						throw new InputMismatchException();
					}
					
					validBalance = true;
					
				}
				
				catch (InputMismatchException e) {
					System.out.println("\nError: Invalid starting balance. ");
					
					input.nextLine();
					
					System.out.println();
					
					continue;
				}
			}
			
			try {
				dao.requestAccount(this.getCustomerID(), accountType, 
						newBalance);
				
				System.out.print("\nAccount Request Succesfully Added!");
				
				Util.clearScreen(true);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void showAccounts(BankDAO dao) {
		Util.clearScreen(false);
		
		ResultSet results;
		
		try {
			results = dao.getAccounts(this.getCustomerID());
			
			System.out.println(header + "Account ID:  Account Type:  "
					+ "Balance:");
			
			try {
				while(results.next()) {
					int accountID = results.getInt(1);
					String type = results.getString(2);
					double balance = results.getDouble(3);
					
					System.out.println("     " + accountID + "       " 
							+ type + "      " + balance);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Util.clearScreen(true);
		} 
		
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	private double roundVal(double val) {
		BigDecimal rounder = new BigDecimal(val)
				.setScale(2, RoundingMode.HALF_UP);
		
		double newVal = rounder.doubleValue();
		
		return newVal;
	}

	@Override
	public String getType() {
		return "Customer";
	}
}