package ma.emsi.testautomation.repository;

import ma.emsi.testautomation.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    boolean existsByName(String name);  // To check if a role exists by name
    Optional<Role> findByName(String name); // ✅ no 'static'
}

