package com.javatechie.keycloak.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity(name = "Employee")
@NoArgsConstructor
@Data
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "salary")
    private double salary;

    @ManyToMany(mappedBy = "employeesOfTheCompany")
    private List<companyOwner> CompaniesOfTheEmployee;

    @Column(name = "roleName")
    static String roleName = "employee";

    public Employee(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }

    public Employee(String name, double salary, companyOwner owner) {
        this.name = name;
        this.salary = salary;
        this.getCompaniesOfTheEmployee().add(owner);
    }
}
