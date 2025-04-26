package app.web;

import app.user.model.User;
import app.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/subscriptions")
public class SubscriptionController {


    private final UserService userService;

    @Autowired
    public SubscriptionController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public String getUpgradePage() {

        return "upgrade";
    }



    @GetMapping("/history")
    public ModelAndView getUserSubscriptions (HttpSession session){

        UUID userId = (UUID) session.getAttribute ("user_id");
        User user = userService.getById (userId);

        ModelAndView modelAndView = new ModelAndView ();
        modelAndView.addObject ("user", user);
        modelAndView.setViewName ("subscription-history");

        return modelAndView;
    }

}