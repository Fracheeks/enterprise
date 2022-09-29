package com.javatechie.keycloak.service;

import com.javatechie.keycloak.entity.Employee;
import com.javatechie.keycloak.entity.companyOwner;
import com.javatechie.keycloak.repository.userRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);
    @Bean
    CommandLineRunner initDatabase(userRepository repo) {

        return args -> {
            repo.save(new Employee("john", 20000));
            repo.save(new Employee("mak", 55000));
            repo.save(new Employee("liam", 2222222));
            repo.save(new companyOwner("fred","bancasella", 5000));
            repo.save(new companyOwner("jack", "ferrari", 2345));
            repo.findAll().forEach(user -> log.info("Preloaded" + user));
        };
    };
}


