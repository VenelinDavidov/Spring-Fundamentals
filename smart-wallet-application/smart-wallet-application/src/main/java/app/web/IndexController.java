package app.web;

import app.user.model.Country;
import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;


@Controller
public class IndexController {

    private final UserService userService;


    @Autowired
    public IndexController(UserService userService) {
        this.userService = userService;
    }


    // Когато не връщаме модел атрибури, ползваме String
    @GetMapping("/")
    public String getIndexPage() {

        return "index";
    }

    //Login
    @GetMapping("/login")
    public String getLoginPage() {

        return "login";
    }



    // Register
    @GetMapping("/register")
    public ModelAndView getRegisterPage(Model model) {

        ModelAndView modelAndView = new ModelAndView ();
        modelAndView.addObject ("registerRequest", new RegisterRequest ());

        modelAndView.setViewName ("register");

        return modelAndView;
    }




    @PostMapping("/register")
    public ModelAndView registerNewUser (RegisterRequest registerRequest){


        return null;
    }




    //Home
    @GetMapping("/home")
    public ModelAndView getHomePage() {

        ModelAndView modelAndView = new ModelAndView ();

        User user = userService.getById (UUID.fromString ("559748e4-acaa-47ea-9456-6ec78e4a02bb"));
        modelAndView.addObject ("user", user);
        modelAndView.setViewName ("home");


        return modelAndView;
    }
}