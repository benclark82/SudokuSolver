package com.sudoku.sudokusolver.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web
        .configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web
        .configuration.WebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation
        .authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web
        .builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //Injects userDetailsService with the other beans it depends on
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //Require that /design and /orders be only accessed for ROLE_USER and everything
                //else can be accessed by anyone
                .authorizeRequests()
                .antMatchers("/play", "/dashboard")
                .access("hasRole('ROLE_USER')")
                .antMatchers("/**").access("permitAll")

                //Designates /login to be the custom login page
                .and()
                .formLogin()
                .loginPage("/login")

                //After logout it will take the user back home
                .and()
                .logout()
                .logoutSuccessUrl("/")


                .and()
                .csrf()
                .ignoringAntMatchers("/h2-console/**")

                // Allow pages to be loaded in frames from the same origin; needed for H2-Console
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()
        ;
    }


    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {

        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(encoder());

    }

}
