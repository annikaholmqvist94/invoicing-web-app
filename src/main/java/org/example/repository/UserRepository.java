package org.example.repository;

import org.example.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    // Hittar en användare baserat på e-post (bra för login)
    Optional<User> findByEmail(String email);
}