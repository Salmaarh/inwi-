package ma.emsi.testautomation.service;

import ma.emsi.testautomation.model.AppUser;
import ma.emsi.testautomation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        if (appUser.getPasswordExpirationDate() != null &&
                appUser.getPasswordExpirationDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Le mot de passe de l'utilisateur a expiré");
        }

        // Conversion des rôles (authorities) en SimpleGrantedAuthority
        List<SimpleGrantedAuthority> authorities = appUser.getAuthorities().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .toList();

        return new org.springframework.security.core.userdetails.User(
                appUser.getUsername(),
                appUser.getPassword(),
                authorities
        );
    }
}