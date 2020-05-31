/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package edu.au.cc.gallery;

import edu.au.cc.gallery.tools.UserAdmin;

public class App {
    public String getGreeting() {
        return "Hello Kevin.";
    }

    public static void main(String[] args) throws Exception {
	// System.out.println(new App().getGreeting());

	//S3.demo();
	//UserAdmin.demo();
	UserAdmin ua = new UserAdmin();
	ua.connect("image_gallery", "kevin");
	ua.createUser("kevin", "password", "kevin walton");
	ua.listAllUsers();
	ua.editUser("kevin", "secret", "kevin walton");
	ua.listAllUsers();
	ua.deleteUser("kevin");
	ua.listAllUsers();
    }
}
