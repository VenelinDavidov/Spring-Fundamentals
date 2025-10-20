package main.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Wizard {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WizardAlignment alignment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private House house;

    @OneToMany(mappedBy = "wizard", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List <Spell> spells = new ArrayList <> ();

    private LocalDateTime createdOn;

    private LocalDateTime updatedOn;


    public void addSpell(Spell spell) {
        spells.add(spell);
        spell.setWizard(this);
    }
}
