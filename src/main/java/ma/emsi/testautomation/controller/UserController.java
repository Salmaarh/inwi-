package ma.emsi.testautomation.controller;

import jakarta.persistence.JoinTable;
import ma.emsi.testautomation.entity.Role;
import ma.emsi.testautomation.model.AppUser;
import ma.emsi.testautomation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    // Obtenir tous les utilisateurs
    @GetMapping
    public ResponseEntity<List<AppUser>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Obtenir un utilisateur par ID
    @GetMapping("/{id}")
    public ResponseEntity<AppUser> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Créer un utilisateur
    @PostMapping
    public ResponseEntity<AppUser> createUser(@RequestBody AppUser appUser) {
        AppUser created = userService.createUser(appUser);
        return ResponseEntity.ok(created);
    }

    // Mettre à jour un utilisateur
    @PutMapping("/{id}")
    public ResponseEntity<AppUser> updateUser(@PathVariable Long id, @RequestBody AppUser appUser) {
        AppUser updated = userService.updateUser(id, appUser);
        if (updated != null) return ResponseEntity.ok(updated);
        else return ResponseEntity.notFound().build();
    }

    // Supprimer un utilisateur
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // Enregistrement d’un nouvel utilisateur (alias de createUser)
    @PostMapping("/register")
    public ResponseEntity<AppUser> register(@RequestBody AppUser user) {
        AppUser created = userService.createUser(user);
        return ResponseEntity.ok(created);
    }

}
