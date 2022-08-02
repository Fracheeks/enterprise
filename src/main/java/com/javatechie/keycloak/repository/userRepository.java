package com.javatechie.keycloak.repository;

import com.javatechie.keycloak.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface userRepository extends JpaRepository<user,Integer> {
}
