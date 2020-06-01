/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package edu.au.cc.gallery;
import java.util.Scanner;
import edu.au.cc.gallery.tools.UserAdmin;

public class App {
    public String getGreeting() {
        return "Hello Kevin.";
    }

    public static void main(String[] args) throws Exception {

	UserAdmin ua = new UserAdmin();
	ua.connect("image_gallery", "kevin");
	App.select(ua);

	//S3.demo();
	//UserAdmin.demo();
	//ua.createUser("kevin", "password", "kevin walton");
//	ua.listAllUsers();
	//ua.editUser("kevin", "secret", "kevin walton");
	//ua.listAllUsers();
	//ua.deleteUser("kevin");
	//ua.listAllUsers();
    }

  public static void menu() {
      System.out.println("\n1) List Users\n"
                       + "2) Add User\n"
                       + "3) Edit User\n"
                       + "4) Delete User\n"
                       + "5) Quit\n");
      System.out.print("Enter command: ");

}

 public static int select(UserAdmin ua) throws Exception {
   //UserAdmin ua = new UserAdmin();
  // ua.connect("image_gallery", "kevin");

   Scanner sc = new Scanner(System.in);
   int selection = 0;
   while (selection != 5) {
   App.menu();

   try {
      String userName = "";
      String password = "";
      String fullName = "";
      selection = Integer.parseInt(sc.nextLine());
      switch (selection) {
      case 1 : ua.listAllUsers();
           break;
      case 2 : System.out.print("Username: ");
               userName = sc.nextLine();
               System.out.print("Password: ");
               password = sc.nextLine();
               System.out.print("Full name: ");
               fullName = sc.nextLine();
	       if (ua.findUser(userName)) {
		System.out.println("User " + userName + " already in database.");
		break;
		}
	       ua.createUser(userName, password, fullName);
               break;
      case 3 : System.out.print("Username: ");
               userName = sc.nextLine();
               System.out.print("Password (press enter to keep current): ");
               password = sc.nextLine();
               System.out.print("Full name (press enter to keep current): ");
               fullName = sc.nextLine();
	       if (!ua.findUser(userName)) {
		System.out.println("User " + userName  + " does not exist in the database." );
		break;
		}
	       ua.editUser(userName, password, fullName);
	       break;
      case 4 : System.out.print("Username: ");
		userName = sc.nextLine();
		if (!ua.findUser(userName)) {
		System.out.println("User with username " + userName + " does not exist");
		break;
		}
		ua.deleteUser(userName);
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
