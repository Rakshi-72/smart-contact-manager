package com.smart_conatct_managet.configuration;

import java.util.Collection;
import java.util.List;

import com.smart_conatct_managet.entities.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDeatils implements UserDetails {

    private User user;

    // A constructor.
    public CustomUserDeatils(User user) {
        this.user = user;
    }

    // A default constructor.
    public CustomUserDeatils() {
    }

    /**
     * It returns a list of authorities that the user has
     * 
     * @return A list of GrantedAuthority objects.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole());
        return List.of(authority);
    }

    /**
     * The function returns the password of the user
     * 
     * @return The password of the user.
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * "This function returns the email of the user."
     * </code>
     * 
     * @return The email of the user.
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /**
     * This function returns true if the account is not expired.
     * 
     * @return True
     */
    /**
     * This function returns true if the account is not expired.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * This function returns true if the account is not locked.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * This function returns true if the user's credentials are not expired.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * This function returns true if the account is not locked.
     */

    @Override
    public boolean isEnabled() {
        return true;
    }

}
