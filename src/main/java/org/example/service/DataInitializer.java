package org.example.service;

import org.example.entity.user.User;
import org.example.entity.company.Company;
import org.example.repository.UserRepository;
import org.example.repository.CompanyRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    public DataInitializer(UserRepository userRepository, CompanyRepository companyRepository) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            // Skapa testföretag
            Company company = new Company();
            company.setName("Mitt Testföretag AB");
            company.setOrgNum("556677-8899");
            companyRepository.save(company);

            // Skapa testanvändare
            User user = new User();
            user.setFirstName("Annika");
            user.setLastName("Test");
            user.setEmail("test@example.com");
            user.setPassword("password123"); // Kom ihåg: i verkligheten ska detta krypteras!

            // Koppla ihop dem!
            user.getCompanies().add(company);
            userRepository.save(user);

            System.out.println(">> Testdata har skapats i Azure-databasen!");
        }
    }
}