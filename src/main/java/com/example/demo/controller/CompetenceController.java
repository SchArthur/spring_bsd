package com.example.demo.controller;

import com.example.demo.dao.CompetenceDao;
import com.example.demo.model.Competence;
import com.example.demo.view.CompetenceView;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class CompetenceController {

    @Autowired
    private CompetenceDao competenceDao;

    @GetMapping("/competence")
    @JsonView(CompetenceView.class)
    public List<Competence> getAll() {

        return competenceDao.findAll();

    }

    @GetMapping("/competence/{id}")
    @JsonView(CompetenceView.class)
    public ResponseEntity<Competence> get(@PathVariable Integer id) {

        //On vérifie que l'competence existe bien dans la base de donnée
        Optional<Competence> optionalCompetence = competenceDao.findById(id);

        //si l'competence n'existe pas
        if(optionalCompetence.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

       return new ResponseEntity<>(optionalCompetence.get(),HttpStatus.OK);
    }

    @PostMapping("/competence")
    public ResponseEntity<Competence> create(@RequestBody Competence competence) {

        //on force l'id à null au cas où le client en aurait fourni un
        competence.setId(null);
        competenceDao.save(competence);

        return new ResponseEntity<>(competence, HttpStatus.CREATED);
    }

    @PutMapping("/competence/{id}")
    public ResponseEntity<Competence> update(@RequestBody Competence competence, @PathVariable Integer id) {

        //on force le changement de l'id de l'competence à enregitrer à l'id passé en paramètre
        competence.setId(id);

        //On vérifie que l'competence existe bien dans la base de donnée
        Optional<Competence> optionalCompetence = competenceDao.findById(id);

        //si l'competence n'existe pas
        if(optionalCompetence.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        competenceDao.save(competence);

        return new ResponseEntity<>(competence, HttpStatus.OK);
    }

    @DeleteMapping("/competence/{id}")
    public ResponseEntity<Competence> delete(@PathVariable Integer id) {

        //On vérifie que l'competence existe bien dans la base de donnée
        Optional<Competence> optionalCompetence = competenceDao.findById(id);

        //si l'competence n'existe pas
        if(optionalCompetence.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        competenceDao.deleteById(id);

        return new ResponseEntity<>(optionalCompetence.get(), HttpStatus.OK);

    }

}
