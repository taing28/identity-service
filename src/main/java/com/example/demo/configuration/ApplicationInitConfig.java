package com.example.demo.configuration;

import com.example.demo.entity.User;
import com.example.demo.enums.Role;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {
    PasswordEncoder passwordEncoder;
    UserRepository userRepository;
    RoleRepository roleRepository;

    @Value("${admin.username}")
    @NonFinal
    String defaultUserName;

    @Value("${admin.password}")
    @NonFinal
    String defaultPassword;

    @Bean
    @ConditionalOnProperty(prefix = "spring",
            value = "datasource.driver-class-name",
            havingValue = "com.mysql.cj.jdbc.Driver")
        // Only use when match condition
    ApplicationRunner applicationRunner() { // Run every time app start
        return args -> {
            if (roleRepository.findById(Role.ADMIN.name()).isEmpty()) {
                com.example.demo.entity.Role role = com.example.demo.entity.Role.builder()
                        .name(Role.ADMIN.name())
                        .description("Admin role")
                        .build();
                roleRepository.save(role);
            }

            if (roleRepository.findById(Role.USER.name()).isEmpty()) {
                com.example.demo.entity.Role role = com.example.demo.entity.Role.builder()
                        .name(Role.USER.name())
                        .description("User role")
                        .build();
                roleRepository.save(role);
            }

            if (userRepository.findByUsername("admin").isEmpty()) {
                var roles = new HashSet<com.example.demo.entity.Role>();
                roles.add(roleRepository.findById(Role.ADMIN.name()).get());
                User user = User.builder()
                        .username(defaultUserName)
                        .password(passwordEncoder.encode(defaultPassword))
                        .roles(roles)
                        .build();

                userRepository.save(user);
                log.warn("role ADMIN and USER have been created without Permission, please update it!");
                log.warn("admin user has been created with default password, please change it!");
            }
        };
    }
}
