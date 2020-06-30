package edu.au.cc.gallery.tools;

import edu.au.cc.gallery.data.*;
import edu.au.cc.gallery.aws.*;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.MultipartConfigElement;
import static spark.Spark.*;
import spark.Request;
import spark.Response;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;
import spark.utils.IOUtils;

public class UserApp {
    private DB db = new DB();
    private final String bucketName = "edu.au.cc.kzw0068.image-gallery";

    public void addRoutes() {

        // get routes
        get("/", (req, res) -> homePage(req, res));
        get("/admin/users", (req, res) -> adminPage(req, res));
        get("/admin/addUser", (req, res) -> addUserPage(req, res));
        get("/admin/deleteUser/:user", (req, res) -> deleteUserPage(req, res));
        get("/admin/editUser/:user", (req, res) -> editUserPage(req, res));
        get("/debugSession", (req, res) -> debugSession(req, res));

        get("/login", (req, res) -> loginPage(req, res));
        get("/upload", (req, res) -> uploadPage(req, res));
        get("/view", (req, res) -> viewPage(req, res));
        get("/view/:imageid", (req, res) -> viewLargeImage(req, res));
        // post routes
        post("/admin/addUser", (req, res) -> addUser(req, res));
        post("/admin/editUser", (req, res) -> editUser(req, res));
        post("/admin/deleteUser", (req, res) -> deleteUser(req, res));
        post("/deleteImage/:imageid", (req, res) -> deleteImage(req, res));
        post("/login", (req, res) -> loginUser(req, res));
        post("/uploadImage", (req, res) -> uploadImage(req, res));
        //checks
        before("/admin/*", (req, res) -> checkAdmin(req, res));
        before("/upload", (req, res) -> checkUser(req, res));
        before("/view", (req, res) -> checkUser(req, res));
        before("/", (req, res) -> checkUser(req, res));


    }

    private String deleteImage(Request req, Response res) throws Exception {
        Map<String, Object> model = new HashMap<>();
        String imageId = req.params(":imageid");
        Postgres postgres = new Postgres();
        postgres.deleteImage(imageId);
        S3 s3 = new S3();
        s3.connect();
        s3.deleteObject(bucketName, imageId);
        res.redirect("/view");
        return "";
    }

    private String viewLargeImage(Request req, Response res) throws Exception {
        Map<String, Object> model = new HashMap<>();
        S3 s3 = new S3();
        s3.connect();
        String imageid = req.params(":imageid");
        String image = s3.getObject(bucketName, imageid);
        model.put("image", image);
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, "viewLargeImage.hbs"));
    }
    private boolean isUser(String username) throws SQLException{
        Postgres postgres = new Postgres();
        for (User user : postgres.getAllUsers()) {
            if (username.equals(user.getUsername())) {
                return true;
            }
        }

        return false;
    }
    private void checkUser(Request req, Response res) throws SQLException {
        if (req.session().attribute("username") == null) {
            res.redirect("/login");
            halt();
        }
        if (!isUser(req.session().attribute("username"))) {
            res.redirect("/login");
            halt();
        }
    }
    private void checkAdmin(Request req, Response res) {
        if(!isAdmin(req.session().attribute("username"))) {
            res.redirect("/");
            halt();
        }
    }
    private boolean isAdmin(String username) {
        return username != null && username.equals("kevin");
    }

    private String uploadImage(Request req, Response res) throws Exception {
        req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
        try (InputStream is = req.raw().getPart("uploaded_file").getInputStream()) { 
            byte[] contents = IOUtils.toByteArray(is);
            String imageToBinary = Base64.getEncoder().encodeToString(contents);
            S3 s3 = new S3();
            s3.connect();
            Image image = new Image(req.session().attribute("username"));
            s3.putObject(bucketName, image.getImageId(), imageToBinary);   
            Postgres postgres = new Postgres();
            postgres.uploadImage(image);
        }

        res.redirect("/upload");
        return "";
    }
    private String uploadPage(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();

        return new HandlebarsTemplateEngine().render(new ModelAndView(model, "upload.hbs"));
    }

    private String viewPage(Request req, Response res) throws Exception {
        Map<String, Object> model = new HashMap<>();
        S3 s3 = new S3();
        s3.connect();
        Postgres postgres = new Postgres();
        ArrayList<Image> images = postgres.viewAllImages(req.session().attribute("username"));


        for (Image image : images) {
            image.setImageString(s3.getObject(bucketName, image.getImageId()));
            System.out.println(image.getUsername());
        }
        model.put("images", images);
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, "view.hbs"));
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
        res.redirect("/admin/users");
        return "";
    }

    private String editUser(Request req, Response res) throws SQLException {
        db.editUser(req.queryParams("username"), req.queryParams("password"), req.queryParams("full_name"));
        res.redirect("/admin/users");

        return "";
    }

    private String deleteUser(Request req, Response res) throws SQLException {
        db.deleteUser(req.queryParams("username"));
        res.redirect("/admin/users");
        return "";
    }

    private String loginUser(Request req, Response res) throws SQLException {
        String username = req.queryParams("username");
        User user = db.getUser(username);
        if (user == null || !user.getPassword().equals(req.queryParams("password"))) {
            res.redirect("/login");

        } else {
            req.session().attribute("username", username);
            res.redirect("/");
        }

        return "";
    }

    private String debugSession(Request req, Response res) {
        StringBuffer sb = new StringBuffer();
        for (String key : req.session().attributes()) {
            sb.append(key + "->" + req.session().attribute(key) + "<br>");
        }
        return sb.toString();
    };
}

