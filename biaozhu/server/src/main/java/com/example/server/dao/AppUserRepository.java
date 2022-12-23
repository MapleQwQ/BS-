package com.example.server.dao;

import com.example.server.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    AppUser findAppUserByUserName(String userName);

    Optional<AppUser> findByUserName(String userName);

    Optional<AppUser> findAppUserByEmail(String email);
}
