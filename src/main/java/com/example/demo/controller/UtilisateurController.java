package com.example.demo.controller;

import com.example.demo.dao.UtilisateurDao;
import com.example.demo.model.Status;
import com.example.demo.model.Utilisateur;
import com.example.demo.security.AppUserDetails;
import com.example.demo.security.IsAdmin;
import com.example.demo.security.IsUser;
import com.example.demo.view.UtilisateurAvecCompetenceView;
import com.example.demo.view.UtilisateurView;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    @Autowired
    BCryptPasswordEncoder encoder;

    @IsUser
    @GetMapping("/utilisateur")
    @JsonView(UtilisateurView.class)
    public List<Utilisateur> getAll() {

        return utilisateurDao.findAll();

    }

    @IsUser
    @GetMapping("/utilisateur/{id}")
    @JsonView(UtilisateurAvecCompetenceView.class)
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

        utilisateur.setPassword(encoder.encode(utilisateur.getPassword()));
        utilisateur.setAdministrateur(false);
        Status disponible = new Status();
        disponible.setId(1);
        utilisateur.setStatus(disponible);

        utilisateurDao.save(utilisateur);

        return new ResponseEntity<>(utilisateur, HttpStatus.CREATED);
    }

    @IsAdmin
    @PutMapping("/utilisateur/{id}")
    public ResponseEntity<Utilisateur> update(
            @RequestBody @Valid Utilisateur utilisateurEnvoye, @PathVariable Integer id) {

        //on force le changement de l'id de l'utilisateur à enregitrer à l'id passé en paramètre
        utilisateurEnvoye.setId(id);

        //On vérifie que l'utilisateur existe bien dans la base de donnée
        Optional<Utilisateur> optionalUtilisateur = utilisateurDao.findById(id);

        //si l'utilisateur n'existe pas
        if(optionalUtilisateur.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Utilisateur utilisateurBaseDeDonne = optionalUtilisateur.get();

        //On recupère de la base de donnée toutes les informations
        // que l'on ne veut pas laisser l'utilisateur modifier
        utilisateurEnvoye.setStatus(utilisateurBaseDeDonne.getStatus());

        //Si l'utilisateur a un nouveau mot de passe, on le hash le nouveau
        if(utilisateurEnvoye.getPassword() != null) {
            utilisateurEnvoye.setPassword(encoder.encode(utilisateurEnvoye.getPassword()));
        }

        //si le json envoyé n'a pas de competences, on gardent les anciennes
        // (mais si le json contient un tableau vide pour les competences,
        // alors elles seront bien supprimées)
        if(utilisateurEnvoye.getCompetences() == null) {
            utilisateurEnvoye.setCompetences(utilisateurBaseDeDonne.getCompetences());
        }
        //idem pour status
        if(utilisateurEnvoye.getStatus() == null) {
            utilisateurEnvoye.setStatus(utilisateurBaseDeDonne.getStatus());
        }
        //idem pour administrateur
        if(utilisateurEnvoye.getAdministrateur() == null) {
            utilisateurEnvoye.setAdministrateur(utilisateurBaseDeDonne.getAdministrateur());
        }
        //Note : on évalue la présence de chaque propriété, mais dans le cas d'une application front
        //(ex : angular, vue, react, android, ios ...) cela ne serait pas nécessaire, puisque l'on
        //aurait récupéré au préalable l'objet entier (ca depend de l'usage de l'api, ici on a pris
        // le parti de laisser la possibilité de n'envoyer qu'une partie des information à mettre à jour)


        utilisateurDao.save(utilisateurEnvoye);

        return new ResponseEntity<>(optionalUtilisateur.get(), HttpStatus.OK);
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
