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

    private String username;

    @Column(name = "name")
    private String name;

    @Column(name = "roleName")
    public final static String roleName = "companyOwner";

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "admin_id")
    admin Admin = new admin();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "employees_Of_The_Company",
    joinColumns = @JoinColumn(name = "companyOwner_id"),
    inverseJoinColumns = @JoinColumn(name = "Employee_id"))
    private List<Employee> employeesOfTheCompany;

    public companyOwner(String name, String username){
        this.name = name;
        Admin.addOwner(this);
    }
}
