package com.javatechie.keycloak.entity;

import javax.persistence.*;
import java.util.*;
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
    private List<Employee> employeesOfTheCompany = new ArrayList<>();

    public companyOwner(String username, String company){
        super.username = username;
        this.company=company;
        super.roleName="companyOwner";
    }

    public companyOwner (String username, String company, String userIdByToken){
        super.username=username;
        this.company=company;
        this.userIdByToken=userIdByToken;
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
