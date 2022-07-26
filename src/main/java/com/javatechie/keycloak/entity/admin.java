package com.javatechie.keycloak.entity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
public class admin{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "roleName")
    static String roleName = "admin";

    @OneToMany(mappedBy = "Admin")
    private List<companyOwner> companyOwnersManagedByTheAdmin;

    public admin(String name){
        this.name=name;
    }

    public admin(String name,companyOwner owner) {
        this.name=name;}
}
