package com.javatechie.keycloak.repository;

import com.javatechie.keycloak.entity.companyOwner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface companyOwnerRepository extends JpaRepository<companyOwner,Integer> {
    companyOwner findByUsername(String username);
}
