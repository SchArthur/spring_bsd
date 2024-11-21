package com.example.demo.controller;

import com.example.demo.dao.StatusDao;
import com.example.demo.model.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class StatusController {

    @Autowired
    private StatusDao statusDao;

    @GetMapping("/status")
    public List<Status> getAll() {

        return statusDao.findAll();

    }

    @GetMapping("/status/{id}")
    public ResponseEntity<Status> get(@PathVariable Integer id) {

        //On vérifie que l'status existe bien dans la base de donnée
        Optional<Status> optionalStatus = statusDao.findById(id);

        //si l'status n'existe pas
        if(optionalStatus.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

       return new ResponseEntity<>(optionalStatus.get(),HttpStatus.OK);
    }

    @PostMapping("/status")
    public ResponseEntity<Status> create(@RequestBody Status status) {

        //on force l'id à null au cas où le client en aurait fourni un
        status.setId(null);
        statusDao.save(status);

        return new ResponseEntity<>(status, HttpStatus.CREATED);
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<Status> update(@RequestBody Status status, @PathVariable Integer id) {

        //on force le changement de l'id de l'status à enregitrer à l'id passé en paramètre
        status.setId(id);

        //On vérifie que l'status existe bien dans la base de donnée
        Optional<Status> optionalStatus = statusDao.findById(id);

        //si l'status n'existe pas
        if(optionalStatus.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        statusDao.save(status);

        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @DeleteMapping("/status/{id}")
    public ResponseEntity<Status> delete(@PathVariable Integer id) {

        //On vérifie que l'status existe bien dans la base de donnée
        Optional<Status> optionalStatus = statusDao.findById(id);

        //si l'status n'existe pas
        if(optionalStatus.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        statusDao.deleteById(id);

        return new ResponseEntity<>(optionalStatus.get(), HttpStatus.OK);

    }

}
