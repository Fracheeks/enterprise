package com.javatechie.keycloak.service;

import com.javatechie.keycloak.repository.userRepository;
import com.javatechie.keycloak.entity.user;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

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
    public user addUser(user newUser){
        int id = repo.save(newUser).getId();
        return getUser(id);
    }

    public List<user> getAllUsers(){
        return repo.findAll();
    }

    public user getUserByUsername(String username) {
        return getAllEmployees().stream().filter(e->e.getUsername().equals(username)).findFirst().orElse(null);
    }
}
