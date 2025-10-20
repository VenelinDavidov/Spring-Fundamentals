package main.service;

import main.exception.DomainException;
import main.exception.SpellNotAvailableException;
import main.model.*;
import main.property.SpellsProperties;
import main.repository.SpellRepository;
import main.repository.WizardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;



@Service
public class SpellService {

    private final SpellRepository spellRepository;
    private final SpellsProperties spellsProperties;
    private final WizardRepository wizardRepository;


    @Autowired
    public SpellService(SpellRepository spellRepository,
                        SpellsProperties spellsProperties,
                        WizardRepository wizardRepository) {
        this.spellRepository = spellRepository;
        this.spellsProperties = spellsProperties;
        this.wizardRepository = wizardRepository;
    }



    // Learn Spell
    public Spell findRandomSpellWithMinLearned(int minLearned) {

        List <SpellsProperties.SpellYmlData> spellYmlData = spellsProperties
                .getSpells ()
                .stream ()
                .filter (s -> s.getMinLearned () == minLearned)
                .toList ();

        if (spellYmlData.isEmpty ()) return null;

        SpellsProperties.SpellYmlData randomSpellYmlData = spellYmlData.get (new Random ().nextInt (spellYmlData.size ()));

        return Spell.builder ()
                .code (randomSpellYmlData.getCode ())
                .name (randomSpellYmlData.getName ())
                .category (SpellCategory.valueOf (randomSpellYmlData.getCategory ()))
                .image (randomSpellYmlData.getImage ())
                .alignment (SpellAlignment.valueOf (randomSpellYmlData.getAlignment ()))
                .power (randomSpellYmlData.getPower ())
                .description (randomSpellYmlData.getDescription ())
                .createdOn (LocalDateTime.now ())
                .build ();


    }


    public List<Spell> getLearnedSpells(UUID wizardId) {
        return spellRepository.findSpellByWizardId (wizardId);
    }





  // Unlearned Spells
    public List<SpellsProperties.SpellYmlData> getUnlearnedSpells(Wizard wizard) {

        List <String> learnedCodes = wizard.getSpells ()
                                            .stream ()
                                            .map (Spell::getCode)
                                            .toList (                            );

                              return  spellsProperties.getAllSpells ()
                                             .stream ()
                                             .filter (spell -> !learnedCodes.contains (spell.getCode ()))
                                             .toList ();
    }





    // Available Spells
    public List <SpellsProperties.SpellYmlData> getAvailableSpells(Wizard wizard) {

        int learnedCount = wizard.getSpells ().size ();

       return getUnlearnedSpells (wizard)
                .stream ()
                .filter (s -> s.getMinLearned () <= learnedCount)
                .toList ();

    }




  // Locked Spells
    public List <SpellsProperties.SpellYmlData> getLockedSpell(Wizard wizard) {

        int learnedCount = wizard.getSpells ().size ();

        return getUnlearnedSpells (wizard)
                .stream ()
                .filter (s -> s.getMinLearned () > learnedCount)
                .toList ();
    }



 // Learn Spell
    public void learnSpell(UUID wizardId, String spellCode) {

        Wizard wizard = wizardRepository.findById (wizardId).orElseThrow (() -> new DomainException ("Wizard not found"));

        SpellsProperties.SpellYmlData newSpell =
                 spellsProperties.getSpells ()
                .stream ()
                .filter (s -> s.getCode ().equals (spellCode))
                .findFirst ()
                .orElseThrow (() -> new SpellNotAvailableException ("Spell not available"));

        //Do not allow learning the same spell twice, already learned spells should ideally not appear in this section anymore.
        boolean alreadyLearned = wizard.getSpells ().stream().anyMatch (s -> s.getCode ().equals (spellCode));

        if (alreadyLearned) {
            throw new SpellNotAvailableException ("Spell already learned");
        }

        //A wizard cannot learn a spell if the minLearned requirement is not satisfied!
        if(wizard.getSpells ().size () < newSpell.getMinLearned ()){
           throw new SpellNotAvailableException ("Not enough spells learned");
        }

        //A new learned Spell must be created for the wizard based on the selected spell from spells.yaml.
        Spell spell = Spell.builder ()
                .code (newSpell.getCode ())
                .name (newSpell.getName ())
                .category (SpellCategory.valueOf (newSpell.getCategory ()))
                .image (newSpell.getImage ())
                .alignment (SpellAlignment.valueOf (newSpell.getAlignment ()))
                .power (newSpell.getPower ())
                .description (newSpell.getDescription ())
                .createdOn (LocalDateTime.now ())
                .wizard (wizard)
                .build ();


        spellRepository.save (spell);

        // Every wizard can learn any spell, regardless of the spell/wizard alignment, but if a Light wizard learns a Dark spell, the wizard’s alignment instantly changes to Dark,
        if (wizard.getAlignment () == WizardAlignment.LIGHT &&  spell.getAlignment () == SpellAlignment.DARK ){
             wizard.setAlignment (WizardAlignment.DARK);
        }

        wizard.getSpells ().add (spell);
        wizardRepository.save (wizard);
    }
}

