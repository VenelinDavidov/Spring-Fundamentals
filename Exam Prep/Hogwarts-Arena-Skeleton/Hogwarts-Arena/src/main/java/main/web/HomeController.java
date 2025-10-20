package main.web;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import main.model.Spell;
import main.model.Wizard;
import main.property.SpellsProperties;
import main.service.SpellService;
import main.service.WizardService;
import main.web.dto.ProfileUpdateDto;
import main.web.mapper.EditDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
public class HomeController {


    private final WizardService wizardService;
    private final SpellService spellService;

    @Autowired
    public HomeController(WizardService wizardService, SpellService spellService) {
        this.wizardService = wizardService;
        this.spellService = spellService;
    }




    @GetMapping("/home")
    public ModelAndView showHomePage(HttpSession session) {

        UUID wizardId = (UUID) session.getAttribute ("user_id");
        Wizard wizard = wizardService.findById(wizardId);

        if (wizard == null) {
            return new ModelAndView("redirect:/login");
        }

        ModelAndView modelAndView = new ModelAndView ("home");

        List <Spell> learnedSpells = spellService.getLearnedSpells (wizardId);
        List <SpellsProperties.SpellYmlData> availableSpells = spellService.getAvailableSpells (wizard);
        List <SpellsProperties.SpellYmlData> lockedSpell = spellService.getLockedSpell (wizard);

        modelAndView.addObject ("wizard", wizard);
        modelAndView.addObject ("learnedSpells", learnedSpells);
        modelAndView.addObject ("availableSpells", availableSpells);
        modelAndView.addObject ("lockedSpell", lockedSpell);

        return modelAndView;

    }



    @GetMapping("/profile")
    public ModelAndView showProfilePage( HttpSession session){

        UUID wizardId = (UUID) session.getAttribute ("user_id");
        Wizard wizard = wizardService.findById(wizardId);

        ModelAndView modelAndView = new ModelAndView ("profile");
        modelAndView.addObject ("wizard", wizard);
        modelAndView.addObject ("alignment", wizard.getAlignment ());
        modelAndView.addObject ("editRequest", EditDtoMapper.mapToProfileUpdateDto (wizard));

        return modelAndView;
    }


    @PutMapping("/profile")
    public ModelAndView editProfile(HttpSession session,
                                    @Valid @ModelAttribute("editRequest")
                                    ProfileUpdateDto editRequest,
                                    BindingResult bindingResult)
    {

        UUID wizardId = (UUID) session.getAttribute ("user_id");
        Wizard wizard = wizardService.findById(wizardId);


       if (bindingResult.hasErrors ()){
           ModelAndView modelAndView = new ModelAndView ();
           modelAndView.addObject ("editRequest", editRequest);
           modelAndView.addObject ("wizard", wizard);
           modelAndView.addObject ("alignment", wizard.getAlignment ());
           return modelAndView;
       }

       wizardService.updateWizard (wizardId, editRequest);


       return new ModelAndView ("redirect:/home");
    }


    @PatchMapping("/profile/alignment")
    public ModelAndView changeAlignment(HttpSession session){

        UUID wizardId = (UUID) session.getAttribute ("user_id");

        wizardService.changeAlignment (wizardId);

        return new ModelAndView ("redirect:/profile");
    }
}
