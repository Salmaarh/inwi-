package ma.emsi.testautomation.service;

import lombok.AllArgsConstructor;
import ma.emsi.testautomation.entity.Role;
import ma.emsi.testautomation.model.AppUser;
import ma.emsi.testautomation.repository.RoleRepository;
import ma.emsi.testautomation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@AllArgsConstructor
    @Service
    public class UserService {


        private final RoleRepository roleRepository;
        private UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;

    public AppUser createUser(AppUser user) {
        // Step 1: Generate a unique username
        String uniqueUsername = generateUniqueUsername(user.getFirstName(), user.getLastName());
        user.setUsername(uniqueUsername);

        // Step 2: Encode the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPasswordExpirationDate(LocalDate.now().plusDays(90)); // 90 days validity

        // Step 3: Validate roles
        if (user.getAuthorities() == null || user.getAuthorities().isEmpty()) {
            throw new RuntimeException("User must have at least one role specified");
        }

        List<Role> validatedRoles = new ArrayList<>();
        for (Role role : user.getAuthorities()) {
            Optional<Role> dbRole = roleRepository.findByName(role.getName());
            if (dbRole.isEmpty()) {
                throw new RuntimeException("Role not found: " + role.getName());
            }
            validatedRoles.add(dbRole.get());
        }

        user.setAuthorities(validatedRoles);

        //  Step 4: Save the user
        return userRepository.save(user);
    }

    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<AppUser> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public AppUser updateUser(Long id, AppUser updatedUser) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setUsername(updatedUser.getUsername());
                    user.setEmail(updatedUser.getEmail());
                    user.setLastName(updatedUser.getLastName());
                    user.setFirstName(updatedUser.getFirstName());
                    user.setAuthorities(updatedUser.getAuthorities()); // mise à jour des rôles
                    return userRepository.save(user);
                })
                .orElse(null);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    private String generateUniqueUsername(String firstName, String lastName) {
        String baseUsername = (lastName + "." + firstName).toLowerCase().replaceAll("\\s+", "");
        String username = baseUsername;
        int counter = 1;

        while (userRepository.existsByUsername(username)) {
            username = baseUsername + counter;
            counter++;
        }

        return username;
    }


}
