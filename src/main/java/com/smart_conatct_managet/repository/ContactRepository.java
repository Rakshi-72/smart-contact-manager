package com.smart_conatct_managet.repository;


import java.util.List;

import com.smart_conatct_managet.entities.Contact;
import com.smart_conatct_managet.entities.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// This is a Spring Data JPA repository. It is an interface that extends the JpaRepository interface.
// The JpaRepository interface provides methods for performing CRUD operations on a repository for a
// specific type. It has methods like save, delete, findOne, findAll, and count. The first type
// parameter is the entity type, Contact, and the second type parameter is the type of the entity's
// primary key, Integer.
public interface ContactRepository extends JpaRepository<Contact, Integer>  {


    /**
     * Find all contacts for a given user, and return them in a pageable format.
     * 
     * @param user The user to search for.
     * @param pageable This is the Pageable object that contains the page number, page size, and sort
     * order.
     * @return A Page of Contacts
     */
    Page<Contact> findContactByUser(User user, Pageable pageable);

   /**
    * Find all contacts whose name contains the given string and belong to the given user.
    * 
    * @param name The name of the contact you want to find.
    * @param user The user who owns the contacts.
    * @return A list of contacts that contain the name and user.
    */
    List<Contact> findByNameContainingAndUser(String name, User user);
    
}
