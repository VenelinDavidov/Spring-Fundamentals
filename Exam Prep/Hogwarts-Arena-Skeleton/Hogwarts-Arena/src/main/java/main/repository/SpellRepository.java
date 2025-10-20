package main.repository;

import main.model.Spell;
import main.model.Wizard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface SpellRepository extends JpaRepository<Spell, UUID> {





    List<Spell> findSpellByWizardId(UUID wizardId);
}
