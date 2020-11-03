package com.revature.ui;

import com.revature.enums.State;

public abstract class UI {
	
	private final String bankName;
	
	public UI(String bankName) {
		this.bankName = bankName;
	}
}