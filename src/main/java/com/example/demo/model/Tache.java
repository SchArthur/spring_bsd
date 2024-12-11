package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Tache {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String nom;

    String description;

    boolean valide;

    @ManyToOne
    Priorite priorite;

    @ManyToOne
    Utilisateur createur;

    @ManyToMany(mappedBy = "tachesAffectees")
    List<Utilisateur> utilisateurs;
}
