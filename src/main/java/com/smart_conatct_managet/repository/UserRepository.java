package com.smart_conatct_managet.repository;


import com.smart_conatct_managet.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Get the user by name
     * 
     * @param email The email of the user
     * @return A User object
     */
    @Query("select u from User u where u.email = ?1")
    User getUserByName(String email);

}
