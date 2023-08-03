package com.codefellowship.codefellowship.repos;

import com.codefellowship.codefellowship.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    public AppUser findByUsername(String username);
}


