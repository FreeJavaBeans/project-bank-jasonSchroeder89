package com.revature.ui;

import java.sql.PreparedStatement;

import com.revature.enums.State;

public interface Display {
	
	public State prompt();
	
	public PreparedStatement requestData();
}
