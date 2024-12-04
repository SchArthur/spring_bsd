package com.example.demo.model;

import com.example.demo.view.CompetenceView;
import com.example.demo.view.UtilisateurAvecCompetenceView;
import com.example.demo.view.UtilisateurView;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Primary;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({UtilisateurView.class, CompetenceView.class})
    Integer id;

    @Column(length = 100, unique = true)
    @NotBlank(message = "L'email ne peut pas être vide")
    @Pattern(
            regexp = "^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$",
            message = "L'email doit être valide et contenir une extension d'au moins 2 caractères."
    )
    @JsonView({UtilisateurView.class})
    String email;

    //@NotBlank(message = "Le mot de passe ne peut pas être vide")
    String password;

    Boolean administrateur;

    @ManyToOne
    @JsonView(CompetenceView.class)
    Status status;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "competence_utilisateur",
            joinColumns = @JoinColumn(name = "utilisateur_id"),
            inverseJoinColumns = @JoinColumn(name = "competence_id")
    )
    @JsonView({UtilisateurAvecCompetenceView.class})
    List<Competence> competences;
}
