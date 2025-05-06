package ma.emsi.testautomation.model;

import jakarta.persistence.*;
import ma.emsi.testautomation.entity.Role;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;
    private LocalDate passwordExpirationDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "USER_ROLE",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> authorities = new ArrayList<>();


    // --- Getters et Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public LocalDate getPasswordExpirationDate() { return passwordExpirationDate; }
    public void setPasswordExpirationDate(LocalDate passwordExpirationDate) {
        this.passwordExpirationDate = passwordExpirationDate;
    }

    public List<Role> getAuthorities() { return authorities; }
    public void setAuthorities(List<Role> authorities) { this.authorities = authorities; }
}

