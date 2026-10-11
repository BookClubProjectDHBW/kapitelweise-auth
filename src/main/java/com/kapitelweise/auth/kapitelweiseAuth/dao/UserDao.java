package com.kapitelweise.auth.kapitelweiseAuth.dao;

import com.kapitelweise.auth.kapitelweiseAuth.domain.AppUser;

import java.util.Optional;
//unser Interface für das UserDAO, ist ein Pattern das quasi eine Zwischenschicht zwischen Service und Persistence darstellt
// so könnten mehrere Services dieses Zeug nutzen, und wir könnten die Implementierung austauschen, ohne dass die Services was davon merken

public interface UserDao {
    boolean userExists(String username);
    AppUser saveUser(AppUser user);
    Optional<AppUser> findByUsername(String username);
}