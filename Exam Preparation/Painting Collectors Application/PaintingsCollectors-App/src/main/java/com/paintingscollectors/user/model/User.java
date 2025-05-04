package com.paintingscollectors.user.model;

import com.paintingscollectors.painting.model.FavouritePainting;
import com.paintingscollectors.painting.model.Painting;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String  password;

    @Column(unique = true, nullable = false)
    private String email;

    @OneToMany(mappedBy = "owner",fetch = FetchType.EAGER)
    private List <Painting> paintings;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private List<FavouritePainting> favouritePaintings;
}
