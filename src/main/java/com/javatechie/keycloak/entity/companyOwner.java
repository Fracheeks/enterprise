package com.javatechie.keycloak.entity;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "companyOwner_user")
@NoArgsConstructor
@Data
public class companyOwner extends user {

    @Column(name = "name")
    private String name;


    @OneToMany(fetch = FetchType.LAZY,mappedBy = "CompanyOwner")
    private List<Employee> employeesOfTheCompany;

    public companyOwner(String name){
        this.name = name;
        super.roleName="companyOwner";
    }

    public Collection<Employee> getEmployeesOfTheCompany() {
        return this.employeesOfTheCompany;
    }

    public void addNewEmployee(Employee newEmployee){
        employeesOfTheCompany.add(newEmployee);
    }
}
