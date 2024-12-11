package com.example.demo.model;

import com.example.demo.view.CompetenceView;
import com.example.demo.view.UtilisateurAvecCompetenceView;
import com.example.demo.view.UtilisateurView;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Primary;

import java.util.ArrayList;
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
    @NotBlank(message = "Le pseudo ne peut pas être vide")
    String pseudo;

    String password;

    String code;

    @ManyToOne
    Droit droit;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "tache_utilisateur",
            joinColumns = @JoinColumn(name = "utilisateur_id"),
            inverseJoinColumns = @JoinColumn(name = "tache_id")
    )
    List<Tache> tachesAffectees = new ArrayList<>();

    public void setCode(String code) {
        this.code = code.toUpperCase();
    }
}
