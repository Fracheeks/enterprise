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
    private EmployeeService service;
    private companyOwnerService service2;

    //GET REQUEST
    @GetMapping("/{employeeId}")
    @PreAuthorize ("returnObject.username == authentication.principal.username"+ "|| hasRole('" + admin.roleName + "')" + "|| hasRole('" + companyOwner.roleName + "')")
    public ResponseEntity<Employee> getEmployeebyEmployee(@PathVariable int employeeId) throws AccessDeniedException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof companyOwner){
            if(((companyOwner)principal).getEmployeesOfTheCompany().contains(service.getEmployee(employeeId))) return ResponseEntity.ok(service.getEmployee(employeeId));
            else throw new AccessDeniedException("Access denied");
        }
        return ResponseEntity.ok(service.getEmployee(employeeId));}

  /*  @GetMapping("/{employeeId}")
    @PreAuthorize("hasRole('" + companyOwner.roleName + "')")
    public ResponseEntity<Employee> getEmployeebyCompanyOwner(@PathVariable int employeeId) throws AccessDeniedException {
        companyOwner principal = (companyOwner) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(principal.getEmployeesOfTheCompany().contains(service.getEmployee(employeeId)))
            return ResponseEntity.ok(service.getEmployee(employeeId));
        else throw new AccessDeniedException("Access denied");}

    //this method can be accessed by user whose role is admin*/

    @GetMapping
    @PostAuthorize("hasRole('" + admin.roleName + "')")
    public  ResponseEntity<List<Employee>> loadAllEmployees () {
        return ResponseEntity.ok(service.getAllEmployees());
    }

   /* @PostAuthorize("returnObject.username == authentication.principal.nickName")
    public admin loadAdminDetail(String username) {
        return service3.getAdminbyUsername(username);
    }*/

    //DELETE REQUEST
    @DeleteMapping("/{employeeId}")
    @PreAuthorize("hasRole('" + companyOwner.roleName + "')"+"|| hasRole('" + admin.roleName + "')")
    ResponseEntity<?> deleteEmployeeByCompanyOwner(@PathVariable Long id) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof companyOwner){
          if(((companyOwner)principal).getEmployeesOfTheCompany().contains(service.getEmployee(Math.toIntExact(id))))service.deleteEmployee(id);}
        else{service.deleteEmployee(id);}

         return ResponseEntity.noContent().build();}

    /*@DeleteMapping("/{employeeId}")
    @PreAuthorize("hasRole('" + admin.roleName + "')")
    ResponseEntity<?> deleteEmployeeByAdmin(@PathVariable Long id) {
        service.deleteEmployee(id);
        return ResponseEntity.noContent().build();}*/

    //POST REQUEST
    @PostMapping
    @PreAuthorize("hasRole('" + companyOwner.roleName + "')" + "|| hasRole('" + admin.roleName + "')")
    Employee newEmployee(@RequestBody Employee newEmployee) {
        return service.addEmployee(newEmployee);
    }



    public static void main(String[] args) {
        SpringApplication.run(SpringBootKeycloakExampleApplication.class, args);
    }

}
