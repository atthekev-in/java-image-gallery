package edu.au.cc.gallery.tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


import java.sql.*;

public class UserAdmin {

	private static final String dbUrl = "jdbc:postgresql://demo-database-2.cunmw1tbyasz.us-east-2.rds.amazonaws.com/image_gallery"; 
	private Connection conn;


	public void connect(String userName, String password) throws SQLException {
        try {
             	conn = DriverManager.getConnection(dbUrl, userName , password);
        }
        catch (Exception ex) {
                ex.printStackTrace();
                System.exit(1);
        	}

	}

	public static void demo() throws SQLException {
	UserAdmin ua = new UserAdmin();
	ua.connect("image_gallery", "kevin");
	}

	public boolean findUser(String userName) throws SQLException {
	String sql = "select username from users where username = (?)";
	PreparedStatement ps = conn.prepareStatement(sql);
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
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		System.out.println("username\tpassword\tfull name");
		System.out.println("-------------------------------------------");
		while (rs.next()) {
		System.out.printf("%-15s %-15s %-15s %n", rs.getString(1), rs.getString(2), rs.getString(3));
		}
		rs.close();
	}

	public void createUser(String userName, String password, String fullName) throws SQLException {
		try {
		String sql = "insert into users values (?, ?, ?)";
		PreparedStatement ps = conn.prepareStatement(sql);
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
                	PreparedStatement ps = conn.prepareStatement(sql);
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
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, userName);
		ps.executeUpdate();
		} catch (SQLException e) {
		System.out.println(e.getMessage());
		}
	}

}
