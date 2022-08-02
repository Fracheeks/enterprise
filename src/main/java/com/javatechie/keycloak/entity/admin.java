/*package com.javatechie.keycloak.entity;

import javax.persistence.*;
import java.util.*;

@Entity
public class admin{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO )
    private final int id=0;

    @Column(name = "Istance")
    private static admin Istance = null;

    @Column(name = "roleName")
    public final static String roleName = "admin";

    @OneToMany(mappedBy = "Admin")
    private List<companyOwner> companyOwnersManagedByTheAdmin;

    public void addOwner(companyOwner owner) {
        companyOwnersManagedByTheAdmin.add(owner);}

    public admin(){
      companyOwnersManagedByTheAdmin = new ArrayList<>();
    };

    public admin getIstance(){
        if(Istance==null) Istance= new admin();
        return Istance;

    }

}*/
