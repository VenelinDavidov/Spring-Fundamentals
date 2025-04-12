package main.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String profilePictureUrl;

    private String firstName;

    private String username;

    private String country;

    private Double balance;

    private String phoneNumber;

    private String address;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "owner", targetEntity = Transaction.class)
    private List<Transaction> transactions;
}
