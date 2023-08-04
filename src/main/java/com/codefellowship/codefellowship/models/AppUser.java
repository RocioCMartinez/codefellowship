package com.codefellowship.codefellowship.models;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class AppUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String username;

    String firstName;

    String lastName;

    String password;

    LocalDate dateOfBirth;

    String bio;

    @OneToMany(mappedBy = "appUser")
    List<Posts> posts;

    @ManyToMany
    @JoinTable(
            name = "followers_following",
            joinColumns = {@JoinColumn(name = "followingUser")},
            inverseJoinColumns = {@JoinColumn(name = "followee")}
    )
    Set<AppUser> usersIFollow = new HashSet<>();


    @ManyToMany(mappedBy = "usersIFollow")
    Set<AppUser> usersWhoFollowMe = new HashSet<>();


    public AppUser() {
    }

    public AppUser(String username, String firstName,  String lastName, LocalDate dateOfBirth, String bio) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.bio = bio;
    }

    public Set<AppUser> getUsersIFollow() {
        return usersIFollow;
    }

    public void setUsersIFollow(Set<AppUser> usersIFollow) {
        this.usersIFollow = usersIFollow;
    }

    public Set<AppUser> getUsersWhoFollowMe() {
        return usersWhoFollowMe;
    }

    public void setUsersWhoFollowMe(Set<AppUser> usersWhoFollowMe) {
        this.usersWhoFollowMe = usersWhoFollowMe;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Posts> getPosts() {
        return posts;
    }

    public void setPosts(List<Posts> posts) {
        this.posts = posts;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
