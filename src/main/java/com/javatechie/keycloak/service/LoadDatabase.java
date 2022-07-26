package com.javatechie.keycloak.service;

import com.javatechie.keycloak.entity.Employee;
import com.javatechie.keycloak.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(EmployeeRepository employeeRepository) {

        return args -> {
            //adding admin

            //adding companyOwner

            //adding employees
            employeeRepository.save(new Employee("Steve", 1500));
            employeeRepository.save(new Employee("Fred", 2000));
            employeeRepository.findAll().forEach(employee -> log.info("Preloaded " + employee));


        };
    }
}
