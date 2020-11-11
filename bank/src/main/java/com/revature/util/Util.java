package com.revature.util;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

public class Util {
	
	public static String validateUserName(String userName) {
		return null;
	}
	
	public static HashSet<String> getUserNameSet(ResultSet results) {
		HashSet<String> userNames = new HashSet<String>();
		
		try {
			while(results.next()) {
				userNames.add(results.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return userNames;
	}
	
	public static void clearScreen(boolean prompt) {
		if (prompt) {
			try {
				System.out.print("\n\nPress 'Enter' to continue.... ");
				System.in.read();
			} 
			catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
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
