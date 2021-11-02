package com.example.ReExam.repo;

import com.example.ReExam.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailsRepo extends JpaRepository<User, String> {
    User findByUsername(String username);
}
