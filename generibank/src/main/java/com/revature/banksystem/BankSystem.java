package com.revature.banksystem;

import java.sql.ResultSet;
import java.util.Scanner;

import com.revature.data.BankDAO;
import com.revature.ui.SystemUI;
import com.revature.user.NewUser;
import com.revature.user.User;

public class BankSystem {
	
	private Scanner input;
	private SystemUI ui;
	private User user;
	private BankDAO dao;
	private ResultSet results;
	private boolean regSuccess;
	private boolean loginSuccess;
	
	public BankSystem(String bankName) {
		this.input = new Scanner(System.in);
		this.ui = new SystemUI(bankName, input);
		this.dao = new BankDAO();
	}
	
	public void run() {
		boolean run = true;
		
		do {
			ui.welcomePrompt(user);
			
			if (user == null) {
				System.out.println("\nGoodbye!\n");
				
				return;
			}
			
			if (user.getClass() == NewUser.class) {
				results = dao.getUserNames();
				
				regSuccess = ui.regPrompt(results, user);
				
				if (regSuccess) {
					dao.createNewCustomer(user);
				}
				
				ui.regSuccessMessage();
				
				SystemUI.clearScreen();
				
				continue;
			}
			
			else {
				loginSuccess = user.login(results, input);
				
				if (loginSuccess) {
					continue;
				}
				
				else {
					continue;
				}
			}
		}
		while(run);				
	}
}