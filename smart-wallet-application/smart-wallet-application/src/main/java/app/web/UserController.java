package app.web;

import app.security.RequireAdminRole;
import app.user.model.User;
import app.user.service.UserService;
import app.web.mapper.DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/users")
public class UserController {


    private final UserService userService;




    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

      @RequireAdminRole
      @GetMapping
      public ModelAndView getAllUsers(){

        List <User> users = userService.getAllUsers ();

        ModelAndView modelAndView = new ModelAndView ();
        modelAndView.setViewName ("users");
        modelAndView.addObject ("users", users);

        return modelAndView;
      }



    // Edit profile user
    @GetMapping("/{id}/profile")
    public ModelAndView getProfileMenu (@PathVariable UUID id){

        User user = userService.getById (id);

        ModelAndView modelAndView = new ModelAndView ();
        modelAndView.addObject ("user", user);
        modelAndView.setViewName ("profile-menu");
        modelAndView.addObject ("userEditRequest", DtoMapper.mapUserToUserEditRequest(user));

        return modelAndView;
    }



    @PutMapping("/{id}/status") // PUT -> /users/ {id} /status
    public String updateUserStatus (@PathVariable UUID id){

        userService.switchStatus(id);

        return "redirect:/users";
    }


    @PutMapping("/{id}/role") // PUT -> /users/ {id} /role
    public String updateUserRole (@PathVariable UUID id){

        userService.switchRole (id);

        return "redirect:/users";
    }
}