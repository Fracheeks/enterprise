package com.javatechie.keycloak;

import com.javatechie.keycloak.entity.*;
import com.javatechie.keycloak.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@SpringBootApplication
@RestController
@RequestMapping("/employees")
public class SpringBootKeycloakExampleApplication {

    @Autowired
    private userService service;

    //GET REQUEST
    @GetMapping("/{Id}")
    public ResponseEntity<user> getEmployeebyEmployee(@PathVariable Long Id) throws AccessDeniedException {
        user principal = (user) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //companyOwner access
        if(principal instanceof companyOwner){
            if(((companyOwner)principal).getEmployeesOfTheCompany().
                    contains(service.getUser(Id)) || ((companyOwner)principal).getId() == Id){
                          return ResponseEntity.ok(service.getUser(Id));}
            else throw new AccessDeniedException("Access denied");
        }

        //employee access
        if(principal instanceof Employee){
            if(((Employee)principal).getId()== Id) return ResponseEntity.ok(service.getUser(Id));
            else throw new AccessDeniedException("Access denied");
        }

        //admin access
        return ResponseEntity.ok(service.getUser(Id));}

    //get all users
    @GetMapping
    @PostAuthorize("hasRole('admin')")
    public  ResponseEntity<List<user>> loadAllUsers () {
        return ResponseEntity.ok(service.getAllEmployees());
    }

    //get all employees of a company
    @GetMapping("/company/{companyName}")
    @PostAuthorize("hasRole('admin')")
    public  ResponseEntity<List<user>> loadAllEmployees (@PathVariable String companyName) {
        return ResponseEntity.ok(service.getAllEmployees().stream().
                filter(e->((Employee)e).getCompanyOwner().getCompany().equals(companyName)).toList());
    }

    //get all companies
    @GetMapping("/company")
    @PostAuthorize("hasRole('admin')")
    public  ResponseEntity<List<String>> loadAllEmployees () {
        return ResponseEntity.ok(service.getAllCompanyOwner().stream().map(e->((companyOwner)e).getCompany()).toList());
    }

    //DELETE REQUEST
    @DeleteMapping("/{employeeId}")
    @PreAuthorize("hasRole('companyOwner')"+"|| hasRole('admin')")
    ResponseEntity<?> deleteEmployeeByCompanyOwner(@PathVariable Long employeeId) {
        user principal = (user)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof companyOwner){
          if(((companyOwner)principal).getEmployeesOfTheCompany().
                  contains(service.getUser(employeeId))){
                          service.deleteUser(employeeId);}}
        else{service.deleteUser(employeeId);}

        return ResponseEntity.noContent().build();}

    //POST REQUEST
    @PostMapping
    @PreAuthorize("hasRole('companyOwner')" + "|| hasRole('admin')")
    user newEmployee(@RequestBody Employee newEmployee) {
        user principal = (user)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //adding the relationship "employee-companyOwner"
        if(principal instanceof companyOwner){
            ((companyOwner)principal).addNewEmployee(newEmployee);
            newEmployee.setCompanyOwner((companyOwner) principal);
        }

        return service.addEmployee(newEmployee);
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBootKeycloakExampleApplication.class, args);
    }

}
