package com.smart_conatct_managet.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * This class is a Spring Security configuration class that extends the
 * WebSecurityConfigurerAdapter
 * class.
 */
@Configuration
@EnableWebSecurity
public class CustomUserSecurity extends WebSecurityConfigurerAdapter {

    /**
     * This function returns a new instance of the CustomUserService class.
     * 
     * @return A new instance of CustomUserService.
     */
    @Bean
    public CustomUserService getCustomUserService() {
        return new CustomUserService();
    }

    /**
     * Used to encrypt the password.
     * 
     * @return A new instance of BCryptPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * This function returns a DaoAuthenticationProvider object that is configured
     * to use the
     * CustomUserService object and the PasswordEncoder object.
     * 
     * @return A DaoAuthenticationProvider object.
     */
    @Bean
    public DaoAuthenticationProvider getDaoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService(getCustomUserService());
        daoAuthenticationProvider.setPasswordEncoder(getPasswordEncoder());

        return daoAuthenticationProvider;
    }

    /**
     * "This function is called by the Spring Security framework to configure the
     * authentication
     * manager.
     * The authentication manager is responsible for authenticating users.
     * The authentication manager is configured to use the DAO authentication
     * provider."
     * 
     * @param auth AuthenticationManagerBuilder
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(getDaoAuthenticationProvider());
    }

    /**
     * If the user is trying to access the /admin/** page, they must have the role ADMIN. If the user
     * is trying to access the /user/** page, they must have the role USER. If the user is trying to
     * access any other page, they can do so without any restrictions.
     * 
     * @param http This is the main object that is used to configure the security of the application.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN").antMatchers("/user/**").hasRole("USER")
                .antMatchers("/**").permitAll().and().formLogin().loginPage("/signin").loginProcessingUrl("/login")
                .defaultSuccessUrl("/user/")
                .and().csrf().disable();

    }

}
