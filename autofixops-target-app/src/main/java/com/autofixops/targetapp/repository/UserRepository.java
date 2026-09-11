package com.autofixops.targetapp.repository;

import com.autofixops.targetapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    // INCIDENT TYPE 3: SQL / Query Failure
    // The actual column is 'username', but this native query expects 'user_name'.
    // Calling this will result in: column "user_name" does not exist
    @Query(value = "SELECT * FROM users WHERE user_name = :username LIMIT 1", nativeQuery = true)
    User findByUsernameIncident(@Param("username") String username);
}
