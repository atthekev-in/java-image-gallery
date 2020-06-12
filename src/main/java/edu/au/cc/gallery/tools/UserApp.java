package edu.au.cc.gallery.tools;
import edu.au.cc.gallery.DB;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static spark.Spark.*;
import spark.Request;
import spark.Response;
import spark.ModelAndView;
import spark.Route;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class UserApp {
    DB db = new DB();
    
    public void addRoutes() {
        get("/admin", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            ArrayList<String> users = db.getUserNames();
            model.put("users", users);
            return new HandlebarsTemplateEngine().render(new ModelAndView(model, "admin.hbs"));
        });
        get("/admin/addUser", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return new HandlebarsTemplateEngine().render(new ModelAndView(model, "addUser.hbs"));
        });
        
        get("/admin/editUser/:user", (req, res) -> {
            Map<String, Object> model = new HashMap<>();

            String user = req.params(":user");
            model.put("username", user);
            return new HandlebarsTemplateEngine().render(new ModelAndView(model, "editUser.hbs"));
        });
        post("/admin/addUser", (req, res) -> createUser(req, res));
        post("/admin/editUser", (req, res) -> editUser(req, res));
        post("/admin/deleteUser", (req, res) -> deleteUser(req,res));
    }
    
   

    public String createUser(Request req, Response res) throws SQLException {
        String[] user = {req.queryParams("username"), req.queryParams("password"), req.queryParams("full_name")};
        db.createUser(user);
        return "user added";
    }
    public String editUser(Request req, Response res) throws SQLException {
	System.out.println(req.queryParams("username") + req.queryParams("password") + req.queryParams("full_name"));
        db.editUser(req.queryParams("username"), req.queryParams("password"), req.queryParams("full_name"));
        return "user edited";
    }
    public String deleteUser(Request req, Response res) throws SQLException {
        db.deleteUser(req.queryParams("username"));
        return "user deleted";
    }
}
