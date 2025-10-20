package main.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Spell {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column( nullable = false)
    private String code;

    @Column( nullable = false)
    private String name;

    @Column( columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    private Wizard wizard;


    @Enumerated(EnumType.STRING)
    private SpellCategory category;

    @Enumerated(EnumType.STRING)
    private SpellAlignment alignment;

    @Column(nullable = false)
    private String image;


    private int power;
    private LocalDateTime createdOn;



}
