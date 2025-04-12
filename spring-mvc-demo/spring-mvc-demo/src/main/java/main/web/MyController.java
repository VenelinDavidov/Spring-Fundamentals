package main.web;

// Handler Method is a method that handles HTTP requests

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

@RestController
@RequestMapping("/info")  // base path for all handler methods( all endpoints) HTTP Request
public class MyController {


    private Map <Integer, String> users = Map.of (
            1, "Ivan",
            2, "Joro",
            3, "Venko"
    );

    // HTTP GET /info/time-now
    // 1. Колко е часът сега?

    @GetMapping("/time-now")
    public String getTimeNow() {

        return "Time now is " + LocalTime.now ();
    }



    // HTTP GET /info/today
    // 2. Ден от седмицата?

    @GetMapping("/today")
    public String getDayOfWeek() {

        return "Today is " + LocalDateTime.now ().getDayOfWeek ().name ();
    }



    //HTTP GET /info/users/id
    //http://localhost:8080/info/users/3?firstName=Venko&age=30
    @GetMapping("/users/{id}")
    public String getUsernameById(@PathVariable int id, @RequestParam("firstName") String firstName, @RequestParam("age") int age) {

        return users.get(id);
    }
}
