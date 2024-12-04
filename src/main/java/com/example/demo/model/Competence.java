package com.example.demo.model;

import com.example.demo.view.CompetenceView;
import com.example.demo.view.UtilisateurAvecCompetenceView;
import com.example.demo.view.UtilisateurView;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Competence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(CompetenceView.class)
    Integer id;

    @JsonView({UtilisateurAvecCompetenceView.class, CompetenceView.class})
    String nom;

    @ManyToMany(mappedBy = "competences")
    @JsonView(CompetenceView.class)
    List<Utilisateur> utilisateurs = new ArrayList<>();
}
