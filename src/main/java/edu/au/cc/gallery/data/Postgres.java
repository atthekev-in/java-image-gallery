package edu.au.cc.gallery.data;

import java.sql.SQLException;
import java.util.ArrayList;

public class Postgres {

    DB db = new DB();

    
    public ArrayList<User> getAllUsers() throws SQLException {
        ArrayList<User> users = db.getUsernameAndFullName();
        return users;
    }
    public ArrayList<Image> viewAllImages(String username) throws SQLException {
        String sql = "select * from images where username = ?";
        ArrayList<Image> images = db.getAllImages(sql, username);
        return images;
    }
    public void uploadImage(Image image) throws SQLException {
        db.insertImage(image);
    }
    public void deleteImage(String imageId) throws SQLException {
        String sql = "delete from images where imageid = ?;";
        db.deleteImage(sql, imageId);
    }

}
