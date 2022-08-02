package com.javatechie.keycloak.entity;

import javax.persistence.*;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class companyOwner extends user {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "roleName")
    public final static String roleName = "companyOwner";


    @OneToMany(fetch = FetchType.LAZY,mappedBy = "CompanyOwner")
    private List<Employee> employeesOfTheCompany;

    public companyOwner(String name){
        this.name = name;
    }
}
