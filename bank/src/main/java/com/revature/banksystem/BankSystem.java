package com.revature.banksystem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import org.apache.logging.log4j.Logger;

import com.revature.data.BankDAO;
import com.revature.ui.SystemUI;
import com.revature.user.User;
import com.revature.util.Util;

public class BankSystem {
	
	private final String bankName;
	private Scanner input;
	private SystemUI ui;
	private User user;
	private BankDAO dao;
	private ResultSet results;
	private boolean regSuccess;
	private int loginSuccess;
	
	public BankSystem(String bankName, Logger logger) {
		this.bankName = bankName;
		this.input = new Scanner(System.in);
		this.ui = new SystemUI(bankName, input);
		this.dao = new BankDAO(logger);
	}
	
	public void run() {
		do {
			user = ui.welcomePrompt();
			
			Util.clearScreen(false);
			
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
					e.getStackTrace();
					
					return;
				}
			}
			
			else {
				loginSuccess = ui.login(user, dao, input);
				
				if (loginSuccess == 1) {
					Util.clearScreen(true);
					
					user.prompt(bankName, dao, input);
					
					Util.clearScreen(false);
					
					continue;
				}
				
				else if (loginSuccess == -1) {
					Util.clearScreen(false);
					
					continue;
				}
				
				else {
					ui.exceedLoginAttemptsMessage();
					
					ui.exitingMessage();
					
					return;
				}
			}
		}
		while(true);				
	}
}