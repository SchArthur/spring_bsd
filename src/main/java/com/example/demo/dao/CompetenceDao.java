package com.example.demo.dao;

import com.example.demo.dto.StatistiqueCompetenceDto;
import com.example.demo.model.Competence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CompetenceDao extends JpaRepository<Competence, Integer> {

    @Query("SELECT new com.example.demo.dto.StatistiqueCompetenceDto(c.nom, COUNT(u) ) " +
            "FROM Competence c " +
            "LEFT JOIN c.utilisateurs u " +
            "GROUP BY c.nom")
    List<StatistiqueCompetenceDto> nombrePersonneParCompetence();

}
