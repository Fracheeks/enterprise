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
@RequestMapping("/users")
public class SpringBootKeycloakExampleApplication {

    @Autowired
    private userService service;

    //GET REQUEST ======================================================================================================


    //get ALL USERS
    @GetMapping
    @PreAuthorize("hasRole('admin')")
    public  ResponseEntity<List<user>> loadAllUsers () {
        return ResponseEntity.ok(service.getAllUsers());
    }

    @GetMapping("/{Id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<user> getEmployeebyEmployee(@PathVariable int Id) throws AccessDeniedException {
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


    //get ALL EMPLOYEES
    @GetMapping("/employees")
    @PreAuthorize("hasRole('admin')")
    public  ResponseEntity<List<user>> loadAllEmployees () {
        return ResponseEntity.ok(service.getAllEmployees());
    }

    //get ALL EMPLOYEES of a COMPANY
    @GetMapping("/company/{companyName}")
    @PreAuthorize("hasRole('admin')")
    public  ResponseEntity<List<user>> loadAllEmployeesOfaCompany (@PathVariable String companyName) {
        return ResponseEntity.ok(service.getAllEmployees().stream().
                filter(e->((Employee)e).getCompanyOwner().getCompany().equals(companyName)).toList());
    }

    //get ALL COMPANIES
    @GetMapping("/company")
    @PreAuthorize("hasRole('admin')")
    public  ResponseEntity<List<String>> loadAllCompanies() {
        return ResponseEntity.ok(service.getAllCompanyOwner().stream().map(e->((companyOwner)e).getCompany()).toList());
    }

    //DELETE REQUEST====================================================================================================
    @DeleteMapping("/{employeeId}")
    @PreAuthorize("hasRole('companyOwner')"+"|| hasRole('admin')")
    ResponseEntity<?> deleteEmployeeByCompanyOwner(@PathVariable int employeeId) {
        user principal = (user)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof companyOwner){
          if(((companyOwner)principal).getEmployeesOfTheCompany().
                  contains(service.getUser(employeeId))){
                          service.deleteUser(employeeId);}}
        else{service.deleteUser(employeeId);}

        return ResponseEntity.noContent().build();}

    //POST REQUEST======================================================================================================
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

    //PUT REQUEST======================================================================================================

    //add an employee by the Admin
    @PutMapping("/{companyName]/{employeeId}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<user> putAnEmployeeIntoaCompanyByAdmin (@PathVariable String companyName, @PathVariable int employeeId) throws ClassNotFoundException {
        if(service.getAllCompanyOwner().stream().anyMatch(e->((companyOwner)e).getCompany().equals(companyName))){
           Employee emp = (Employee)service.getUser(employeeId);

           if(emp==null){throw new ClassNotFoundException("employee not exists");}

           companyOwner own = (companyOwner) service.getAllCompanyOwner().stream().
                   filter(e->((companyOwner)e).getCompany().equals(companyName)).findFirst().get();

           emp.setCompanyOwner(own);
           own.addNewEmployee(emp);
           return ResponseEntity.ok(emp);
          }
         throw new ClassNotFoundException("company not exists");}

    //add an employee by the companyOwner
    @PutMapping("/{companyName]/{employeeId}")
    @PreAuthorize("hasRole('companyOwner')")
    public ResponseEntity<user> putAnEmployeeIntoaCompanyByCompanyOwner (@PathVariable int employeeId) throws ClassNotFoundException {

        companyOwner principal = (companyOwner) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Employee emp = (Employee) service.getUser(employeeId);

        if (emp == null) {
            throw new ClassNotFoundException("employee not exists");
        }

        emp.setCompanyOwner(principal);
        principal.addNewEmployee(emp);
        return ResponseEntity.ok(emp);
    }
}
