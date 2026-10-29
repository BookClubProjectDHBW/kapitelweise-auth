package com.kapitelweise.auth.kapitelweiseAuth.service;

import java.util.Collections;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kapitelweise.auth.kapitelweiseAuth.dao.UserDao;
import com.kapitelweise.auth.kapitelweiseAuth.domain.AppUser;


//@Service ist die Businesslogikschicht, liegt zw. Controller und DAO
//er wird aufgerufen durch SecurityConfig, auf den geschützen Pfaden (aktuell bspw. /hey als Dummy)
//oder scheinbar auch vom authenticationManager im Controller

@Service
public class UserService implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    private AppUser getUserFromUsername(String username) {
        Optional<AppUser> found = userDao.findByUsername(username);
        if (found.isEmpty()) {
            log.warn(username + " will sich einloggen, kennen wir aber gar nicht");
            throw new UsernameNotFoundException("User not found");
        }
        return found.get();
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = getUserFromUsername(username);

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(Collections.emptyList())
                .build();
    }
}