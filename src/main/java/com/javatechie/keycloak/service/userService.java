package com.javatechie.keycloak.service;

import com.javatechie.keycloak.entity.*;
import com.javatechie.keycloak.repository.userRepository;
import com.javatechie.keycloak.entity.user;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class userService {

    @Autowired
    private userRepository repo;

    @PostConstruct
    public void initializeTable() {
        repo.saveAll(
                Stream.of(
                        new companyOwner("fred"),
                        new companyOwner("steve"),
                        new Employee("john", 20000),
                        new Employee("mak", 55000),
                        new Employee("peter", 120000)
                ).collect(Collectors.toList()));
    }

    public user getUserbyId(int id) {
        return repo
                .findById(id)
                .orElse(null);
    }

    public List<user> getAllCompanyOwner() {
        return repo.findAll().stream().filter(e->e.roleName.equals("companyOwner")).toList();
    }

    public user getEmployee(int employeeId) {
        return repo
                .findById(employeeId)
                .orElse(null);
    }

    public void  deleteEmployee(Long employeeId) {
        repo.deleteById(Math.toIntExact(employeeId));
    }

    public List<user> getAllEmployees() {
        return repo.findAll().stream().filter(e->e.roleName.equals("employee")).toList();
    }
    public user addEmployee(Employee newEmployee){
        int id = repo.save(newEmployee).getId();
        return getEmployee(id);
    }



}
