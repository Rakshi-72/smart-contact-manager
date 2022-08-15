package com.smart_conatct_managet.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


/**
 * User class is a POJO class which is used to store the user information in the database
 */
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotEmpty(message = "name shoud not be empty")
    @Size(min = 4, max = 20, message = "name shoud be within 4-20 characters")
    private String name;

    @Column(unique = true)
    @Size(min = 8, max = 30, message = "email range should be within 8-25 characters")
    @NotNull(message = "email shoud not be empty")
    private String email;

    @NotNull(message = "password shoud not be null")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "password must contain 1 capital, 1 small, 1 numeric and one special character")
    private String password;


    private String role;
    private boolean enabled;
    private String imageUrl;
    @Column(length = 200)
    private String about;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    List<Contact> contacts = new ArrayList<>();

    public User() {
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    // @Override
    // public String toString() {
    //     return "User [about=" + about + ", contacts=" + contacts + ", email=" + email + ", enabled=" + enabled + ", id="
    //             + id + ", imageUrl=" + imageUrl + ", name=" + name + ", password=" + password + ", role=" + role + "]";
    // }

}
