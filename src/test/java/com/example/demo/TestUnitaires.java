package com.example.demo;

import com.example.demo.model.Utilisateur;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestUnitaires {

    @Test
    public void definirCodeUtilisateur_codeDoitEtreEnMajuscule(){

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setCode("azerty");

        Assertions.assertEquals("AZERTY", utilisateur.getCode());

    }

}
