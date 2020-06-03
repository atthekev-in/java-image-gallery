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


	public void  demo() throws Exception {
	DB db = new DB();
	db.connect();
	db.listAllUsers();
	}

	public boolean findUser(String userName) throws SQLException {
	String sql = "select username from users where username = (?)";

	PreparedStatement ps = connection.prepareStatement(sql);
	ps.setString(1, userName);
	ResultSet rs = ps.executeQuery();
	if (rs.next()) {
	return true;
	} else {
		return false;
	}

	}

	public void listAllUsers() throws SQLException {
		String sql = "select * from users;";
		try {
		PreparedStatement ps = connection.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		System.out.println("username\tpassword\tfull name");
		System.out.println("-------------------------------------------");
		while (rs.next()) {
		System.out.printf("%-15s %-15s %-15s %n", rs.getString(1), rs.getString(2), rs.getString(3));
		}
		rs.close();
		} catch (SQLException e) {
		System.out.println(e.getMessage());
		}
	}

	public void createUser(String userName, String password, String fullName) throws SQLException {
		try {
		String sql = "insert into users values (?, ?, ?)";
		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setString(1, userName);
		ps.setString(2, password);
		ps.setString(3, fullName);
		ps.executeUpdate();
		    } catch (SQLException e) {
			System.out.println("Error: User with username " + userName + " already exists. ");
		    }
		}

	public void editUser(String userName, String password, String fullName) throws SQLException {
		String sql = "";
		if (password.equals("") && fullName.equals("")) {
		return;
		}
		if (!password.equals("") && !fullName.equals("")) {
         	 sql = "update users set password = (?), full_name = (?) where username = (?)";
		}
		else if (password.equals("")) {
		sql = "update users set full_name = (?) where username = (?)";
		}
		else {
		sql = "update users set password = (?) where username = (?)";
		}
		try {
                	PreparedStatement ps = connection.prepareStatement(sql);
                	if (!password.equals("") && !fullName.equals("")) {
			ps.setString(1, password);
			ps.setString(2, fullName);
			ps.setString(3, userName);
			ps.executeUpdate();
		        }
               		else  if (password.equals("")) {
			ps.setString(1, fullName);
			ps.setString(2, userName);
			ps.executeUpdate();
			}
			else {
			ps.setString(1, password);
			ps.setString(2, userName);
			ps.executeUpdate();
			}
		} catch (SQLException e) {
		System.out.println(e.getMessage());
		}
	}

	public void deleteUser(String userName) throws SQLException {

		String sql = "delete from users where username = (?)";
		try {
		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setString(1, userName);
		ps.executeUpdate();
		} catch (SQLException e) {
		System.out.println(e.getMessage());
		}
	}

}
