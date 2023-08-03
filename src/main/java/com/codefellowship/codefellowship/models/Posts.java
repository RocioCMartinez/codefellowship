package com.codefellowship.codefellowship.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Posts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String body;

    LocalDate createdAt;

    @ManyToOne
    AppUser appUser;

    public Posts() {
    }

    public Posts(String body, AppUser appUser) {
        this.body = body;
        this.appUser = appUser;
    }

    public Posts(String body, LocalDate createdAt, AppUser appUser) {
        this.body = body;
        this.createdAt = createdAt;
        this.appUser = appUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    @Override
    public String toString() {
        return "Posts{" +
                "id=" + id +
                ", body='" + body + '\'' +
                ", createdAt=" + createdAt +
                ", appUser=" + appUser +
                '}';
    }
}
