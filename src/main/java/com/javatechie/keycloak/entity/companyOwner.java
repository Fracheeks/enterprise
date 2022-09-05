package com.javatechie.keycloak.entity;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "companyOwner")
@NoArgsConstructor
@Data
public class companyOwner extends user {

    @Column(name = "company")
    private String company;

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "CompanyOwner")
    private List<Employee> employeesOfTheCompany;

    public companyOwner(String username, String company){
        super.username = username;
        this.company=company;
        super.roleName="companyOwner";
    }

    public Collection<Employee> getEmployeesOfTheCompany() {
        return this.employeesOfTheCompany;
    }

    public void addNewEmployee(Employee newEmployee){
        employeesOfTheCompany.add(newEmployee);
    }

    public void deleteAnEmployee(Employee employee){
        this.employeesOfTheCompany.remove(employee);
    }
}
