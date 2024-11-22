package com.example.demo.controller;

import com.example.demo.dao.UtilisateurDao;
import com.example.demo.model.Status;
import com.example.demo.model.Utilisateur;
import com.example.demo.security.AppUserDetails;
import com.example.demo.security.JwtUtils;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin
public class ConnexionController {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    BCryptPasswordEncoder encoder;

    @Autowired
    UtilisateurDao utilisateurDao;

    @Autowired
    AuthenticationProvider authenticationProvider;

    @PostMapping("/inscription")
    public ResponseEntity<Map<String,Object>> inscription(@RequestBody Utilisateur utilisateur) {

        utilisateur.setPassword(encoder.encode(utilisateur.getPassword()));
        utilisateur.setAdministrateur(false);
        Status disponible = new Status();
        disponible.setId(1);
        utilisateur.setStatus(disponible);

        utilisateurDao.save(utilisateur);

        return ResponseEntity.ok(Map.of("message","Enregistrement effectué"));
    }

    @PostMapping("/connexion")
    public ResponseEntity<String> connexion(@RequestBody Utilisateur utilisateur) {

        try {
            AppUserDetails appUserDetails = (AppUserDetails) authenticationProvider
                    .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                utilisateur.getEmail(),
                                utilisateur.getPassword()))
                    .getPrincipal();

            return ResponseEntity.ok(jwtUtils.generationToken(appUserDetails.getUsername()));

        } catch (AuthenticationException ex) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/test-jwt")
    public String testJwt() {
        return jwtUtils.generationToken("a@a.com");
    }

}
