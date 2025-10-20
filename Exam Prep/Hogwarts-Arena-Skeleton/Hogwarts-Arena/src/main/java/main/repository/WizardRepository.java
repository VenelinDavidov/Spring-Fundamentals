package main.repository;


import main.model.Wizard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WizardRepository extends JpaRepository<Wizard, UUID> {



    boolean existsByUsername( String username);

    Optional <Wizard> findByUsername(String username);
}
