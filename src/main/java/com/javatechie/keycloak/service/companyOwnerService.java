/*package com.javatechie.keycloak.service;

import com.javatechie.keycloak.entity.companyOwner;
import com.javatechie.keycloak.repository.companyOwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class companyOwnerService {
    @Autowired
    private companyOwnerRepository CompanyOwnerRepository;

    @PostConstruct
    public void initializeCompanyOwnerTable() {
        CompanyOwnerRepository.saveAll(
                Stream.of(
                        new companyOwner("fred", "s123456"),
                        new companyOwner("steve", "s345678")
                ).collect(Collectors.toList()));
    }


    public companyOwner getCompanyOwnerbyId(int employeeId) {
        return CompanyOwnerRepository
                .findById(employeeId)
                .orElse(null);
    }
    public companyOwner getCompanyOwnerByUsername(String username) {
        return CompanyOwnerRepository
                .findByUsername(username);
    }

    public List<companyOwner> getAllCompanyOwner() {
        return CompanyOwnerRepository.findAll();
    }
}*/