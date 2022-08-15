package com.smart_conatct_managet.configuration;

import com.smart_conatct_managet.entities.User;
import com.smart_conatct_managet.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserService implements UserDetailsService {

   // Injecting the UserRepository object into the CustomUserService class.
    @Autowired
    private UserRepository repository;

   /**
    * The function takes a username as a parameter and returns a UserDetails object
    * 
    * @param username The username of the user to load.
    */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Getting the user from the database.
        User user = repository.getUserByName(username);

        // This is a check to see if the user exists in the database. If the user does not exist, then
        // an exception is thrown.
        if (user == null) {
            throw new UsernameNotFoundException("Could not found the user name");
        }
        
        CustomUserDeatils customUserDeatils = new CustomUserDeatils(user);
        return customUserDeatils;

    }

}
