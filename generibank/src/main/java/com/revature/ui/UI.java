package com.revature.ui;

import java.util.Scanner;

import com.revature.enums.State;

public abstract class UI {
	
	protected String bankName;
	protected final String clearScreenPrompt = 
			"\n\nPress 'Enter' to continue.... ";
	
	public UI(String bankName) {
		this.bankName = bankName;
	}
}