package com.autofixops.targetapp.service;

import com.autofixops.targetapp.entity.User;
import com.autofixops.targetapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // INCIDENT TYPE 2: Configuration / Environment Failure
    // In config-failure scenario, TARGET_ENV will be misconfigured.
    @Value("${TARGET_ENV:default}")
    private String targetEnv;

    @PostConstruct
    public void init() {
        if ("CRASH".equals(targetEnv)) {
            throw new IllegalStateException("Configuration Failure: Application expected TARGET_ENV to be 'production' but was configured to 'CRASH'.");
        }
        
        // Seed some data
        if (userRepository.count() == 0) {
            userRepository.save(new User("admin", "admin@example.com"));
        }
    }

    // INCIDENT TYPE 1: Runtime Failure
    public User getUserById(Long id) {
        // Unsafe Optional access that will throw NoSuchElementException if user doesn't exist
        return userRepository.findById(id).get();
    }

    // INCIDENT TYPE 3: SQL Failure Trigger
    public User getUserByUsername(String username) {
        return userRepository.findByUsernameIncident(username);
    }
}
