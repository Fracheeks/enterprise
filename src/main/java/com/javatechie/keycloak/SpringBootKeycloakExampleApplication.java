package com.javatechie.keycloak;

import com.javatechie.keycloak.entity.Employee;
import com.javatechie.keycloak.entity.admin;
import com.javatechie.keycloak.entity.companyOwner;
import com.javatechie.keycloak.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@SpringBootApplication
@RestController
@RequestMapping("/employees")
public class SpringBootKeycloakExampleApplication {

    @Autowired
    private EmployeeService service;

    //this method can be accessed by user whose role is user
    @GetMapping("/{employeeId}")
    @PreAuthorize("hasRole('" + Employee.roleName + "')")
    public ResponseEntity<Employee> getEmployeebyEmployee(@PathVariable int employeeId) throws AccessDeniedException {
        if(service.getEmployee(employeeId)!=null) return ResponseEntity.ok(service.getEmployee(employeeId));
        else throw new AccessDeniedException("Access denied");}

    @GetMapping("/{employeeId}")
    @PreAuthorize("hasRole('" + companyOwner.roleName + "')")
    public ResponseEntity<Employee> getEmployeebyCompanyOwner(@PathVariable int employeeId) throws AccessDeniedException {
        if(service.getEmployee(employeeId)!=null) return ResponseEntity.ok(service.getEmployee(employeeId));
        else throw new AccessDeniedException("Access denied");}

    @GetMapping("/{employeeId}")
    @PreAuthorize("hasRole('" + admin.roleName + "')")
    public ResponseEntity<Employee> getEmployeebyAdmin(@PathVariable int employeeId) throws AccessDeniedException {
       return ResponseEntity.ok(service.getEmployee(employeeId));}


    //this method can be accessed by user whose role is admin
    @GetMapping
    @RolesAllowed("admin")
    public ResponseEntity<List<Employee>> findALlEmployees() {
        return ResponseEntity.ok(service.getAllEmployees());
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBootKeycloakExampleApplication.class, args);
    }

}
