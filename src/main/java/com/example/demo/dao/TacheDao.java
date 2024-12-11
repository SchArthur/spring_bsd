package com.example.demo.dao;

import com.example.demo.model.Tache;
import com.example.demo.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TacheDao extends JpaRepository<Tache, Integer> {

    @Query("FROM Tache t JOIN t.utilisateurs u WHERE :idUtilisateur = u.id")
    List<Tache> tacheAffecte(@Param("idUtilisateur") int idUtilisateur);

    @Query("FROM Tache t JOIN t.priorite p WHERE t.valide = false ORDER BY p.id DESC")
    List<Tache> tacheNonValide();
    
}
