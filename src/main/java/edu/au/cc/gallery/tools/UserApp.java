package edu.au.cc.gallery.tools;

import edu.au.cc.gallery.DB;
import edu.au.cc.gallery.User;

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
            ArrayList<User> users = db.getUsernameAndFullName();
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
        get("/admin/deleteUser/:user", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            String user = req.params(":user");
            model.put("username", user);
            return new HandlebarsTemplateEngine().render(new ModelAndView(model, "deleteUser.hbs"));        
        });
        post("/admin/addUser", (req, res) -> {
            String[] user = { req.queryParams("username"), req.queryParams("password"), req.queryParams("full_name") };
            db.createUser(user);
            res.redirect("/admin");
            return "added user";

        });
        post("/admin/editUser", (req, res) -> {
            db.editUser(req.queryParams("username"), req.queryParams("password"), req.queryParams("full_name"));
            res.redirect("/admin");
            return "modified user";

        });
        post("/admin/deleteUser", (req, res) -> {
            db.deleteUser(req.queryParams("username"));
            res.redirect("/admin");
            return "deleted user";

        });

    }

}

