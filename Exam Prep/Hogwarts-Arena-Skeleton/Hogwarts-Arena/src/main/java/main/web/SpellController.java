package main.web;

import jakarta.servlet.http.HttpSession;
import main.service.SpellService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller

public class SpellController {

    private final SpellService spellService;



    @Autowired
    public SpellController(SpellService spellService) {
        this.spellService = spellService;

    }



    @PostMapping("/spells")
    public String learnSpell(@RequestParam ("spellCode") String spellCode, HttpSession session) {


        UUID wizardId = (UUID) session.getAttribute ("user_id");
        if (wizardId == null) return "redirect:/login";

        spellService.learnSpell (wizardId, spellCode);

        return "redirect:/home";
   }


}
