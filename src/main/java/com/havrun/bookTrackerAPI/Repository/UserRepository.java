package com.havrun.bookTrackerAPI.Repository;

import com.havrun.bookTrackerAPI.entity.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    Optional<User> findById(int id);
    Optional<User> findByEmail(String email);
    Optional<User> findByVerificationCode (String verificationCode);
}
