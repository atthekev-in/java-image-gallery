package edu.au.cc.gallery.data;

import edu.au.cc.gallery.aws.*;
import java.sql.*;
import java.util.ArrayList;
import org.json.JSONObject;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public class DB {

	private static final String dbUrl = "jdbc:postgresql://image-gallery.cunmw1tbyasz.us-east-2.rds.amazonaws.com/";
	private Connection connection;
	private JSONObject getSecret() {
	String s = Secrets.getSecretImageGallery();
	return new JSONObject(s);
        }

        private String getPassword(JSONObject secret) {
	return secret.getString("password");
        }
   

	public void connect() throws SQLException {
		try {

			JSONObject secret = getSecret();
			connection = DriverManager.getConnection(dbUrl, "image_gallery", getPassword(secret));
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		}

	}

	public void insertImage(Image image) throws SQLException {
		String sql = "insert into images values (?, ?);";
		try {
			connect();
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, image.getImageId());
			ps.setString(2, image.getUsername());
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error");
		} finally {
			connection.close();
		}
	}

	public ArrayList<Image> getAllImages(String sql, String username) throws SQLException {
		ArrayList<Image> images = new ArrayList<>();
		try {
			connect();
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Image image = new Image(rs.getString("imageid"), rs.getString("username"));
				images.add(image);
			}
			rs.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			connection.close();
		}
		return images;
	}

	public User getUser(String username) throws SQLException {
		connect();
		String sql = "select * from users where username = ?;";
		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setString(1, username);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			User user = new User(rs.getString("username"), rs.getString("password"), rs.getString("full_name"));
			connection.close();
			return user;
		} else {
			connection.close();
			return null;
		}

	}

	public ArrayList<User> getUsernameAndFullName() throws SQLException {
		String sql = "select username, full_name from users;";
		ArrayList<User> userData = new ArrayList<>();
		connect();
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				User user = new User();
				user.setUsername(rs.getString("username"));
				user.setFull_name(rs.getString("full_name"));
				userData.add(user);
			}
			rs.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			connection.close();
		}

		return userData;
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

	public void createUser(String[] user) throws SQLException {
		try {
			connect();
			String sql = "insert into users values (?, ?, ?)";
			PreparedStatement ps = connection.prepareStatement(sql);
			for (int i = 0; i < user.length; i++) {
				ps.setString(i + 1, user[i]);
			}
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error: User with username " + user[0] + " already exists. ");
		} finally {
			connection.close();
		}
	}

	public void editUser(String userName, String password, String fullName) throws SQLException {
		String sql = "";
		if (password.equals("") && fullName.equals("")) {
			return;
		}
		if (!password.equals("") && !fullName.equals("")) {
			sql = "update users set password = (?), full_name = (?) where username = (?)";
		} else if (password.equals("")) {
			sql = "update users set full_name = (?) where username = (?)";
		} else {
			sql = "update users set password = (?) where username = (?)";
		}
		try {
			connect();
			PreparedStatement ps = connection.prepareStatement(sql);
			if (!password.equals("") && !fullName.equals("")) {
				ps.setString(1, password);
				ps.setString(2, fullName);
				ps.setString(3, userName);
			} else if (password.equals("")) {
				ps.setString(1, fullName);
				ps.setString(2, userName);
			} else {
				ps.setString(1, password);
				ps.setString(2, userName);
			}
			ps.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

	public void deleteUser(String userName) throws SQLException {

		String sql = "delete from users where username = (?)";
		try {
			connect();
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, userName);
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			connection.close();
		}
	}
}

