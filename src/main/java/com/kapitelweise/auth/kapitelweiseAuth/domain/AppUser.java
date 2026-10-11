package com.kapitelweise.auth.kapitelweiseAuth.domain;

import java.time.LocalDateTime;
import java.util.Objects;

// das ist unser Datenbank User Objekt, das aktuell username, password enthält
public class AppUser{

    private Long id;
    private String username;
    private String password;
    private LocalDateTime created_at;

    public AppUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public AppUser(Long id, String username, String password, LocalDateTime created_at){
        this.id = id;
        this.username=username;
        this.password = password;
        this.created_at=created_at;
    }

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public Long getId(){ return id;}
    public LocalDateTime getCreated_at(){return created_at;}
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppUser)) return false;
        AppUser other = (AppUser) o;
        return Objects.equals(username, other.username);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(username);
    }

    public void setUsername(String username) {
        this.username = username;
    }
}