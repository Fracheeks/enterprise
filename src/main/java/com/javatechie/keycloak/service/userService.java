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

    public user getUser(int id) {
        return repo
                .findById(id)
                .orElse(null);
    }

    public void  deleteUser(int Id) {
        repo.deleteById(Id);
    }

    public List<user> getATypeOfUsers(String type) {
        return repo.findAll().stream().filter(e->type.equals(e.getRoleName())).toList();
    }
    public user addUser(user newUser){
        int id = repo.save(newUser).getId();
        return getUser(id);
    }

    public List<user> getAllUsers(){
        return repo.findAll();
    }

    public user getUserIdByToken(String IdToken) {
        return getAllUsers().stream().filter(e->IdToken.equals(e.getUserIdByToken())).findFirst().orElse(null);
    }
}
