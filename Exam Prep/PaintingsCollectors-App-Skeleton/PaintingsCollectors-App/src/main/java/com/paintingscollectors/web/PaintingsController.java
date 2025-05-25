package com.paintingscollectors.web;

import com.paintingscollectors.painting.service.PaintingService;
import com.paintingscollectors.user.model.User;
import com.paintingscollectors.user.service.UserService;
import com.paintingscollectors.web.dto.CreatePaintingRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/paintings")
public class PaintingsController {

    private final UserService userService;
    private final PaintingService paintingService;

    @Autowired
    public PaintingsController(UserService userService, PaintingService paintingService) {
        this.userService = userService;
        this.paintingService = paintingService;
    }

    @GetMapping("/new")
    public ModelAndView getNewPainingPage(HttpSession session) {

        UUID userId = (UUID) session.getAttribute("user_id");
        User user = userService.getById(userId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("new-painting");
        modelAndView.addObject("user", user);
        modelAndView.addObject("createPaintingRequest", new CreatePaintingRequest());

        return modelAndView;
    }

    @PostMapping
    public ModelAndView createNewPainting(@Valid CreatePaintingRequest createPaintingRequest, BindingResult bindingResult, HttpSession session) {

        UUID userId = (UUID) session.getAttribute("user_id");
        User user = userService.getById(userId);

        if (bindingResult.hasErrors()) {

            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("new-painting");
            modelAndView.addObject("user", user);
            return modelAndView;
        }


        paintingService.createNewPainting(createPaintingRequest, user);

        return new ModelAndView("redirect:/home");
    }



    @DeleteMapping("/{id}")
    public String deletePainting(@PathVariable UUID id) {

        paintingService.deletePaintingById(id);

        return "redirect:/home";
    }


    @PostMapping("/favourites/{id}")
    public String makeFavouritePainting (@PathVariable UUID id, HttpSession session){
        UUID userId = (UUID) session.getAttribute("user_id");
        User user = userService.getById(userId);

        paintingService.createFavouritePainting(id, user);

         return "redirect:/home";
    }

    @DeleteMapping("/favourites/{id}")
    public String deleteFavouritePainting (@PathVariable UUID id){

        paintingService.deleteFavouritePainting(id);

        return "redirect:/home";
    }




    @PutMapping("/favourites/{id}/votes")
    public String updateVotes (@PathVariable UUID id){

        paintingService.incrementsVotesByOne(id);

        return "redirect:/home";
    }



}
