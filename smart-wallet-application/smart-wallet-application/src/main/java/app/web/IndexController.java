package app.web;

import app.user.model.User;
import app.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
    public String getRegisterPage() {

        return "register";
    }

    //Home
    @GetMapping("/home")
    public ModelAndView getHomePage() {

        ModelAndView modelAndView = new ModelAndView ();

        User user = userService.getById (UUID.fromString ("9df09aa2-741c-4eba-b854-fca20e353e66"));
        modelAndView.addObject ("user", user);
        modelAndView.setViewName ("home");


        return modelAndView;
    }
}
