package com.javatechie.keycloak.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "Employee")
@NoArgsConstructor
@Data
public class Employee extends user {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "salary")
    private double salary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "companyOwner_id")
    private companyOwner CompanyOwner;

    @Column(name = "roleName")
    public final static String roleName = "employee";

    public Employee(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }

    public Employee(String name, double salary, companyOwner owner) {
        this.name = name;
        this.salary = salary;
        this.CompanyOwner=owner;
    }
}
