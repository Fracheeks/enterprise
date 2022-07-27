/*package com.javatechie.keycloak.service;

        import com.javatechie.keycloak.entity.admin;
        import com.javatechie.keycloak.repository.adminRepository;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;

        import javax.annotation.PostConstruct;
        import java.util.List;
        import java.util.stream.Collectors;
        import java.util.stream.Stream;

@Service
public class adminService {
    @Autowired
    private adminRepository AdminRepository;

    @PostConstruct
    public void initializeAdminTable() {
        AdminRepository.saveAll(
                Stream.of(
                        new admin()
                ).collect(Collectors.toList()));
    }

    public admin getCompanyOwnerbyId(int employeeId) {
        return AdminRepository
                .findById(employeeId)
                .orElse(null);
    }
    public admin getAdminbyUsername(String username) {
        return AdminRepository
                .findByUsername(username);
    }

    public List<admin> getAllCompanyOwner() {
        return AdminRepository.findAll();
    }
}*/

