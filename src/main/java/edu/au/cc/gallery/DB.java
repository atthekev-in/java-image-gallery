package edu.au.cc.gallery;

import java.sql.*;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public class DB {

	private static final String dbUrl = "jdbc:postgresql://demo-database-2.cunmw1tbyasz.us-east-2.rds.amazonaws.com/image_gallery";
	private Connection connection;
	private String getPassword() {
		try(BufferedReader br = new BufferedReader(new FileReader("/home/ec2-user/.sql-passwd"))) {
		String result = br.readLine();

		return result;
		} catch (IOException ex) {
		System.err.println("Error opening password file");
	    	System.exit(1);
	}
	return null;
	}
	public void connect() throws SQLException {
	try {

		connection = DriverManager.getConnection(dbUrl, "image_gallery" , getPassword());
	}
		 catch (Exception ex) {
		ex.printStackTrace();
		System.exit(1);
	}

	}


	public static void  demo() throws Exception {
	DB db = new DB();
	db.connect();

	}



}
