package com.javatechie.keycloak;

import com.javatechie.keycloak.entity.*;
import com.javatechie.keycloak.service.*;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;

@SpringBootApplication
@RestController
@RequestMapping("/users")
public class SpringBootKeycloakExampleApplication {

    @Autowired
    private userService service;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootKeycloakExampleApplication.class, args);
    }

    private String getCurrentUserIdByToke() {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            KeycloakPrincipal<KeycloakSecurityContext> kPrincipal = (KeycloakPrincipal<KeycloakSecurityContext>) principal;
            return kPrincipal.getKeycloakSecurityContext().getIdToken().getSubject();
    }

    private Set<String> getCurrentUserRole() {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        KeycloakPrincipal<KeycloakSecurityContext> kPrincipal = (KeycloakPrincipal<KeycloakSecurityContext>) principal;
        AccessToken.Access access  = (AccessToken.Access) kPrincipal.getKeycloakSecurityContext().getToken().getResourceAccess();
        return access.getRoles();
    }

    user findUser() {
        user User = service.getUserIdByToken(getCurrentUserIdByToke());

        //add employee from keycloak
        if (User == null && getCurrentUserRole().contains("employee")) {
            service.addUser(new Employee(getCurrentUserIdByToke()));
        }

        //add companyOwner from keycloak
        if (User == null && getCurrentUserRole().contains("companyOwner")) {
            service.addUser(new companyOwner(getCurrentUserIdByToke()));
        }

        return User;
    }

    /*==================================================================================================================
    GET REQUEST
    ==================================================================================================================*/


    //get ALL USERS
    @GetMapping
    @PreAuthorize("hasRole('admin')")
    public  ResponseEntity<List<user>> loadAllUsers () {
        return ResponseEntity.ok(service.getAllUsers());
    }

    @GetMapping("/{Id}")
    @PreAuthorize("hasRole('admin')"+"|| hasRole('companyOwner')"+"|| hasRole('employee')")
    public ResponseEntity<user> getEmployeeByEmployee(@PathVariable int Id) throws AccessDeniedException {

        user User = findUser();

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
        return ResponseEntity.ok(service.getAllEmployees().stream().filter(e->((Employee)e).getCompanyOwner()!=null).
                filter(e->((Employee)e).getCompanyOwner().getCompany().equals(companyName)).toList());
    }

    //get ALL COMPANIES
    @GetMapping("/company")
    @PreAuthorize("hasRole('admin')")
    public  ResponseEntity<List<String>> loadAllCompanies() {
        return ResponseEntity.ok(service.getAllCompanyOwner().stream().map(e->((companyOwner)e).getCompany()).toList());
    }


    /*==================================================================================================================
    DELETE REQUEST
    ==================================================================================================================*/

    //delete an employee
    @DeleteMapping("/{employeeId}")
    @PreAuthorize("hasRole('companyOwner')"+"|| hasRole('admin')")
    ResponseEntity<?> deleteEmployeeByCompanyOwner(@PathVariable int employeeId) {
        user User = findUser();
        if(User instanceof companyOwner){
          if(((companyOwner)User).getEmployeesOfTheCompany().
                  contains(service.getUser(employeeId))){
                          service.deleteUser(employeeId);}}
        else{service.deleteUser(employeeId);}

        return ResponseEntity.noContent().build();}

    //remove an employee from a company by the admin
    @DeleteMapping("remove/{companyName}/{employeeId}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<user> deleteAnEmployeeFromACompany (@PathVariable String companyName, @PathVariable int employeeId) throws ClassNotFoundException {
        //find the user
        user emp = service.getUser(employeeId);

        if (emp == null || !emp.getRoleName().equals("employee")) {
            throw new ClassNotFoundException("employee not exists");
        }

        if (((Employee)emp).getCompanyOwner()==null) {
            throw new ClassNotFoundException("employee not assigned");
        }

        companyOwner own = (companyOwner) service.getAllCompanyOwner().stream().
                filter(e -> ((companyOwner) e).getCompany().equals(companyName)).findFirst().orElse(null);

        if (own == null) {
            throw new ClassNotFoundException("company not exists");
        }

        own.deleteAnEmployee((Employee) emp); ((Employee) emp).setCompanyOwner(null);
        service.addUser(emp);service.addUser(own);

        return ResponseEntity.noContent().build();}

    //remove an employee from a company by the companyOwner
    @DeleteMapping("remove/{employeeId}")
    @PreAuthorize("hasRole('companyOwner')")
    public ResponseEntity<user> deleteAnEmployeeFromACompanyByCompanyOwner (@PathVariable int employeeId) throws ClassNotFoundException {

        user User = findUser();
        Employee emp = (Employee) service.getUser(employeeId);

        if (emp == null) {
            throw new ClassNotFoundException("employee not exists");
        }

        if (emp.getCompanyOwner()!=null) {
            throw new ClassNotFoundException("employee already assigned");
        }

        emp.setCompanyOwner(null);  ((companyOwner)User).deleteAnEmployee(emp);
        service.addUser(emp);service.addUser(User);
        return ResponseEntity.ok(emp);
    }


    /*==================================================================================================================
    POST REQUEST
    ==================================================================================================================*/

    @PostMapping
    @PreAuthorize("hasRole('companyOwner')" + "|| hasRole('admin')")
    user newEmployee(@RequestBody Employee newEmployee) {
        user User = findUser();

        //adding the relationship "employee-companyOwner"
        if(User instanceof companyOwner){
            ((companyOwner)User).addNewEmployee(newEmployee);
            newEmployee.setCompanyOwner((companyOwner) User);
        }

        return service.addUser(newEmployee);
    }


    /*==================================================================================================================
    PUT REQUEST
    ==================================================================================================================*/

    //add an employee by the Admin
    @PutMapping("/add/{companyName}/{employeeId}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<user> putAnEmployeeIntoACompany (@PathVariable String companyName, @PathVariable int employeeId) throws ClassNotFoundException {
            //find the user
            user emp = service.getUser(employeeId);

            if (emp == null || !emp.getRoleName().equals("employee")) {
                throw new ClassNotFoundException("employee not exists");
            }

            if (((Employee)emp).getCompanyOwner()!=null) {
                throw new ClassNotFoundException("employee already assigned");
            }

        //find the company owner
        
            companyOwner own = (companyOwner) service.getAllCompanyOwner().stream().
                    filter(e -> ((companyOwner) e).getCompany().equals(companyName)).findFirst().orElse(null);

            if (own == null) {
                throw new ClassNotFoundException("company not exists");
            }

            own.addNewEmployee((Employee) emp); ((Employee)emp).setCompanyOwner(own);
            service.addUser(emp);service.addUser(own);

            return ResponseEntity.ok(own);

        }

    //add an employee by the companyOwner
    @PutMapping("/add//{employeeId}")
    @PreAuthorize("hasRole('companyOwner')")
    public ResponseEntity<user> putAnEmployeeIntoACompanyByCompanyOwner (@PathVariable int employeeId) throws ClassNotFoundException {

        user User = findUser();
        Employee emp = (Employee) service.getUser(employeeId);

        if (emp == null) {
            throw new ClassNotFoundException("employee not exists");
        }

        if (emp.getCompanyOwner()!=null) {
            throw new ClassNotFoundException("employee already assigned");
        }

        emp.setCompanyOwner((companyOwner) User); ((companyOwner)User).addNewEmployee(emp);
        service.addUser(emp);service.addUser(User);
        return ResponseEntity.ok(emp);
    }
}
