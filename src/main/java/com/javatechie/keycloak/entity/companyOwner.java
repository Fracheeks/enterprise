package com.javatechie.keycloak.entity;

import javax.persistence.*;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class companyOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "roleName")
    static String roleName = "companyOwner";

    @ManyToOne
    @JoinColumn(name = "admin_id")
    admin Admin;

    @ManyToMany
    @JoinTable(name = "employees_Of_The_Company",
    joinColumns = @JoinColumn(name = "companyOwner_id"),
    inverseJoinColumns = @JoinColumn(name = "Employee_id"))
    private List<Employee> employeesOfTheCompany;

    public companyOwner(String name){
        this.name = name;
    }
}
