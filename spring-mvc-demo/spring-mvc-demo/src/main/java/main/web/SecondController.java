package main.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalTime;

@Controller
@RequestMapping("/info-2")
public class SecondController {


    @GetMapping("/time-now")
    public ModelAndView getInfo (){

        ModelAndView modelAndView = new ModelAndView ();

        String timeNowText = "Time now is: " + LocalTime.now ();
        modelAndView.addObject ("message", timeNowText);

        // Избираме коя HTML страница да покажем на потребителя
        modelAndView.setViewName ("info");


        return modelAndView;
    }






    // 2 вариант

    @GetMapping("/time-now2")
    public String getInfo2(Model model) {

        String timeNowText = "Time now is: " + LocalTime.now();
        model.addAttribute("message", timeNowText);

        // Избираме коя HTML страница да покажем на потребителя
        return "info";
    }



}
