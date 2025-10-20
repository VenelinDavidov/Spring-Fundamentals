package main.web;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import main.model.Wizard;
import main.service.SpellService;
import main.service.WizardService;
import main.web.dto.LoginRequest;
import main.web.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
public class IndexController {

    private final WizardService wizardService;




    @Autowired
    public IndexController(WizardService wizardService) {
        this.wizardService = wizardService;
    }


    @GetMapping
    public String index() {
        return "index";
    }




    // register
    @GetMapping("/register")
    private ModelAndView showRegisterPage() {

        ModelAndView modelAndView = new ModelAndView ("register");
        modelAndView.addObject ("registerRequest", new RegisterRequest ());

        return modelAndView;
    }

    @PostMapping("/register")
    private String register(@Valid RegisterRequest registerRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors ()) {
            return "register";
        }

        wizardService.register (registerRequest);

        return "redirect:/login";

    }





    // login
    @GetMapping("/login")
    private ModelAndView getLoginPage() {

        ModelAndView modelAndView = new ModelAndView ("login");

        modelAndView.addObject ("loginRequest", new LoginRequest ());

        return modelAndView;
    }

    @PostMapping("/login")
    private String loginPlayer(LoginRequest loginRequest, HttpSession session) {

        Wizard wizard = wizardService.loginPlayer (loginRequest);
        session.setAttribute ("user_id", wizard.getId ());

        return "redirect:/home";
    }







    @GetMapping("/logout")
    private String logout (HttpSession session){

        session.invalidate();
        return "redirect:/";
    }

}
