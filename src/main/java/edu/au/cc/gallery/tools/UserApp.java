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
        get("/", (req, res) -> homePage(req, res));
        get("/admin", (req, res) -> adminPage(req, res));
        get("/login", (req, res) -> loginPage(req, res));
        get("/admin/addUser", (req, res) -> addUserPage(req, res));
        get("/admin/editUser/:user", (req, res) -> editUserPage(req, res));
        get("/admin/deleteUser/:user", (req, res) -> deleteUserPage(req, res));
        post("/admin/addUser", (req, res) ->  addUser(req, res));
        post("/admin/editUser", (req, res) -> editUser(req, res));
        post("/admin/deleteUser", (req, res) -> deleteUser(req, res));
        post("/login", (req, res) -> loginUser(req, res));
        get("/debugSession", (req, res) -> debugSession(req, res));
    }
    private String homePage(Request req, Response res) throws SQLException {
        Map<String, Object> model = new HashMap<>();
        // ArrayList<User> users = db.getUsernameAndFullName();
        // model.put("users", users);
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, "home.hbs"));
    }
    private String adminPage(Request request, Response res) throws SQLException {
        Map<String, Object> model = new HashMap<>();
        ArrayList<User> users = db.getUsernameAndFullName();
        model.put("users", users);
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, "admin.hbs"));
    }
    private String loginPage(Request req, Response res) throws SQLException {
        Map<String, Object> model = new HashMap<>();
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, "login.hbs"));
    }
    private String addUserPage(Request req, Response res) throws SQLException {
        Map<String, Object> model = new HashMap<>();
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, "addUser.hbs"));
    }
    private String editUserPage(Request req, Response res) throws SQLException {
        Map<String, Object> model = new HashMap<>();

        String user = req.params(":user");
        model.put("username", user);
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, "editUser.hbs"));
    }
    private String deleteUserPage(Request req, Response res) throws SQLException {
        Map<String, Object> model = new HashMap<>();
        String user = req.params(":user");
        model.put("username", user);
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, "deleteUser.hbs"));
    }
    private String addUser(Request req, Response res) throws SQLException {
        String[] user = { req.queryParams("username"), req.queryParams("password"), req.queryParams("full_name") };
        db.createUser(user);
        res.redirect("/admin");
        return "";
    }
    private String editUser(Request req, Response res) throws SQLException {
        db.editUser(req.queryParams("username"), req.queryParams("password"), req.queryParams("full_name"));
        res.redirect("/admin");

        return "";
    }
    private String deleteUser(Request req, Response res) throws SQLException {
        db.deleteUser(req.queryParams("username"));
        res.redirect("/admin");
        return "";
    }
    private String loginUser(Request req, Response res) throws SQLException {
        String username = req.queryParams("username");
	System.out.println(username);
        User user = db.getUser(username);
        if (user == null || !user.getPassword().equals(req.queryParams("password"))) {
            res.redirect("/login");

        } else {
            req.session().attribute("user", username);
            res.redirect("/debugSession");
        }

        return "";
    }
    private String debugSession(Request req, Response res) {
        StringBuffer sb = new StringBuffer();
        for (String key : req.session().attributes()) {
            sb.append(key + "->" + req.session().attribute(key) + "<br>");
        }
        return "";
    };
}

