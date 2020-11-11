package com.revature.banksystem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.revature.data.BankDAO;
import com.revature.ui.SystemUI;
import com.revature.user.NewUser;
import com.revature.user.User;
import com.revature.util.Util;

public class BankSystem {
	
	private Scanner input;
	private SystemUI ui;
	private User user;
	private BankDAO dao;
	private ResultSet results;
	private boolean regSuccess;
	private boolean loginSuccess;
	private boolean runUserSession;
	
	public BankSystem(String bankName) {
		this.input = new Scanner(System.in);
		this.ui = new SystemUI(bankName, input);
		this.dao = new BankDAO();
	}
	
	public void run() {
		do {
			user = ui.welcomePrompt();
			
			if (user == null) {
				System.out.println("\nGoodbye!\n");
				
				return;
			}
			
			if (user.getType().equals("New")) {
				try {
					results = dao.getUserNames();
					
					Util.clearScreen(false);
					
					regSuccess = ui.regPrompt(results, user);
					
					if (regSuccess) {
						dao.createNewCustomer(user);
						
						ui.regSuccessMessage();
						
						Util.clearScreen(true);
						
						continue;
					}
					
					else {
						ui.exitingMessage();
						
						continue;
					}					
				}
				
				catch (SQLException e) {
					System.out.println("Error Connecting to Database:"
							+ e.getStackTrace());
					
					System.out.println("Debug connection!");
					
					return;
				}
			}
			
			else {
				loginSuccess = ui.login(dao, input);
				
				if (loginSuccess) {
					runUserSession = true;
					
					while (runUserSession) {
						runUserSession = user.prompt(dao, input);
					}
					
					Util.clearScreen(false);
					
					continue;
				}
				
				else {
					ui.exitingMessage();
					
					return;
				}
			}
		}
		while(true);				
	}
}