package com.example.demo.controller;

import com.example.demo.dao.UtilisateurDao;
import com.example.demo.model.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class UtilisateurController {

    @Autowired
    private UtilisateurDao utilisateurDao;

    @GetMapping("/utilisateur")
    public List<Utilisateur> getAll() {

        return utilisateurDao.findAll();

    }

    @GetMapping("/utilisateur/{id}")
    public Utilisateur get(@PathVariable Integer id) {

       Optional<Utilisateur> optionalUtilisateur = utilisateurDao.findById(id);

       if(optionalUtilisateur.isPresent()) {
           return optionalUtilisateur.get();
       }

       return null;
    }

    @PostMapping("/utilisateur")
    public boolean create(@RequestBody Utilisateur utilisateur) {

        utilisateurDao.save(utilisateur);

        return true;

    }

}
