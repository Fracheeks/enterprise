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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
    @PreAuthorize("hasRole('admin')"+"|| hasRole('companyOwner')"+"|| hasRole('employee')")
    public ResponseEntity<user> getEmployeeByEmployee(@PathVariable int Id) throws AccessDeniedException {
        user User = service.getUserByUsername(getCurrentUsername());

        //companyOwner access
        if(User instanceof companyOwner){ //principal.getRoleName().equals("companyOwner
            if(((companyOwner)User).getEmployeesOfTheCompany().
                    contains(service.getUser(Id)) || User.getId() == Id){
                          return ResponseEntity.ok(service.getUser(Id));}
            else throw new AccessDeniedException("Access denied");
        }

        //employee access
        if(User instanceof Employee){
            if(User.getId()== Id) return ResponseEntity.ok(service.getUser(Id));
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
        user User = service.getUserByUsername(getCurrentUsername());
        if(User instanceof companyOwner){
          if(((companyOwner)User).getEmployeesOfTheCompany().
                  contains(service.getUser(employeeId))){
                          service.deleteUser(employeeId);}}
        else{service.deleteUser(employeeId);}

        return ResponseEntity.noContent().build();}

    //POST REQUEST======================================================================================================
    @PostMapping
    @PreAuthorize("hasRole('companyOwner')" + "|| hasRole('admin')")
    user newEmployee(@RequestBody Employee newEmployee) {
        user User = service.getUserByUsername(getCurrentUsername());

        //adding the relationship "employee-companyOwner"
        if(User instanceof companyOwner){
            ((companyOwner)User).addNewEmployee(newEmployee);
            newEmployee.setCompanyOwner((companyOwner) User);
        }

        return service.addEmployee(newEmployee);
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBootKeycloakExampleApplication.class, args);
    }

    //PUT REQUEST======================================================================================================

    //add an employee by the Admin
    @PutMapping("/{companyName}/{employeeId}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<user> putAnEmployeeIntoACompany (@PathVariable String companyName, @PathVariable int employeeId) throws ClassNotFoundException {
            //find the user
            Employee emp = (Employee) service.getUser(employeeId);

            if (emp == null || !emp.getRoleName().equals("employee")) {
                throw new ClassNotFoundException("employee not exists");
            }

            //find the company owner
        
            companyOwner own = (companyOwner) service.getAllCompanyOwner().stream().
                    filter(e -> ((companyOwner) e).getCompany().equals(companyName)).findFirst().orElse(null);

            if (own == null) {
                throw new ClassNotFoundException("company not exists");
            }

            emp.setCompanyOwner(own);
            own.addNewEmployee(emp);
            return ResponseEntity.ok(emp);

        }

    //add an employee by the companyOwner
    @PutMapping("/{employeeId}")
    @PreAuthorize("hasRole('companyOwner')")
    public ResponseEntity<user> putAnEmployeeIntoACompanyByCompanyOwner (@PathVariable int employeeId) throws ClassNotFoundException {

        companyOwner principal = (companyOwner) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Employee emp = (Employee) service.getUser(employeeId);

        if (emp == null) {
            throw new ClassNotFoundException("employee not exists");
        }

        emp.setCompanyOwner(principal);
        principal.addNewEmployee(emp);
        return ResponseEntity.ok(emp);
    }

    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        if (principal instanceof Principal) {
            return ((Principal) principal).getName();
        }
        return String.valueOf(principal);
    }
}
