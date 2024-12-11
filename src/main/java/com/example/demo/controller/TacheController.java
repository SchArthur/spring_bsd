package com.example.demo.controller;

import com.example.demo.dao.TacheDao;
import com.example.demo.dao.TacheDao;
import com.example.demo.model.Droit;
import com.example.demo.model.Tache;
import com.example.demo.model.Tache;
import com.example.demo.security.AppUserDetails;
import com.example.demo.security.IsAdmin;
import com.example.demo.security.IsEmploye;
import com.example.demo.security.IsRedacteur;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class TacheController {

    @Autowired
    private TacheDao tacheDao;


    @GetMapping("/tache/non-valide")
    public List<Tache> tacheNonValide() {

        return tacheDao.tacheNonValide();

    }
    

    @IsEmploye
    @GetMapping("/tache")
    public List<Tache> getAll() {

        return tacheDao.findAll();

    }

    @IsEmploye
    @GetMapping("/tache/{id}")
    public ResponseEntity<Tache> get(@PathVariable Integer id) {

        //On vérifie que l'tache existe bien dans la base de donnée
        Optional<Tache> optionalTache = tacheDao.findById(id);

        //si l'tache n'existe pas
        if(optionalTache.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

       return new ResponseEntity<>(optionalTache.get(),HttpStatus.OK);
    }

    @IsRedacteur
    @PostMapping("/tache")
    public ResponseEntity<Tache> create(
            @RequestBody @Valid Tache tache) {

        //on force l'id à null au cas où le client en aurait fourni un
        tache.setId(null);

        tacheDao.save(tache);

        return new ResponseEntity<>(tache, HttpStatus.CREATED);
    }

    @IsRedacteur
    @PutMapping("/tache/{id}")
    public ResponseEntity<Tache> update(
            @RequestBody @Valid Tache tacheEnvoye,
            @PathVariable Integer id,
            @AuthenticationPrincipal AppUserDetails appUserDetails) {

        //on force le changement de l'id de l'tache à enregitrer à l'id passé en paramètre
        tacheEnvoye.setId(id);

        //On vérifie que l'tache existe bien dans la base de donnée
        Optional<Tache> optionalTache = tacheDao.findById(id);

        //si l'tache n'existe pas
        if(optionalTache.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        //if(optionalTache.get().getCreateur().getId().equals(appUserDetails.getUtilisateur().getId()))
        if(!appUserDetails.getUtilisateur().getDroit().getNom().equals("administrateur") &&
                !optionalTache.get().getCreateur().getPseudo().equals(appUserDetails.getUsername())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        tacheDao.save(tacheEnvoye);

        return new ResponseEntity<>(optionalTache.get(), HttpStatus.OK);
    }

    @IsRedacteur
    @DeleteMapping("/tache/{id}")
    public ResponseEntity<Tache> delete(@PathVariable Integer id) {

        //On vérifie que l'tache existe bien dans la base de donnée
        Optional<Tache> optionalTache = tacheDao.findById(id);

        //si l'tache n'existe pas
        if(optionalTache.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        tacheDao.deleteById(id);

        return new ResponseEntity<>(optionalTache.get(), HttpStatus.OK);

    }
    
}
