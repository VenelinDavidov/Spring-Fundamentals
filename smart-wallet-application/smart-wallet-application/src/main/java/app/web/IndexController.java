package app.web;

import app.user.model.Country;
import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.LoginRequest;
import app.web.dto.RegisterRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
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
    @GetMapping("/login")
    public ModelAndView getLoginPage() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        modelAndView.addObject("loginRequest", new LoginRequest());

        return modelAndView;
    }


    // HttpSession -> създава нова сесия за тази заявка
    @PostMapping("/login")
    public String login(@Valid LoginRequest loginRequest, BindingResult bindingResult, HttpSession session) {

        if (bindingResult.hasErrors()) {
            return "login";
        }
        User loginInUser = userService.login (loginRequest);
        session.setAttribute ("user_id", loginInUser.getId ());

        return "redirect:/home";
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
    public ModelAndView registerNewUser (@Valid RegisterRequest registerRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ModelAndView("register");
        }
        userService.register(registerRequest);

        return new ModelAndView("redirect:/home");
    }





    //Home
    @GetMapping("/home")
    public ModelAndView getHomePage(HttpSession session) {

        UUID userId = (UUID) session.getAttribute ("user_id");
        User user = userService.getById (userId);

        ModelAndView modelAndView = new ModelAndView ();
        modelAndView.addObject ("user", user);
        modelAndView.setViewName ("home");


        return modelAndView;
    }


    @GetMapping("/logout")
    public  String getLogout (HttpSession session){

        session.invalidate ();

        return "redirect:/";
    }
}