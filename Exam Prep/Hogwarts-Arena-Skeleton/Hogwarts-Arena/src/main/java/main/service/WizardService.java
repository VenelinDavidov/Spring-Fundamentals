package main.service;



import jakarta.validation.Valid;
import main.model.Spell;
import main.model.Wizard;
import main.model.WizardAlignment;
import main.repository.WizardRepository;
import main.web.dto.LoginRequest;
import main.web.dto.ProfileUpdateDto;
import main.web.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.UUID;


@Service
public class WizardService {

    private final PasswordEncoder passwordEncoder;
    private final WizardRepository wizardRepository;
    private final SpellService spellService;


    @Autowired
    public WizardService(PasswordEncoder passwordEncoder, WizardRepository wizardRepository, SpellService spellService) {
        this.passwordEncoder = passwordEncoder;
        this.wizardRepository = wizardRepository;
        this.spellService = spellService;
    }




    public void register(RegisterRequest registerRequest) {

        Wizard wizard = Wizard.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .avatarUrl(registerRequest.getAvatarUrl())
                .house(registerRequest.getHouse())
                .alignment(registerRequest.getAlignment())
                .createdOn(LocalDateTime.now())
                .build();

        wizardRepository.save(wizard);

        Spell randomSpellWithMinLearned = spellService.findRandomSpellWithMinLearned (0);

        learnSpell(wizard, randomSpellWithMinLearned);

    }



    private void learnSpell(Wizard wizard, Spell spell) {

        if (wizard.getSpells() == null) {
            wizard.setSpells(new ArrayList <> ());
        }

        spell.setWizard (wizard);

        wizard.getSpells ().add (spell);

        wizardRepository.save (wizard);
    }




    public Wizard findById(UUID wizardId) {
        return wizardRepository.findById(wizardId)
                .orElseThrow(() -> new RuntimeException("Wizard not found"));
    }


    public Wizard findByUsername(String username) {
        return wizardRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Wizard not found"));
    }




    public Wizard loginPlayer(LoginRequest loginRequest) {

        Wizard wizard = wizardRepository.findByUsername (loginRequest.getUsername ())
                .orElseThrow (() -> new RuntimeException ("Wizard not found"));

        if (!passwordEncoder.matches (loginRequest.getPassword (), wizard.getPassword ())) {
            throw new RuntimeException ("Invalid password");
        }

        return wizard;
    }



    public void updateWizard(UUID wizardId,  ProfileUpdateDto editRequest) {

        Wizard wizard = findById (wizardId);

        wizard.setUsername (editRequest.getUsername ());
        wizard.setAvatarUrl (editRequest.getAvatarUrl ());
        wizard.setUpdatedOn (LocalDateTime.now ());

        wizardRepository.save (wizard);
    }



    public void changeAlignment(UUID wizardId) {

        Wizard wizard = findById (wizardId);

        if (wizard.getAlignment () == WizardAlignment.LIGHT){
            wizard.setAlignment (WizardAlignment.DARK);
            wizard.setUpdatedOn (LocalDateTime.now ());
            wizardRepository.save (wizard);
        }
    }
}
