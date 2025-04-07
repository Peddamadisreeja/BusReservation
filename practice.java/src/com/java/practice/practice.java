package com.java.practice;
import java.util.*;
import java.sql.*;
import java.sql.*;




class Demo{
	public static void main(String[]args) {
		try {
		    Class.forName("com.mysql.cj.jdbc.Driver");
		    System.out.println("driver loaded successfully");
		    String URL = "jdbc:mysql://localhost:3306/bus_reservation";
		    String USER = "root";
	        String PASSWORD = "admin";
		    Connection con=DriverManager.getConnection(URL,USER,PASSWORD);
		    System.out.println("connection successfully");
		    
		    
		    		
		    
		}
		catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
 
		
	}
}