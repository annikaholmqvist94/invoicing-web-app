package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.user.CreateUserDTO;
import org.example.entity.user.User;
import org.example.entity.company.Company;
import org.example.repository.UserRepository;
import org.example.repository.CompanyRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    @Transactional
    public void run(String... args) {
        try{
            if (userRepository.count() == 0) {
                userService.registerUser(new CreateUserDTO(
                        "Admin",
                        "User",
                        "admin@example.com",
                        "Admin1234!"
                ));
                System.out.println(">>> Test user created successfully!");
            } else {
                System.out.println(">>> Database already contains users, skipping initialization.");
            }
        } catch(Exception e){
            System.err.println("Could not initialize data: " + e.getMessage());
        }
    }
}