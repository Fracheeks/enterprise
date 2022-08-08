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

    public List<user> getAllCompanyOwner() {
        return repo.findAll().stream().filter(e->e.roleName.equals("companyOwner")).toList();
    }

    public user getUser(int id) {
        return repo
                .findById(id)
                .orElse(null);
    }

    public void  deleteUser(int Id) {
        repo.deleteById(Id);
    }

    public List<user> getAllEmployees() {
        return repo.findAll().stream().filter(e->e.roleName.equals("employee")).toList();
    }
    public user addEmployee(Employee newEmployee){
        int id = repo.save(newEmployee).getId();
        return getUser(id);
    }

    public List<user> getAllUsers(){
        return repo.findAll();
    }

}
