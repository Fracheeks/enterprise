package com.javatechie.keycloak.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "Employee_user")
@NoArgsConstructor
@Data
public class Employee extends user {

    @Column(name = "name")
    private String name;

    @Column(name = "salary")
    private double salary;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "companyOwner_id")
    private companyOwner CompanyOwner=null;

    public Employee(String name, double salary) {
        this.name = name;
        this.salary = salary;
        super.roleName="employee";
    }

    public Employee(String name, double salary, companyOwner owner) {
        this.name = name;
        this.salary = salary;
        this.CompanyOwner=owner;
        super.roleName="employee";
    }

    public companyOwner getCompanyOwner() {
        return CompanyOwner;
    }
}
