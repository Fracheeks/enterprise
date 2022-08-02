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

    public List<user> getAllCompanyOwner() {
        return repo.findAll().stream().filter(e->e.roleName.equals("companyOwner")).toList();
    }

    public user getUser(Long id) {
        return repo
                .findById(Math.toIntExact(id))
                .orElse(null);
    }

    public void  deleteUser(Long employeeId) {
        repo.deleteById(Math.toIntExact(employeeId));
    }

    public List<user> getAllEmployees() {
        return repo.findAll().stream().filter(e->e.roleName.equals("employee")).toList();
    }
    public user addEmployee(Employee newEmployee){
        Long id = repo.save(newEmployee).getId();
        return getUser(id);
    }



}
