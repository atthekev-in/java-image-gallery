package edu.au.cc.gallery.tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import edu.au.cc.gallery.DB;

import java.sql.*;

public class UserAdmin {

 private DB db;

public UserAdmin() throws SQLException {
this.db = new DB();
db.connect();
}

  public void menu() {
      System.out.println("\n1) List Users\n"
                       + "2) Add User\n"
                       + "3) Edit User\n"
                       + "4) Delete User\n"
                       + "5) Quit\n");
      System.out.print("Enter command: ");

}

 public int select() throws Exception {
   //UserAdmin ua = new UserAdmin();
  // ua.connect("image_gallery", "kevin");

   Scanner sc = new Scanner(System.in);
   int selection = 0;
   while (selection != 5) {
   menu();

   try {
      String userName = "";
      String password = "";
      String fullName = "";
      selection = Integer.parseInt(sc.nextLine());
      switch (selection) {
      case 1 : db.listAllUsers();
           break;
      case 2 : System.out.print("Username: ");
               userName = sc.nextLine();
               System.out.print("Password: ");
               password = sc.nextLine();
               System.out.print("Full name: ");
               fullName = sc.nextLine();
	       if (db.findUser(userName)) {
		System.out.println("User " + userName + " already in database.");
		break;
		}
		String[] newUser = {userName, password, fullName};
	       db.createUser(newUser);
               break;
      case 3 : System.out.print("Username: ");
               userName = sc.nextLine();
               System.out.print("Password (press enter to keep current): ");
               password = sc.nextLine();
               System.out.print("Full name (press enter to keep current): ");
               fullName = sc.nextLine();
	       if (!db.findUser(userName)) {
		System.out.println("User " + userName  + " does not exist in the database." );
		break;
		}
	       db.editUser(userName, password, fullName);
	       break;
      case 4 : System.out.print("Username: ");
		userName = sc.nextLine();
		if (!db.findUser(userName)) {
		System.out.println("User with username " + userName + " does not exist");
		break;
		}
		db.deleteUser(userName);
		System.out.println("User deleted");
		break;
      case 5 : System.out.println("Goodbye");
               System.exit(0);
               break;
      default : System.out.println("Invalid Input. Please try again.");
      
      }
   }
   catch (Exception e) {
      System.out.println("Invalid Input. Please try again.");
   }
   }
   return selection;
   
   }

}
