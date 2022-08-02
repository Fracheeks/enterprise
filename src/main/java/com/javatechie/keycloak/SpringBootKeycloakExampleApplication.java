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
    @GetMapping("/{employeeId}")
    public ResponseEntity<user> getEmployeebyEmployee(@PathVariable Long employeeId) throws AccessDeniedException {
        user principal = (user) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //companyOwner access
        if(principal instanceof companyOwner){
            if(((companyOwner)principal).getEmployeesOfTheCompany().contains(service.getEmployee(employeeId))) return ResponseEntity.ok(service.getEmployee(employeeId));
            else throw new AccessDeniedException("Access denied");
        }

        //employee access
        if(principal instanceof Employee){
            if(((Employee)principal).getId()==employeeId) return ResponseEntity.ok(service.getEmployee(employeeId));
            else throw new AccessDeniedException("Access denied");
        }

        //admin access
        return ResponseEntity.ok(service.getEmployee(employeeId));}

    @GetMapping
    @PostAuthorize("hasRole('admin')")
    public  ResponseEntity<List<user>> loadAllEmployees () {
        return ResponseEntity.ok(service.getAllEmployees());
    }

    //DELETE REQUEST
    @DeleteMapping("/{employeeId}")
    @PreAuthorize("hasRole('companyOwner')"+"|| hasRole('admin')")
    ResponseEntity<?> deleteEmployeeByCompanyOwner(@PathVariable Long id) {
        user principal = (user)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof companyOwner){
          if(((companyOwner)principal).getEmployeesOfTheCompany().
                  contains(service.getEmployee(id))){
                          service.deleteEmployee(id);}}
        else{service.deleteEmployee(id);}

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
