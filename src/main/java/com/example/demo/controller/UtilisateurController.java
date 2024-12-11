package com.example.demo.controller;

import com.example.demo.dao.TacheDao;
import com.example.demo.dao.UtilisateurDao;
import com.example.demo.model.Droit;
import com.example.demo.model.Tache;
import com.example.demo.model.Utilisateur;
import com.example.demo.security.AppUserDetails;
import com.example.demo.security.IsAdmin;
import com.example.demo.security.IsEmploye;
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

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class UtilisateurController {

    @Autowired
    private UtilisateurDao utilisateurDao;

    @Autowired
    private TacheDao tacheDao;

    @Autowired
    BCryptPasswordEncoder encoder;

    @GetMapping("/utilisateur/tache/{id}")
    public ResponseEntity<List<Tache>> tacheUtilisateur(@PathVariable int id) {
        Optional<Utilisateur> optionalUtilisateur = utilisateurDao.findById(id);

        //si l'utilisateur n'existe pas
        if(optionalUtilisateur.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        //solution 1
//      return ResponseEntity.ok(optionalUtilisateur.get().getTachesAffectees());

        //solution 2
        return ResponseEntity.ok(tacheDao.tacheAffecte(id));
    }


    @IsEmploye
    @GetMapping("/utilisateur")
    @JsonView(UtilisateurView.class)
    public List<Utilisateur> getAll() {

        return utilisateurDao.findAll();

    }

    @IsEmploye
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

        Droit droitEmploye = new Droit();
        droitEmploye.setId(1);
        utilisateur.setDroit(droitEmploye);

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

        //Si l'utilisateur a un nouveau mot de passe, on le hash le nouveau
        if(utilisateurEnvoye.getPassword() != null) {
            utilisateurEnvoye.setPassword(encoder.encode(utilisateurEnvoye.getPassword()));
        }

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

    @IsEmploye
    @GetMapping("/profil")
    public ResponseEntity<Utilisateur> profil(@AuthenticationPrincipal AppUserDetails userDetails) {

        Utilisateur utilisateur = userDetails.getUtilisateur();
        utilisateur.setPassword(null);

        return ResponseEntity.ok(userDetails.getUtilisateur());

    }

}
