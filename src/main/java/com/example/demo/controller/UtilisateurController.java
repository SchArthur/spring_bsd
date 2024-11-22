package com.example.demo.controller;

import com.example.demo.dao.UtilisateurDao;
import com.example.demo.model.Utilisateur;
import com.example.demo.security.AppUserDetails;
import com.example.demo.security.IsAdmin;
import com.example.demo.security.IsUser;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class UtilisateurController {

    @Autowired
    private UtilisateurDao utilisateurDao;

    @IsUser
    @GetMapping("/utilisateur")
    public List<Utilisateur> getAll() {



        return utilisateurDao.findAll();

    }

    @IsUser
    @GetMapping("/utilisateur/{id}")
    public ResponseEntity<Utilisateur> get(@PathVariable Integer id) {

        //On vérifie que l'utilisateur existe bien dans la base de donnée
        Optional<Utilisateur> optionalUtilisateur = utilisateurDao.findById(id);

        //si l'utilisateur n'existe pas
        if(optionalUtilisateur.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

       return new ResponseEntity<>(optionalUtilisateur.get(),HttpStatus.OK);
    }

    @IsAdmin
    @PostMapping("/utilisateur")
    public ResponseEntity<Utilisateur> create(
            @RequestBody @Valid Utilisateur utilisateur) {

        //on force l'id à null au cas où le client en aurait fourni un
        utilisateur.setId(null);
        utilisateurDao.save(utilisateur);

        return new ResponseEntity<>(utilisateur, HttpStatus.CREATED);
    }

    @IsAdmin
    @PutMapping("/utilisateur/{id}")
    public ResponseEntity<Utilisateur> update(
            @RequestBody @Valid Utilisateur utilisateur, @PathVariable Integer id) {

        //on force le changement de l'id de l'utilisateur à enregitrer à l'id passé en paramètre
        utilisateur.setId(id);

        //On vérifie que l'utilisateur existe bien dans la base de donnée
        Optional<Utilisateur> optionalUtilisateur = utilisateurDao.findById(id);

        //si l'utilisateur n'existe pas
        if(optionalUtilisateur.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        utilisateurDao.save(utilisateur);

        return new ResponseEntity<>(utilisateur, HttpStatus.OK);
    }

    @IsAdmin
    @DeleteMapping("/utilisateur/{id}")
    public ResponseEntity<Utilisateur> delete(@PathVariable Integer id) {

        //On vérifie que l'utilisateur existe bien dans la base de donnée
        Optional<Utilisateur> optionalUtilisateur = utilisateurDao.findById(id);

        //si l'utilisateur n'existe pas
        if(optionalUtilisateur.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        utilisateurDao.deleteById(id);

        return new ResponseEntity<>(optionalUtilisateur.get(), HttpStatus.OK);

    }

    @IsUser
    @GetMapping("/profil")
    public ResponseEntity<Utilisateur> profil(@AuthenticationPrincipal AppUserDetails userDetails) {

        Utilisateur utilisateur = userDetails.getUtilisateur();
        utilisateur.setPassword(null);

        return ResponseEntity.ok(userDetails.getUtilisateur());

    }

}
