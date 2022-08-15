package com.smart_conatct_managet.controllers;

import java.security.Principal;
import java.util.List;

import com.smart_conatct_managet.entities.Contact;
import com.smart_conatct_managet.repository.ContactRepository;
import com.smart_conatct_managet.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {

    @Autowired
    private UserRepository repository;
    @Autowired
    private ContactRepository contactRepository;

    @GetMapping("/serach/{query}")
    public ResponseEntity<?> fetchCEntity(@PathVariable("query") String query, Principal principal) {

        List<Contact> contacts = contactRepository.findByNameContainingAndUser(query,
                repository.getUserByName(principal.getName()));

        return ResponseEntity.ok(contacts);
    }

}
